package spoon.ast.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestTreeLevel {

    private static Stream<Arguments> provideEnumValues() {
        return Stream.of(
                Arguments.of("a", Optional.of(TreeLevel.AUTO)),
                Arguments.of("s", Optional.of(TreeLevel.STATEMENT)),
                Arguments.of("x", Optional.of(TreeLevel.EXPRESSION)),
                Arguments.of("e", Optional.of(TreeLevel.CLASS_ELEMENT)),
                Arguments.of("rAndoMValue", Optional.empty()),
                Arguments.of("AUTO", Optional.empty())
        );
    }

    @ParameterizedTest
    @MethodSource("provideEnumValues")
    void name(final String val, final Optional<TreeLevel> expectedOutput) {
        assertEquals(expectedOutput, TreeLevel.from(val));
    }
}