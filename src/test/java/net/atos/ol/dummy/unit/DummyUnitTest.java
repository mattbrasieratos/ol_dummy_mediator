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
        // This lets us use RestAssured for in-container testing
        //File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
        //        .resolve("io.rest-assured:rest-assured")
        //        .withTransitivity().asFile();
        Archive war = ShrinkWrap.create(ZipImporter.class, "dummy-mediation.war")
                .importFrom(new File("target/dummy-mediation.war")).as(WebArchive.class)
                .addClasses(DummyUnitTest.class,DummyTestCases.class);
        System.out.println(war.toString(true));
        return war;
    }


    @ArquillianResource
    private URL url;

    @Test
    @RunAsClient
    @InSequence(10)
    public void echoTestDefault() throws Exception {
        super.echoTestDefault(fixUrl(url));

    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void reverseTestDefault() throws Exception {

        super.reverseTestDefault(fixUrl(url));
    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void timeTestDefault() throws Exception {

        super.timeTestDefault(fixUrl(url));

    }
    @Test
    @RunAsClient
    @InSequence(40)
    public void echoTestV1() throws Exception {

        super.echoTestV1(fixUrl(url));

    }

    @Test
    @RunAsClient
    @InSequence(50)
    public void reverseTestV1() throws Exception {

        super.reverseTestV1(fixUrl(url));
    }
    @Test
    @RunAsClient
    @InSequence(60)
    public void timeTestV1() throws Exception {

        super.timeTestV1(fixUrl(url));
    }

    @Test
    @InSequence(999)
    public void dummy_to_capture_stats() {
        Assert.assertTrue(true);
    }

    private String fixUrl(URL url)
    {
        String urlAsString = url.toExternalForm();
        System.out.println("URL as string: "+urlAsString);
        String hostname_1=urlAsString.substring(urlAsString.indexOf("http://")+"http://".length(),urlAsString.length());
        System.out.println("Intermediate string: "+hostname_1);
        String hostname = hostname_1.substring(0,hostname_1.indexOf(":8080"));
        String testUrl="http://"+hostname+":1080/";
        System.out.println("TEST URL: "+testUrl);
        return testUrl;
    }
}