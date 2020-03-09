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


@SpringBootTest
@ExtendWith(APICallbacks.class)
class GetTodoControllerAPITest {

    @Test
    public void Given_EmptyTodoCollection_When_RetrievingTodos_Then_EmptyListIsReturned() {
        //@formatter:off
        given().
        when().
                get("/api/todos").
        then().
                body("$", is(emptyList())).
                statusCode(200);
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
                body("$.size()", is(1)).
                body("[0].id", is(not(nullValue()))).
                body("[0].title", is("Test")).
                body("[0].completed", is(false)).
                body("[0].createdAt", is(not(nullValue()))).
                statusCode(200);
        //@formatter:on
    }

}
