package nl.codecontrol.todoapp.restassured.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;

public class SingletonListMatcher {

    public static <E> Matcher<Iterable<? extends Iterable<? extends E>>> matchesListOfSingletons(E... items) {
        return contains(Stream.of(items).map(Matchers::contains).collect(toList()));
    }
}
