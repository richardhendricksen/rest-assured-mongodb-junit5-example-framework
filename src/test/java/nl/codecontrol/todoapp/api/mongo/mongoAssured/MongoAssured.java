package nl.codecontrol.todoapp.api.mongo.mongoAssured;

public class MongoAssured {

    public static MongoAssuredDatabase database(String database) {
        return new MongoAssuredDatabase(database);
    }
}
