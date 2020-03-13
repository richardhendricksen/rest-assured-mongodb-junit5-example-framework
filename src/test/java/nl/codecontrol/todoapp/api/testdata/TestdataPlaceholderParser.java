package nl.codecontrol.todoapp.api.testdata;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public class TestdataPlaceholderParser {

    private Map<String, String> placeholders = new HashMap<>();

    public TestdataPlaceholderParser replaceWithRelativeDateTimeInEpoch(String placeholder, long amount, TemporalUnit time) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now().plus(amount, time);

        placeholders.put(placeholder, Long.toString(zonedDateTime.toInstant().toEpochMilli()));

        return this;
    }

    public TestdataPlaceholderParser replaceWithStartOfCurrentWeekInEpoch(String placeholder) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();

        int dayOfWeek = zonedDateTime.getDayOfWeek().getValue();
        final ZonedDateTime atCurrentStartOfWeek = zonedDateTime.plusDays(1L - dayOfWeek);

        placeholders.put(placeholder, Long.toString(atCurrentStartOfWeek.toInstant().toEpochMilli()));

        return this;
    }

    public String parse(String document) {
        for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            document = document.replaceAll(placeholder.getKey(), placeholder.getValue());
        }

        return document;
    }
}
