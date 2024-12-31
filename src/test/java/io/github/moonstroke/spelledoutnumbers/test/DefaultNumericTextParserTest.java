package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;

class DefaultNumericTextParserTest {
	static NumericTextParser parser;


	@BeforeAll
	static void setUp() {
		parser = NumericTextParser.getTextParserFor(Locale.getDefault());
	}

	@Test
	void testParserCannotParseNull() {
		assertThrows(NullPointerException.class, () -> parser.parse(null));
	}

	@Test
	void testParserCannotParseEmptyString() {
		assertThrows(NumberFormatException.class, () -> parser.parse(""));
	}

	@ParameterizedTest
	@MethodSource("validInputs")
	void testParserParsesValidValues(String input, double expected) {
		assertEquals(expected, parser.parse(input));
	}

	static Stream<Arguments> validInputs() {
		/* Explicit array allows trailing comma for cleaner diffs */
		return Stream.of(new Arguments[] {
				Arguments.of("not a number", Double.NaN),
				Arguments.of("infinity", Double.POSITIVE_INFINITY),
				Arguments.of("minus infinity", Double.NEGATIVE_INFINITY),
				Arguments.of("zero", 0.),
				Arguments.of("minus zero", -0.),
		});
	}
}
