package nl.codecontrol.todoapp.restassured.matchers;

import org.junit.jupiter.api.Test;

import static nl.codecontrol.todoapp.restassured.matchers.JsonMatcher.eqJson;
import static nl.codecontrol.todoapp.restassured.matchers.JsonMatcher.eqJsonResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

class JsonMatcherTest {

    private static final String EXPECTED_JSON = "{ \"property1\": \"value1\", " +
            "\"property2\": { \"subproperty2_1\": [\"value2_1\", \"value2_2\"] } }";

    @Test
    void whenJsonFullyEqual_thenExpectMatched() {
        assertThat(EXPECTED_JSON,
                eqJson(EXPECTED_JSON));
    }

    @Test
    void whenJsonFullyEqualToResourceFile_thenExpectMatched() {
        assertThat(EXPECTED_JSON,
                eqJsonResource("JsonMatcherTest.json"));
    }

    @Test
    void whenPropertyValueDifferent_thenExpectNotMatched() {
        assertThat("{ \"property1\": \"FOOBAR\", " +
                        "\"property2\": { \"subproperty2_1\": [\"value2_1\", \"value2_2\"] } }",
                not(eqJson(EXPECTED_JSON)));
    }

    @Test
    void whenMissingProperty_thenExpectNotMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": {} }",
                not(eqJson(EXPECTED_JSON)));
    }

    @Test
    void whenArrayElementOrderingDifferent_thenExpectNotMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": [\"value2_2\", \"value2_1\"] } }",
                not(eqJson(EXPECTED_JSON)
                        .withStrictArrayOrdering(true)));
    }

    @Test
    void whenArrayElementOrderingDifferentButNotStrictlyCompared_thenExpectMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": [\"value2_2\", \"value2_1\"] } }",
                eqJson(EXPECTED_JSON)
                        .withStrictArrayOrdering(false));
    }

    @Test
    void whenAdditionalProperty_thenExpectNotMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": [\"value2_1\", \"value2_2\"] }, " +
                        "\"property3\": \"value3\" }",
                not(eqJson(EXPECTED_JSON)
                        .withIgnoreAdditionalProperties(false)));
    }

    @Test
    void whenAdditionalIgnoredProperty_thenExpectMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": [\"value2_1\", \"value2_2\"] }, " +
                        "\"property3\": \"value3\" }",
                eqJson(EXPECTED_JSON)
                        .withIgnoreAdditionalProperties(true));
    }

    @Test
    void whenPropertyValueDifferentButIgnored_thenExpectMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": \"FOOBAR\" }",
                eqJson(EXPECTED_JSON)
                        .withIgnoreContentsOfPaths("property2"));
    }

    @Test
    void whenDeeperPathValueDifferentButIgnored_thenExpectMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": \"FOOBAR\" } }",
                eqJson(EXPECTED_JSON)
                        .withIgnoreContentsOfPaths("property2.subproperty2_1"));
    }

    @Test
    void whenWildcardPathValueDifferentButIgnored_thenExpectMatched() {
        assertThat("{ \"property1\": \"value1\", " +
                        "\"property2\": { \"subproperty2_1\": \"FOOBAR\" } }",
                eqJson(EXPECTED_JSON)
                        .withIgnoreContentsOfPaths("**.subproperty2_1"));
    }
}
