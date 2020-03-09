package nl.codecontrol.todoapp.restassured.testdata;

import com.google.common.io.Resources;
import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClient;
import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClientSingleton;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestdataLoader {

    private static final String[] CLEAR_BETWEEN_TESTS_COLLECTIONS = {};
    private static final String DATABASE_NAME = "todo";
    private static final String TESTDATA_RESOURCES_ROOT = "testdata/";

    private static MongoTestClient mongoTestClient = MongoTestClientSingleton.getInstance();

    public static void insertTestdata(final String collection, final String scenario) {
        final String documentJson = readTestResource(TESTDATA_RESOURCES_ROOT + collection + "/" + scenario + ".json");
        mongoTestClient.getTestCollection(DATABASE_NAME, collection)
                .insertTestdata(documentJson);
    }

    public static void insertTestdata(final String collection, final String scenario, final TestdataPlaceholderParser testdataPlaceholderParser) {
        String documentJson = readTestResource(TESTDATA_RESOURCES_ROOT + collection + "/" + scenario + ".json");
        documentJson = testdataPlaceholderParser.parse(documentJson);
        mongoTestClient.getTestCollection(DATABASE_NAME, collection)
                .insertTestdata(documentJson);
    }

    public static void deleteTransitoryCollections() {
        for (final String collection : CLEAR_BETWEEN_TESTS_COLLECTIONS) {
            mongoTestClient.getTestCollection(DATABASE_NAME, collection).drop();
        }
    }

    private static String readTestResource(String testResource) {
        try {
            URL resourceUrl = Resources.getResource(testResource);
            return Resources.toString(resourceUrl, UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
