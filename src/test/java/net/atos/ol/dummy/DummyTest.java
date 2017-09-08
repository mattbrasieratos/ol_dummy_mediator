package net.atos.ol.dummy;

import java.net.URL;

import org.arquillian.cube.DockerUrl;
import org.arquillian.cube.HostIp;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class DummyTest {

    @HostIp
    private String ip;

    @DockerUrl(containerName = "test", exposedPort = 8080)
    @ArquillianResource
    private URL url;

    @Test
    @InSequence(1)
    public void basicTest() {
        assertThat(true, is(true));
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void echoTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "/dummy_mediation/echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }

    @Test
    @RunAsClient
    @InSequence(3)
    public void reverseTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "/dummy_mediation/reverse/12345").
                then().
                assertThat().body(containsString("54321"));
    }
    @Test
    @RunAsClient
    @InSequence(4)
    public void timeTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "/dummy_mediation/time").
                then().
                assertThat().body(containsString("12345"));
    }
    @Test
    @RunAsClient
    @InSequence(2)
    public void heartbeatTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "/dummy_mediation/echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }
}