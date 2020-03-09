package nl.codecontrol.todoapp.restassured.mongo.mongoTestClient;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoTestCollection {

    private static final Map<String, List<Document>> TEST_DATA_CACHE = new HashMap<>();

    private MongoClient mongo;
    private String database;
    private String collection;

    private TestResourceFormat testResourceFormat = TestResourceFormat.JSON;

    public MongoTestCollection(MongoClient mongo, String database, String collection) {
        this.mongo = mongo;
        this.database = database;
        this.collection = collection;
    }

    public MongoCollection<Document> getCollection() {
        return mongo.getDatabase(database).getCollection(collection);
    }

    public void insertTestdata(String jsonData) {
        List<Document> documents = TEST_DATA_CACHE.computeIfAbsent(jsonData, key -> testResourceFormat.read(jsonData));
        getCollection().insertMany(documents);

    }

    public void drop() {
        getCollection().drop();
    }
}
