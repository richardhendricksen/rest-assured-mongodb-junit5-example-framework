package nl.codecontrol.todoapp.restassured.mongo.mongoTestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public enum TestResourceFormat {

    /**
     * A valid JSON resource, can either be a single JSON object or a JSON array
     */
    JSON(json -> {
        JsonNode jsonNode = readJsonTree(json);
        if (jsonNode.isArray()) {
            return stream(jsonNode.spliterator(), false)
                    .map(JsonNode::toString)
                    .map(Document::parse)
                    .collect(toList());
        } else if (jsonNode.isObject()) {
            return singletonList(Document.parse(json));
        } else {
            throw new IllegalArgumentException("Unsupported JSON type: " + jsonNode.getNodeType());
        }
    }),

    /**
     * Every line in the JSON resource contains a single JSON object.
     * This is the default format used by mongoimport and mongoexport.
     */
    JSON_OBJECT_PER_LINE(json -> Arrays.stream(json.split("\n")).map(Document::parse).collect(toList()));

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Function<String, List<Document>> readFunction;

    TestResourceFormat(Function<String, List<Document>> readFunction) {
        this.readFunction = readFunction;
    }

    public List<Document> read(String json) {
        return readFunction.apply(json);
    }

    private static JsonNode readJsonTree(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
