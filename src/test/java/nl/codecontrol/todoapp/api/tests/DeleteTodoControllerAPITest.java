package nl.codecontrol.todoapp.api.tests;

import nl.codecontrol.todoapp.api.junit.APICallbacks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static nl.codecontrol.todoapp.api.mongo.mongoAssured.MongoAssured.database;
import static nl.codecontrol.todoapp.api.testdata.TestdataLoader.insertTestdata;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.emptyString;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(APICallbacks.class)
class DeleteTodoControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_DeletingNonExistingTodo_Then_ErrorIsReturned() {
        final String id = "5e668ff91f72294573382441";

        //@formatter:off
        given().
        when().
                delete("/api/todos/{id}", id).
        then().
                statusCode(404).
                body(is(emptyString()));
        //@formatter:on
    }

    @Test
    public void Given_SingleTodoInCollection_When_DeletingTodo_Then_CollectionIsEmpty() {
        final String id = "5e668ff91f72294573382441";

        insertTestdata("todos", "singleTodo");

        //@formatter:off
        given().
        when().
                delete("/api/todos/{id}", id).
        then().
                statusCode(200).
                body(is(emptyString()));
        //@formatter:on

        database("test")
                .collection("todos")
                .size(is(0));
    }
    @Test
    public void Given_MultipleTodosInCollection_When_DeletingTodo_Then_CollectionDoesNotContainTodo() {
        final String id = "5e668ff91f72294573382442";

        insertTestdata("todos", "multipleTodos");

        //@formatter:off
        given().
        when().
                delete("/api/todos/{id}", id).
        then().
                statusCode(200).
                body(is(emptyString()));
        //@formatter:on

        database("test")
                .collection("todos")
                .find()
                .body("size()", is(2))
                .body("[0]._id", is(not(nullValue())))
                .body("[0].title", is("Test1"))
                .body("[0].completed", is(false))
                .body("[0].createdAt", is(not(nullValue())))
                .body("[1]._id", is(not(nullValue())))
                .body("[1].title", is("Test3"))
                .body("[1].completed", is(true))
                .body("[1].createdAt", is(not(nullValue())));
    }

}
