package nl.codecontrol.todoapp.restassured.mongo.mongoTestClient;

public class MongoTestClientSingleton {

    private static MongoTestClient mongoTestClient;

    public static MongoTestClient getInstance() {
        if (mongoTestClient == null) {
            mongoTestClient = new MongoTestClient(57017);
        }

        return mongoTestClient;
    }

}
