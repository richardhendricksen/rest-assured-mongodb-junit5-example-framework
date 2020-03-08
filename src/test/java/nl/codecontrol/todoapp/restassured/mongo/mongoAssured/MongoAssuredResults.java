package nl.codecontrol.todoapp.restassured.mongo.mongoAssured;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.hamcrest.Matcher;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MongoAssuredResults {

    private final List<Document> documents;
    private final MongoAssuredCollection mongoAssuredCollection;

    MongoAssuredResults(List<Document> documents, MongoAssuredCollection mongoAssuredCollection) {
        this.documents = documents;
        this.mongoAssuredCollection = mongoAssuredCollection;
    }

    public MongoAssuredResults size(Matcher<Integer> matcher) {
        assertThat(documents.size(), matcher);

        return this;
    }

    public MongoAssuredResults isEmpty() {
        assertThat(documents.isEmpty(), equalTo(true));

        return this;
    }

    public MongoAssuredResult get(int nr) {
        return new MongoAssuredResult(documents.get(nr));
    }

    public MongoAssuredResult first() {
        return new MongoAssuredResult(documents.get(0));
    }

    public MongoAssuredResults forEach(String path, Matcher<?> matcher) {
        for (Document document :
                documents) {
            new MongoAssuredResult(document).body(path, matcher);
        }
        return this;
    }

    public MongoAssuredResults find(Bson filter) {
        return mongoAssuredCollection.find(filter);
    }


}
