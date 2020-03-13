package nl.codecontrol.todoapp.api.tests;

import nl.codecontrol.todoapp.api.junit.APICallbacks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static nl.codecontrol.todoapp.api.testdata.TestdataLoader.insertTestdata;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(APICallbacks.class)
class GetTodoControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_RetrievingNonExistingTodo_Then_ErrorIsReturned() {
        final String id = "5e668ff91f72294573382441";

        //@formatter:off
        given().
        when().
                get("/api/todos/{id}", id).
        then().
                statusCode(404).
                body(is(""));
        //@formatter:on
    }

    @Test
    public void Given_SingleTodoInCollection_When_RetrievingTodo_Then_TodoIsReturned() {
        final String id = "5e668ff91f72294573382441";

        insertTestdata("todos", "singleTodo");

        //@formatter:off
        given().
        when().
                get("/api/todos/{id}", id).
        then().
                statusCode(200).
                body("id", is(id)).
                body("title", is("Test")).
                body("completed", is(false)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on
    }

    @Test
    public void Given_MultipleTodosInCollection_When_RetrievingTodo_Then_TodoIsReturned() {

        final String id = "5e668ff91f72294573382442";

        insertTestdata("todos", "multipleTodos");

        //@formatter:off
        given().
        when().
                get("/api/todos/{id}", id).
        then().
                statusCode(200).
                body("id", is(id)).
                body("title", is("Test2")).
                body("completed", is(false)).
                body("createdAt", is(not(nullValue())));
        //@formatter:on
    }
}
