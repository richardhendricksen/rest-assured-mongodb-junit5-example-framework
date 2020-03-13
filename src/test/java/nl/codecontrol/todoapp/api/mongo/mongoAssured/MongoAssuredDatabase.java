package nl.codecontrol.todoapp.api.mongo.mongoAssured;

public class MongoAssuredDatabase {

    private final String database;

    MongoAssuredDatabase(String database) {
        this.database = database;
    }

    public MongoAssuredCollection collection(String collection) {
        return new MongoAssuredCollection(database, collection);
    }
}
