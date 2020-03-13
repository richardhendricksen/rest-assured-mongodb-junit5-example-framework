package nl.codecontrol.todoapp.api.matchers;

import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class JsonMatcher extends TypeSafeMatcher<String> {

    private static final String EXPECTED_JSON_RESOURCE_FOLDER = "expected";

    private final String expected;

    @Nullable
    private final String expectedResourceName;

    private List<String> ignoreContentsOfPaths = emptyList();
    private boolean ignoreAdditionalProperties = false;
    private boolean strictArrayOrdering = true;

    private JSONCompareResult lastResult = null;

    private JsonMatcher(final String expected, @Nullable final String expectedResourceName) {
        this.expected = requireNonNull(expected);
        this.expectedResourceName = expectedResourceName;
    }

    /**
     * @param expectedJson The expected JSON string you want to compare against.
     */
    public static JsonMatcher eqJson(final String expectedJson) {
        return new JsonMatcher(expectedJson, null);
    }

    /**
     * @param expectedJsonResourceName The resource file name containing the JSON string you want to compare against.
     */
    public static JsonMatcher eqJsonResource(final String expectedJsonResourceName) {
        final Path resourcePath = Paths.get(EXPECTED_JSON_RESOURCE_FOLDER, expectedJsonResourceName);
        final URL resourceURL = JsonMatcher.class.getClassLoader().getResource(resourcePath.toString());
        final String expectedJson;
        try {
            expectedJson = String.join("", Resources.readLines(requireNonNull(resourceURL), UTF_8));
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("Cannot read expected JSON resource: " + resourcePath, e);
        }
        return new JsonMatcher(expectedJson, expectedJsonResourceName);
    }

    /**
     * Indicates that the actual contents of certain properties should not be compared to the expected contents of those
     * properties (but please note that the properties are still expected to be present). Example JSON path strings:
     * <ul>
     *     <li><code>timestamp</code> - matches only properties named "timestamp" on root level</li>
     *     <li><code>property1.*.timestamp</code> - matches only properties named "timestamp" that have "property1" as
     *     their grandparent, with exactly 1 parent in between with any name.</li>
     *     <li><code>list[*].text</code> - matches all entries "text" in array "list"</li>
     *     <li><code>**.timestamp</code> - matches properties named "timestamp" anywhere in the structure tree.</li>
     *     <li><code>**.property1.**.timestamp</code> - matches only properties named "timestamp" that have "property1" as
     *     one of their ancestors, anywhere in the structure tree.</li>
     *     <li><code>property1.**</code> - this is not supported, you must explicitly specify the name of the properties
     *     you want to match.</li>
     * </ul>
     * By default, no property paths are ignored at all.
     *
     * @param ignoreContentsOfPaths The JSON path strings pointing to the properties to treat this way.
     */
    public JsonMatcher withIgnoreContentsOfPaths(final String... ignoreContentsOfPaths) {
        this.ignoreContentsOfPaths = asList(ignoreContentsOfPaths);
        return this;
    }

    /**
     * Indicates that additional properties that appear in the actual JSON but not in the expected JSON resource should
     * be ignored. This is false by default so that added things let tests fail, so that you are forced to update the
     * expected JSON accordingly.
     *
     * @param ignoreAdditionalProperties True to ignore additional properties in the actual JSON, false (= default) if
     *                                   comparison should fail on any additional properties.
     */
    public JsonMatcher withIgnoreAdditionalProperties(final boolean ignoreAdditionalProperties) {
        this.ignoreAdditionalProperties = ignoreAdditionalProperties;
        return this;
    }

    /**
     * Indicates whether the ordering of elements in arrays matters. This is true by default, so that a different array
     * element ordering lets the test fail (because resources should provide deterministic array orderings). Please note
     * that this setting only affects the comparison of arrays, but not the ordering of properties (which should not
     * matter by definition in JSON).
     *
     * @param strictArrayOrdering True (= default) to only consider arrays equal if both their elements and their
     *                            ordering are equal, false if arrays are also considered equal if the ordering of their
     *                            elements does not match.
     */
    public JsonMatcher withStrictArrayOrdering(final boolean strictArrayOrdering) {
        this.strictArrayOrdering = strictArrayOrdering;
        return this;
    }

    @Override
    protected boolean matchesSafely(final String actual) {
        lastResult = null;
        if (actual == null) {
            lastResult = fail("Actual JSON string is null.");
            return false;
        }
        lastResult = compareJson(actual);
        return lastResult.passed();
    }

    @Override
    public void describeMismatchSafely(final String actual, final Description mismatchDescription) {
        if (lastResult != null && lastResult.failed()) {
            mismatchDescription.appendText(lastResult.getMessage());
        } else {
            mismatchDescription.appendText("was: ").appendText(actual);
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("JSON: ").appendText(expected);
    }

    private JSONCompareResult compareJson(final String actual) {
        // Set the compare mode according to given settings.
        final JSONCompareMode compareMode = STRICT
                .withExtensible(ignoreAdditionalProperties)
                .withStrictOrdering(strictArrayOrdering);

        // If there are paths to ignore, add a customization for each of them.
        final Customization[] customizations = ignoreContentsOfPaths.stream()
                .map(pathToIgnore -> customization(pathToIgnore, (actualValue, expectedValue) -> true))
                .toArray(Customization[]::new);

        final JSONComparator jsonComparator = new CustomComparator(compareMode, customizations);
        try {
            // If not equal, this result includes nicely formatted details about the specific JSON part that failed.
            final JSONCompareResult result = JSONCompare.compareJSON(expected, actual, jsonComparator);
            if (result.failed()) {
                return fail(buildComparisonFailedMessage(actual, result.getMessage()));
            }
            return result;
        } catch (final JSONException e) {
            return fail(buildComparisonFailedMessage(actual, e.getMessage()));
        }
    }

    private String buildComparisonFailedMessage(final String actualJson, @Nullable final String detailErrorMessage) {
        // If comparison fails, in addition to info about specific JSON parts we always want to see the complete JSON also.
        final StringBuilder messageBuilder = new StringBuilder();
        if (expectedResourceName != null) {
            messageBuilder.append("Expected JSON resource name: ").append(expectedResourceName).append('\n');
        }
        messageBuilder.append("Actual JSON: ").append(actualJson).append('\n');
        if (detailErrorMessage != null) {
            messageBuilder.append("Detail error message: ").append(detailErrorMessage);
        }
        return messageBuilder.toString();
    }

    private static JSONCompareResult fail(final String message) {
        final JSONCompareResult result = new JSONCompareResult();
        result.fail(message);
        return result;
    }
}
