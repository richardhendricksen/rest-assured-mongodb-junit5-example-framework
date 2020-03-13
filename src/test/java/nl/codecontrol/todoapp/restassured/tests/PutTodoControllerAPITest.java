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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(APICallbacks.class)
class PutTodoControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_UpdatingNonExistingTodo_Then_ErrorIsReturned() {
        final String id = "5e668ff91f72294573382441";

        //@formatter:off
        given().
                contentType(ContentType.JSON).
                body("{\"id\":\""+id+"\",\"title\":\"NewTitle\",\"completed\":false,\"createdAt\":\"2020-03-13T15:48:39.508+0000\"}").
        when().
                put("/api/todos/{id}", id).
        then().
                statusCode(404).
                body(is(""));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(0));
    }

    @Test
    public void Given_SingleTodoInCollection_When_UpdatingTodo_Then_TodoIsUpdatedInDatabaseAndReturned() {
        final String id = "5e668ff91f72294573382441";

        insertTestdata("todos", "singleTodo");

        //@formatter:off
        given().
                contentType(ContentType.JSON).
                body("{\"id\":\""+id+"\",\"title\":\"NewTitle\",\"completed\":true,\"createdAt\":\"2020-03-13T15:48:39.508+0000\"}").
        when().
                put("/api/todos/{id}", id).
        then().
                statusCode(200).
                body("id", is(id)).
                body("title", is("NewTitle")).
                body("completed", is(true)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(1))
                .body("[0]._id", is(not(nullValue())))
                .body("[0].title", is("NewTitle"))
                .body("[0].completed", is(true))
                .body("[0].createdAt", is(not(nullValue())));
    }

    @Test
    public void Given_MultipleTodosInCollection_When_UpdatingTodo_Then_TodoIsUpdatedAndReturned() {

        final String id = "5e668ff91f72294573382442";

        insertTestdata("todos", "multipleTodos");

        //@formatter:off
        given().
                contentType(ContentType.JSON).
                body("{\"id\":\""+id+"\",\"title\":\"NewTitle\",\"completed\":true,\"createdAt\":\"2020-03-13T15:48:39.508+0000\"}").
        when().
                put("/api/todos/{id}", id).
        then().
                statusCode(200).
                body("id", is(id)).
                body("title", is("NewTitle")).
                body("completed", is(true)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(3))
                .body("[0]._id", is(not(nullValue())))
                .body("[0].title", is("Test1"))
                .body("[0].completed", is(false))
                .body("[0].createdAt", is(not(nullValue())))
                .body("[1]._id", is(not(nullValue())))
                .body("[1].title", is("NewTitle"))
                .body("[1].completed", is(true))
                .body("[1].createdAt", is(not(nullValue())))
                .body("[2]._id", is(not(nullValue())))
                .body("[2].title", is("Test3"))
                .body("[2].completed", is(true))
                .body("[2].createdAt", is(not(nullValue())));
    }
}
