package nl.codecontrol.todoapp.restassured.mongo.mongoAssured;

import com.mongodb.client.FindIterable;
import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClientSingleton;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.temporal.ChronoUnit.MICROS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MongoAssuredCollection {

    private final String database;
    private final String collection;

    MongoAssuredCollection(String database, String collection) {
        this.database = database;
        this.collection = collection;
    }

    public MongoAssuredResults find(Bson filter) {
        FindIterable<Document> result = MongoTestClientSingleton.getInstance().getMongo()
                .getDatabase(database)
                .getCollection(collection)
                .find(filter);

        List<Document> documents = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());

        return new MongoAssuredResults(documents, this);
    }


    public MongoAssuredResults find() {
        FindIterable<Document> result = MongoTestClientSingleton.getInstance().getMongo()
                .getDatabase(database)
                .getCollection(collection)
                .find();

        List<Document> documents = new ArrayList<>();

        for (Document document : result) {
            documents.add(document);
        }

        return new MongoAssuredResults(documents, this);
    }

    public MongoAssuredResult findFirst() {
        Document document = MongoTestClientSingleton.getInstance().getMongo()
                .getDatabase(database)
                .getCollection(collection)
                .find().first();

        return new MongoAssuredResult(document);
    }

    public MongoAssuredCollection size(Matcher<Integer> matcher) {
        long count = MongoTestClientSingleton.getInstance().getMongo()
                .getDatabase(database)
                .getCollection(collection)
                .count();
        assertThat(Math.toIntExact(count), matcher);

        return this;
    }

    public MongoAssuredCollection isEmpty() {
        this.size(equalTo(0));

        return this;
    }

    public MongoAssuredCollection waitUntilPopulated() {

        await().atMost(10, SECONDS).pollInterval(Duration.of(500, MICROS)).until(() ->
                MongoTestClientSingleton.getInstance().getMongo()
                        .getDatabase(database)
                        .getCollection(collection)
                        .count() != 0
        );

        return this;
    }

    public MongoAssuredCollection waitUntilPopulated(int size) {

        await().atMost(10, SECONDS).pollInterval(Duration.of(500, MICROS)).until(() ->
                MongoTestClientSingleton.getInstance().getMongo()
                        .getDatabase(database)
                        .getCollection(collection)
                        .count() == size
        );

        return this;
    }

    public MongoAssuredCollection waitUntilPopulated(Bson filter) {

        await().atMost(10, SECONDS).pollInterval(Duration.of(500, MICROS)).until(() -> {
            FindIterable<Document> result = MongoTestClientSingleton.getInstance().getMongo()
                    .getDatabase(database)
                    .getCollection(collection)
                    .find(filter);

            List<Document> documents = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());

            return documents.size() != 0;
        });

        return this;
    }
}
