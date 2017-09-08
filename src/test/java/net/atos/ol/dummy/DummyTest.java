package net.atos.ol.dummy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.InetAddress;

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
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class DummyTest {

    @HostIp
    private String ip;

    @DockerUrl(containerName = "test", exposedPort = 8080)
    @ArquillianResource
    private URL url;

    @CubeIp(containerName = "test")
    //@ArquillianResource
    private String hip;

    @Test
    @RunAsClient
    @InSequence(10)
    public void echoTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());
        System.out.println("HOST IP: "+ip);
        System.out.println("CUBE IP: "+ip);
        System.out.println("EXEC HOSTNAME: "+execWithResult("hostname"));
        System.out.println("JAVA HOSTNAME: "+InetAddress.getLocalHost().getHostName());
        System.out.println("EXEC2 HOSTNAME: "+execWithResult("ip a | grep docker0"));
        System.out.println("EXEC3 HOSTNAME: "+execWithResult("cat /etc/hosts | grep 172"));
        System.out.println("EXEC4 HOSTNAME: "+execWithResult("hostname -i"));
        System.out.println("EXEC5 HOSTNAME: "+execWithResult("ifconfig eth0 | grep \"inet addr:\" | cut -d \" \" -f 1"));
        System.out.println("EXEC6 HOSTNAME: "+execWithResult("tail -1 /etc/hosts"));
        System.out.println("EXEC7 HOSTNAME: "+execWithResult("/sbin/ip route | awk '/scope/ {print $9}'"));
        System.out.println("EXEC8 HOSTNAME: "+execWithResult("docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' dummy-mediation"));



        //System.out.println("test IP: "+InetAddress.getByName("test"));
        //System.out.println("dummy-mediator IP: "+InetAddress.getByName("dummy-mediator"));



        given().
                when().
                //get(url.toExternalForm() + "dummy-mediation/echo/12345").
                get("http://localhost:8080/" + "dummy-mediation/echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void reverseTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "dummy-mediation/reverse/12345").
                then().
                assertThat().body(containsString("54321"));
    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void timeTest() throws Exception {

        System.out.println("URL: "+url.toExternalForm());

        given().
                when().
                get(url.toExternalForm() + "dummy-mediation/time").
                then().
                assertThat().body(containsString(":"));
    }

    private String execWithResult(String s)
    {
        String result = null;

        try {

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(s);
            InputStream stdout = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdout);
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;

    }
}