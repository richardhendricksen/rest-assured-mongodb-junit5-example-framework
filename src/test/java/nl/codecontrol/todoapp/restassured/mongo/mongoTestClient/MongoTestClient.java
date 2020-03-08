package nl.codecontrol.todoapp.restassured.mongo.mongoTestClient;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoTestClient {

    private static final Logger LOG = LoggerFactory.getLogger(MongoTestClient.class);

    private MongoClient mongo;

    public MongoTestClient(Integer port) {
        mongo = new MongoClient("localhost", port);
    }

    public MongoTestClient(String host, Integer port) {
        LOG.info("Connecting to mongo on host:port " + host + port);
        mongo = new MongoClient(host, port);
    }

    public MongoTestCollection getTestCollection(String database, String collection) {
        return new MongoTestCollection(mongo, database, collection);
    }

    public void close() {
        mongo.close();
    }

    public MongoClient getMongo() {
        return mongo;
    }

}
