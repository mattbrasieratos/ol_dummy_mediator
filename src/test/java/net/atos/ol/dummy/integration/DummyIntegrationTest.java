package net.atos.ol.dummy.integration;

import net.atos.ol.dummy.DummyTestCases;

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


@RunWith(Arquillian.class)
public class DummyIntegrationTest extends DummyTestCases{

    @CubeIp(containerName = "test")
    private String cip;


    @Test
    @RunAsClient
    @InSequence(10)
    public void echoTestDefault() throws Exception {
        super.echoTestDefault("http://"+cip+":1080/");
    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void reverseTestDefault() throws Exception {
        super.reverseTestDefault("http://"+cip+":1080/");

    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void timeTestDefault() throws Exception {
        super.timeTestDefault("http://"+cip+":1080/");

    }
    @Test
    @RunAsClient
    @InSequence(40)
    public void echoTestV1() throws Exception {
        super.echoTestV1("http://"+cip+":1080/");

    }

    @Test
    @RunAsClient
    @InSequence(50)
    public void reverseTestV1() throws Exception {
        super.reverseTestV1("http://"+cip+":1080/");
    }
    @Test
    @RunAsClient
    @InSequence(60)
    public void timeTestV1() throws Exception {
        super.timeTestV1("http://"+cip+":1080/");

    }
}