package nl.codecontrol.todoapp.restassured.config;

import io.restassured.RestAssured;
import io.restassured.config.MatcherConfig;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.config.MatcherConfig.ErrorDescriptionType.HAMCREST;

public class RestAssuredRequestSpecs {

    private static final String BASE_URI = "http://localhost:8081";

    public static void setup() {

        RestAssured.baseURI = BASE_URI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config().matcherConfig(new MatcherConfig(HAMCREST));
    }
}
