package nl.codecontrol.todoapp.restassured.mongo.mongoAssured;

import com.jayway.jsonpath.JsonPath;
import org.bson.Document;
import org.hamcrest.Matcher;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MongoAssuredResult {

    private final Document document;

    MongoAssuredResult(Document document) {
        this.document = document;
    }

    public MongoAssuredResult body(String path, Matcher<?> matcher) {
        assertThat(JsonPath.parse(document.toJson()), withJsonPath(path, matcher));
        return this;
    }

    public MongoAssuredResult body(final Matcher<? super String> matcher) {
        assertThat(document.toJson(), matcher);
        return this;
    }

    public MongoAssuredResult isEmpty() {
        assertThat(document, equalTo(null));
        return this;
    }
}
