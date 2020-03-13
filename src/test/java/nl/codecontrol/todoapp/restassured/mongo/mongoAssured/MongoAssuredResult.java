package nl.codecontrol.todoapp.restassured.mongo.mongoAssured;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.bson.Document;
import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.util.List;
import java.util.stream.Collector;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;

public class MongoAssuredResult {

    private final String json;
    private final DocumentContext documentContext;

    MongoAssuredResult(List<Document> documents) {
        JSONArray jsonArray = documents.stream().map(document -> {
            try {
                return new JSONObject(document.toJson());
            } catch (JSONException e) {
                throw new IllegalStateException(e);
            }
        }).collect(Collector.of(
                JSONArray::new,
                JSONArray::put,
                JSONArray::put));

        this.json = jsonArray.toString();
        this.documentContext = JsonPath.parse(json);
    }

    public MongoAssuredResult body(String path, Matcher<?> matcher) {
        assertThat(documentContext, withJsonPath(path, matcher));
        return this;
    }

    public MongoAssuredResult body(final Matcher<? super String> matcher) {
        assertThat(json, matcher);
        return this;
    }
}
