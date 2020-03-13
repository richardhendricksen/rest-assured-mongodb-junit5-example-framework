package nl.codecontrol.todoapp.restassured.tests;

import io.restassured.http.ContentType;
import nl.codecontrol.todoapp.restassured.junit.APICallbacks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static nl.codecontrol.todoapp.restassured.mongo.mongoAssured.MongoAssured.database;
import static nl.codecontrol.todoapp.restassured.testdata.TestdataLoader.insertTestdata;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.emptyString;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(APICallbacks.class)
class PostTodoControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_AddingTodo_Then_DatabaseContainsOneTodo() {
        //@formatter:off
        given().
                contentType(ContentType.JSON).
                body("{\"title\": \"Test\"}").
        when().
                post("/api/todos").
        then().
                statusCode(200).
                body("id", is(not(nullValue()))).
                body("title", is("Test")).
                body("completed", is(false)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(1))
                .body("[0]._id", is(not(nullValue())))
                .body("[0].title", is("Test"))
                .body("[0].completed", is(false))
                .body("[0].createdAt", is(not(nullValue())));
    }

    @Test
    public void Given_NonTodoCollection_When_AddingTodo_Then_DatabaseContainsBothTodos() {
        insertTestdata("todos", "singleTodo");

        //@formatter:off
        given().
                contentType(ContentType.JSON).
                body("{\"title\": \"Test\"}").
        when().
                post("/api/todos").
        then().
                statusCode(200).
                body("id", is(not(nullValue()))).
                body("title", is("NewTest")).
                body("completed", is(false)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(2))
                .body("[0]._id", is(not(nullValue())))
                .body("[0].title", is("Test"))
                .body("[0].completed", is(false))
                .body("[0].createdAt", is(not(nullValue())))
                .body("[1]._id", is(not(nullValue())))
                .body("[1].title", is("NewTest"))
                .body("[1].completed", is(false))
                .body("[1].createdAt", is(not(nullValue())));
    }

}
