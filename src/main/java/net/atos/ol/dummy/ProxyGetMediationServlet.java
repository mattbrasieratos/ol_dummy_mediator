/*
 * #%L
 * Wildfly Camel
 * %%
 * Copyright (C) 2013 - 2015 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.atos.ol.dummy;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

@WebServlet(name = "HttpServiceServlet", urlPatterns = { "/*" }, loadOnStartup = 1)
public class ProxyGetMediationServlet extends HttpServlet {

    private static String DEFAULT_ENDPOINT="direct:dummy-default";
    private static String VERSION_ONE_ENDPOINT="direct:dummy-v1";
    private static String VERSION_ONE="/v1";

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
        if (requestPath.startsWith(VERSION_ONE))
        {
            requestPath = requestPath.substring(requestPath.indexOf(VERSION_ONE)+VERSION_ONE.length(),requestPath.length());
            targetEndpoint=VERSION_ONE_ENDPOINT;
        }

        String result = producer.requestBodyAndHeader(targetEndpoint,null, "request-path", requestPath, java.lang.String.class);
        out.print(result);
    }
}
