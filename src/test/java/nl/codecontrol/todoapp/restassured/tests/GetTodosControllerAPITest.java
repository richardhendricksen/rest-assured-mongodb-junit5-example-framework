package nl.codecontrol.todoapp.restassured.tests;

import nl.codecontrol.todoapp.restassured.junit.APICallbacks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyList;
import static nl.codecontrol.todoapp.restassured.testdata.TestdataLoader.insertTestdata;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(APICallbacks.class)
class GetTodosControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_RetrievingTodos_Then_EmptyListIsReturned() {
        //@formatter:off
        given().
        when().
                get("/api/todos").
        then().
                statusCode(200).
                body("", is(emptyList()));
        //@formatter:on
    }

    @Test
    public void Given_SingleTodoInCollection_When_RetrievingTodos_Then_SingletonListIsReturned() {

        insertTestdata("todos", "singleTodo");

        //@formatter:off
        given().
        when().
                get("/api/todos").
        then().
                statusCode(200).
                body("$.size()", is(1)).
                body("[0].id", is(not(nullValue()))).
                body("[0].title", is("Test")).
                body("[0].completed", is(false)).
                body("[0].createdAt", is(not(nullValue())));
        //@formatter:on
    }

    @Test
    public void Given_MultipleTodosInCollection_When_RetrievingTodos_Then_ListIsReturned() {

        insertTestdata("todos", "multipleTodos");

        //@formatter:off
        given().
        when().
                get("/api/todos").
        then().
                statusCode(200).
                body("$.size()", is(3)).
                body("[0].id", is(not(nullValue()))).
                body("[0].title", is("Test1")).
                body("[0].completed", is(false)).
                body("[0].createdAt", is(not(nullValue()))).
                body("[1].id", is(not(nullValue()))).
                body("[1].title", is("Test2")).
                body("[1].completed", is(false)).
                body("[1].createdAt", is(not(nullValue()))).
                body("[2].id", is(not(nullValue()))).
                body("[2].title", is("Test3")).
                body("[2].completed", is(true)).
                body("[2].createdAt", is(not(nullValue())));
        //@formatter:on
    }

}
