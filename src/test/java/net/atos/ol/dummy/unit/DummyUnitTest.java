package net.atos.ol.dummy.unit;

import net.atos.ol.dummy.DummyTestCases;
import net.atos.ol.dummy.ProxyGetRouteBuilder;
import org.arquillian.cube.CubeIp;
import org.arquillian.cube.DockerUrl;
import org.arquillian.cube.HostIp;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.net.URL;
import javax.inject.Inject;
import javax.ws.rs.core.Application;

@RunWith(Arquillian.class)
public class DummyUnitTest extends DummyTestCases{

    @Deployment
    public static Archive<?> createDeployment() {
        Archive war = ShrinkWrap.create(ZipImporter.class, "dummy-mediation.war")
                .importFrom(new File("target/dummy-mediation.war")).as(WebArchive.class)
                .addClasses(DummyUnitTest.class,DummyTestCases.class);
        System.out.println(war.toString(true));
        return war;
    }

    @CubeIp(containerName = "unit-test")
    private String cip;

    @ArquillianResource
    private URL url;

    @Test
    @RunAsClient
    @InSequence(10)
    public void echoTestDefault() throws Exception {
        //super.echoTestDefault(fixUrl(url));
        super.echoTestDefault("http://"+cip+":1080/");

    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void reverseTestDefault() throws Exception {
        //super.reverseTestDefault(fixUrl(url));
        super.reverseTestDefault("http://"+cip+":1080/");
    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void timeTestDefault() throws Exception {
        //super.timeTestDefault(fixUrl(url));
        super.timeTestDefault("http://"+cip+":1080/");

    }
    @Test
    @RunAsClient
    @InSequence(40)
    public void echoTestV1() throws Exception {
        //super.echoTestV1(fixUrl(url));
        super.echoTestV1("http://"+cip+":1080/");

    }

    @Test
    @RunAsClient
    @InSequence(50)
    public void reverseTestV1() throws Exception {
        //super.reverseTestV1(fixUrl(url));

        super.reverseTestV1("http://"+cip+":1080/");
    }
    @Test
    @RunAsClient
    @InSequence(60)
    public void timeTestV1() throws Exception {
        //super.timeTestV1(fixUrl(url));
        super.timeTestV1("http://"+cip+":1080/");

    }

    @Test
    @InSequence(999)
    public void dummy_to_capture_stats() {

        Assert.assertTrue(true);
    }

}