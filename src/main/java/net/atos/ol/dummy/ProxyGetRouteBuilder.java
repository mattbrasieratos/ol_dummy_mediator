package net.atos.ol.dummy;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.Exchange;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


@ApplicationScoped
@ContextName("camel-cdi-context")
public class ProxyGetRouteBuilder
        extends RouteBuilder
{
    String host = "localhost";
    String port = "8080";
    String context = "/ol_dummy_service/dummy";

    public ProxyGetRouteBuilder() {}

    public void configure() throws Exception
    {
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
            // do nothing, just use defaults
        }
        from("direct:proxy")
                .setHeader(Exchange.HTTP_URI,
                            simple("http4://" + host + ":" + port + context + "${header.request-path}"))
                .to("http4://localhost:80");
    }
}
