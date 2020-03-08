package nl.codecontrol.todoapp.restassured.mongo.mongoTestClient;

import com.google.common.io.Resources;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MongoTestCollection {

    private static final Map<String, List<Document>> TEST_DATA_CACHE = new HashMap<>();

    private MongoClient mongo;
    private String database;
    private String collection;

    private TestResourceFormat testResourceFormat = TestResourceFormat.JSON_OBJECT_PER_LINE;

    public MongoTestCollection(MongoClient mongo, String database, String collection) {
        this.mongo = mongo;
        this.database = database;
        this.collection = collection;
    }

    public MongoCollection<Document> getCollection() {
        return mongo.getDatabase(database).getCollection(collection);
    }

    public void insertTestdata(String testResource) {
        List<Document> documents = TEST_DATA_CACHE.computeIfAbsent(testResource, key -> {
            try {
                String json = Resources.toString(new URL(testResource), UTF_8);
                return testResourceFormat.read(json);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        getCollection().insertMany(documents);

    }

    public void drop() {
        getCollection().drop();
    }
}
