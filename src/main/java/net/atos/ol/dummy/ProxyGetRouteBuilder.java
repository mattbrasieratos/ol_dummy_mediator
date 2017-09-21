package net.atos.ol.dummy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.Exchange;
import org.apache.camel.cdi.Uri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;


@ApplicationScoped
@ContextName("camel-cdi-context")
public class ProxyGetRouteBuilder
        extends RouteBuilder
{
    public static final String V1 = "v1/";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    String host = "dummy-service";
    String port = "8080";
    String context = "/ol_dummy_service/dummy/";


    @Inject
    @Uri("jetty:http://0.0.0.0:1080?matchOnUriPrefix=true")
    private Endpoint jettyEndpoint;



    public ProxyGetRouteBuilder() {}

    public void configure() throws Exception
    {
        readProperties();
        from("direct:dummy-v1")
                .routeId("dummy-mediation-v1")
                .errorHandler(loggingErrorHandler("dummy-mediation").level(LoggingLevel.ERROR))
                .setHeader("request-path",simple("${header.CamelHttpPath}"))
                .log(simple("v1:Received request for ${header.request-path} from ${header.request-ip}").getText())
                .setHeader(Exchange.HTTP_URI,
                            simple("http4://" + host + ":" + port))
                //In this instance we set the path to the incoming path. We might want to translate for other services
                .setHeader(Exchange.HTTP_PATH, simple(context+"${header.request-path}"))
                .to("http4://dummy:12345?throwExceptionOnFailure=false") //URI here is overridden using header above
                .log(simple("v1:Received response from http://" + host + ":" + port +"${header.CamelHttpPath}").getText())
                .convertBodyTo(java.lang.String.class);

        from(jettyEndpoint)
                .routeId("dummy-mediation")
                .setHeader("request-path",simple("${header.CamelHttpPath}"))
                .log(simple("version-select:Received request for ${header.request-path} from {$header.request-ip}").getText())
                .process(new Processor() {
                   @Override
                   public void process(Exchange exchange) throws Exception {
                     String requestPath = (String)exchange.getIn().getHeader(Exchange.HTTP_PATH);
                     if (requestPath.startsWith(V1))
                     {
                        requestPath = requestPath.substring(requestPath.indexOf(V1)+V1.length(),requestPath.length());
                        exchange.getOut().setHeader("service-version","1");
                        exchange.getOut().setHeader(Exchange.HTTP_PATH,requestPath);

                         HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
                         String remoteAddr = req.getHeader(X_FORWARDED_FOR);
                         if (remoteAddr == null)
                         {
                             remoteAddr = req.getRemoteAddr();
                         }
                         else
                         {
                             // As of https://en.wikipedia.org/wiki/X-Forwarded-For
                             // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
                             // we only want the client
                             remoteAddr = new StringTokenizer(remoteAddr, ",").nextToken().trim();
                         }
                         int remotePort = req.getRemotePort();
                         exchange.getOut().setHeader("request-ip",remoteAddr+":"+remotePort);
                     }
                   }})
                .choice()
                    .when(header("service-version").isEqualTo("1"))
                    .log(simple("version-select:Decision version select: route to v1").getText())
                    .to("direct:dummy-v1")
                .otherwise()
                    .log(simple("version-select:Decision version select: default to v1").getText())
                    .to("direct:dummy-v1")
                .endChoice();
    }

    private void readProperties() throws IOException {
        try {
            Properties p = new Properties();
            FileInputStream file = new FileInputStream("/proxy.properties");
            p.load(file);
            if (p.get("host") != null) {
                host = (String) p.get("host");
            }
            if (p.get("port") != null) {
                port = (String) p.get("port");
            }
            if (p.get("context") != null) {
                context = (String) p.get("context");
            }
        }
        catch (FileNotFoundException fnfe)
        {
            log.warn("Unable to open /proxy.properties. Defaulting proxy settings");
        }
    }
}
