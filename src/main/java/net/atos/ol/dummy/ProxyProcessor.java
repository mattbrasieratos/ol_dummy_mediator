package net.atos.ol.dummy;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProxyProcessor implements Processor{

    private Map<String,Object> headers = new HashMap<String,Object>();

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void addHeader(String key,Object value) {
        this.headers.put(key,value);
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setPattern(ExchangePattern.InOut);
        Set<String> keys = headers.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext())
        {
            String key = it.next();
            exchange.getIn().setHeader(key,headers.get(key));

        }
    }
}
