package net.atos.ol.dummy;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;


public class DummyTestCases {


    public void echoTestDefault(String cip) throws Exception {
        System.out.println("URL: "+cip + "echo/12345");

        given().
                when().
                get(cip + "echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }

    public void reverseTestDefault(String cip) throws Exception {

        System.out.println("URL: "+cip + "reverse/12345");

        given().
                when().
                get(cip + "reverse/12345").
                then().
                assertThat().body(containsString("54321"));
    }

    public void timeTestDefault(String cip) throws Exception {

        System.out.println("URL: "+cip + "time");

        given().
                when().
                get(cip + "time").
                then().
                assertThat().body(containsString(":"));
    }

    public void echoTestV1(String cip) throws Exception {
        System.out.println("URL: "+cip + "v1/echo/12345");

        given().
                when().
                get(cip + "v1/echo/12345").
                then().
                assertThat().body(containsString("12345"));
    }


    public void reverseTestV1(String cip) throws Exception {

        System.out.println(cip + "v1/reverse/12345");

        given().
                when().
                get(cip + "v1/reverse/12345").
                then().
                assertThat().body(containsString("54321"));
    }

    public void timeTestV1(String cip) throws Exception {

        System.out.println("URL: "+cip + "v1/time");

        given().
                when().
                get(cip + "v1/time").
                then().
                assertThat().body(containsString(":"));
    }
}