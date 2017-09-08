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

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MyTest {

    @Inject
    CamelContext camelContext;
    
    @Deployment
    public static WebArchive createDeployment() {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "camel-tests.war");
        archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addPackage(ProxyGetRouteBuilder.class.getPackage());
        return archive;
    }

    @Test
    public void testEcho() {
        ProducerTemplate producer = camelContext.createProducerTemplate();
        String result = producer.requestBodyAndHeader("direct:proxy",null, "request-path", "/echo/test", java.lang.String.class);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testReverse() {
        ProducerTemplate producer = camelContext.createProducerTemplate();
        String result = producer.requestBodyAndHeader("direct:proxy",null, "request-path", "/reverse/test", java.lang.String.class);
        Assert.assertEquals("tset", result);
    }
}
