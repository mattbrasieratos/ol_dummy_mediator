package net.atos.ol.dummy;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.Exchange;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


@ApplicationScoped
@ContextName("camel-cdi-context")
public class ProxyGetRouteBuilder
        extends RouteBuilder
{
    String host = "dummy-service";
    String port = "8080";
    String context = "/ol_dummy_service/dummy";

    public ProxyGetRouteBuilder() {}

    public void configure() throws Exception
    {
        readProperties();
        from("direct:dummy-v1")
                .routeId("dummy-mediation")
                .errorHandler(loggingErrorHandler("dummy-mediation").level(LoggingLevel.ERROR))
                .log(simple("v1:Received request for ${header.request-path} from ${header.request-ip}").getText())
                .setHeader(Exchange.HTTP_URI,
                            simple("http4://" + host + ":" + port + context + "${header.request-path}"))
                .to("http4://dummy:12345?throwExceptionOnFailure=false") //URI here is overridden using header above
                .log(simple("v1:Received response from http://" + host + ":" + port + context + "${header.request-path}").getText())
                .convertBodyTo(java.lang.String.class);


        from("direct:dummy-default")
                .log(simple("default:Received request for ${header.request-path} from request-ip").getText())
                .log(simple("default:Decision version select: route to v1").getText())
                .to("direct:dummy-v1");
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
