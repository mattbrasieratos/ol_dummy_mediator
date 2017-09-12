package net.atos.ol.dummy;

import java.net.URL;

import org.arquillian.cube.DockerUrl;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.CubeIp;


import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Arquillian.class)
public class DummyTest {

    @HostIp
    private String ip;

    @CubeIp(containerName = "test")
    private String cip;

    @DockerUrl(containerName = "test", exposedPort = 8080)
    @ArquillianResource
    private URL url;

    @Test
    @RunAsClient
    @InSequence(10)
    public void echoTest() throws Exception {
        System.out.println("URL: "+"http://"+cip+":8080/" + "dummy-mediation/echo/12345");

        given().
                when().
                get("http://"+cip+":8080/" + "dummy-mediation/echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void reverseTest() throws Exception {

        System.out.println("URL: "+"http://"+cip+":8080/" + "dummy-mediation/reverse/12345");

        given().
                when().
                get("http://"+cip+":8080/" + "dummy-mediation/reverse/12345").
                then().
                assertThat().body(containsString("54321"));
    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void timeTest() throws Exception {

        System.out.println("URL: "+"http://"+cip+":8080/" + "dummy-mediation/time");

        given().
                when().
                get("http://"+cip+":8080/" + "dummy-mediation/time").
                then().
                assertThat().body(containsString(":"));
    }
}