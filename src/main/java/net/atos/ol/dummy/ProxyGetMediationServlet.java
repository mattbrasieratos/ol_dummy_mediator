package net.atos.ol.dummy;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

@WebServlet(name = "HttpServiceServlet", urlPatterns = { "/*" }, loadOnStartup = 1)
public class ProxyGetMediationServlet extends HttpServlet {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String DEFAULT_ENDPOINT="direct:dummy-default";
    private static final String VERSION_ONE_ENDPOINT="direct:dummy-v1";
    private static final String VERSION_ONE="/v1";

    @Inject
    private CamelContext camelContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream out = res.getOutputStream();
        ProducerTemplate producer = camelContext.createProducerTemplate();
        //We only pass through headers we are interested in for the service, to prevent injection attacks
        //The path that was requested is passed into Camel as the header "request-path"
        String targetEndpoint=DEFAULT_ENDPOINT;
        String requestPath = req.getPathInfo();

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

        if (requestPath.startsWith(VERSION_ONE))
        {
            requestPath = requestPath.substring(requestPath.indexOf(VERSION_ONE)+VERSION_ONE.length(),requestPath.length());
            targetEndpoint=VERSION_ONE_ENDPOINT;
        }
        //Create a processor that will add the relevant information into the camel exchange
        ProxyProcessor processor = new ProxyProcessor();
        processor.addHeader("original-path", req.getPathInfo());
        processor.addHeader("request-path", requestPath);
        processor.addHeader("request-query",req.getQueryString());
        processor.addHeader("request-ip",remoteAddr);

        //Call the exchange, passing in the processor
        Exchange result = producer.request(targetEndpoint, processor);

        //set the response code
        res.setStatus((Integer)result.getOut().getHeader(Exchange.HTTP_RESPONSE_CODE));
        out.print((String)result.getOut().getBody());

    }
}
