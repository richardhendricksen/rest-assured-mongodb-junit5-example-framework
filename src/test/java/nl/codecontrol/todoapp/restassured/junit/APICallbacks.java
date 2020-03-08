package nl.codecontrol.todoapp.restassured.junit;

import nl.codecontrol.todoapp.restassured.config.RestAssuredRequestSpecs;
import nl.codecontrol.todoapp.restassured.mongo.mongoTestClient.MongoTestClientSingleton;
import nl.codecontrol.todoapp.restassured.testdata.TestdataLoader;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class APICallbacks implements BeforeEachCallback, BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;
    private static boolean devMode;

    @Override
    public void beforeAll(final ExtensionContext context) {

        // only once
        if (!started) {
            started = true;
            RestAssuredRequestSpecs.setup();

            // Register callback hook for close
            context.getRoot().getStore(GLOBAL).put("APICallbacks", this);
        }
    }

    @Override
    public void beforeEach(final ExtensionContext context) {
        // Clean all transitory testdata before each test
        TestdataLoader.deleteTransitoryCollections();
    }

    @Override
    public void close() {
        MongoTestClientSingleton.getInstance().close();
    }
}
