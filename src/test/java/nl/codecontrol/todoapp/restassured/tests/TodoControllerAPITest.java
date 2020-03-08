package nl.codecontrol.todoapp.restassured.tests;

import nl.codecontrol.todoapp.restassured.junit.APICallbacks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;


@SpringBootTest
@ExtendWith(APICallbacks.class)
class TodoControllerAPITest {

    @Test
    public void restAssuredTest() {
        //@formatter:off
        given().
        when().
                get("https://google.com").
        then().
                statusCode(200);
        //@formatter:on
    }

}
