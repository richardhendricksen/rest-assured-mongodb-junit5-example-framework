package nl.codecontrol.todoapp.restassured.testdata;

import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClient;
import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClientSingleton;

public class TestdataLoader {

    private static final String[] CLEAR_BETWEEN_TESTS_COLLECTIONS = {};
    private static final String DATABASE_NAME = "todo";
    private static final String TESTDATA_RESOURCES_ROOT = "testdata/";

    private static MongoTestClient mongoTestClient = MongoTestClientSingleton.getInstance();

    public static void insertTestdata(final String collection, final String scenario) {
        mongoTestClient.getTestCollection(DATABASE_NAME, collection)
                .insertTestdata(TESTDATA_RESOURCES_ROOT + collection + "/" + scenario + ".json");
    }

    public static void deleteTransitoryCollections() {
        for (final String collection : CLEAR_BETWEEN_TESTS_COLLECTIONS) {
            mongoTestClient.getTestCollection(DATABASE_NAME, collection).drop();
        }
    }
}
