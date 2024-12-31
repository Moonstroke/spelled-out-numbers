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
				Arguments.of("one", 1.),
				Arguments.of("two", 2.),
				Arguments.of("three", 3.),
				Arguments.of("four", 4.),
				Arguments.of("five", 5.),
				Arguments.of("six", 6.),
				Arguments.of("seven", 7.),
				Arguments.of("eight", 8.),
				Arguments.of("nine", 9.),
				Arguments.of("ten", 10.),
				Arguments.of("eleven", 11.),
				Arguments.of("twelve", 12.),
				Arguments.of("thirteen", 13.),
				Arguments.of("fourteen", 14.),
				Arguments.of("fifteen", 15.),
				Arguments.of("sixteen", 16.),
				Arguments.of("seventeen", 17.),
				Arguments.of("eighteen", 18.),
				Arguments.of("nineteen", 19.),
				Arguments.of("twenty", 20.),
				Arguments.of("twenty-one", 21.),
				Arguments.of("twenty-two", 22.),
				Arguments.of("twenty-three", 23.),
				Arguments.of("twenty-four", 24.),
				Arguments.of("twenty-five", 25.),
				Arguments.of("twenty-six", 26.),
				Arguments.of("twenty-seven", 27.),
				Arguments.of("twenty-eight", 28.),
				Arguments.of("twenty-nine", 29.),
				Arguments.of("thirty", 30.),
				Arguments.of("thirty-one", 31.),
				Arguments.of("thirty-two", 32.),
				Arguments.of("thirty-three", 33.),
				Arguments.of("thirty-four", 34.),
				Arguments.of("thirty-five", 35.),
				Arguments.of("thirty-six", 36.),
				Arguments.of("thirty-seven", 37.),
				Arguments.of("thirty-eight", 38.),
				Arguments.of("thirty-nine", 39.),
				Arguments.of("forty", 40.),
				Arguments.of("forty-one", 41.),
				Arguments.of("forty-two", 42.),
				Arguments.of("forty-three", 43.),
				Arguments.of("forty-four", 44.),
				Arguments.of("forty-five", 45.),
				Arguments.of("forty-six", 46.),
				Arguments.of("forty-seven", 47.),
				Arguments.of("forty-eight", 48.),
				Arguments.of("forty-nine", 49.),
				Arguments.of("fifty", 50.),
				Arguments.of("fifty-one", 51.),
				Arguments.of("fifty-two", 52.),
				Arguments.of("fifty-three", 53.),
				Arguments.of("fifty-four", 54.),
				Arguments.of("fifty-five", 55.),
				Arguments.of("fifty-six", 56.),
				Arguments.of("fifty-seven", 57.),
				Arguments.of("fifty-eight", 58.),
				Arguments.of("fifty-nine", 59.),
				Arguments.of("sixty", 60.),
				Arguments.of("sixty-one", 61.),
				Arguments.of("sixty-two", 62.),
				Arguments.of("sixty-three", 63.),
				Arguments.of("sixty-four", 64.),
				Arguments.of("sixty-five", 65.),
				Arguments.of("sixty-six", 66.),
				Arguments.of("sixty-seven", 67.),
				Arguments.of("sixty-eight", 68.),
				Arguments.of("sixty-nine", 69.),
				Arguments.of("seventy", 70.),
				Arguments.of("seventy-one", 71.),
				Arguments.of("seventy-two", 72.),
				Arguments.of("seventy-three", 73.),
				Arguments.of("seventy-four", 74.),
				Arguments.of("seventy-five", 75.),
				Arguments.of("seventy-six", 76.),
				Arguments.of("seventy-seven", 77.),
				Arguments.of("seventy-eight", 78.),
				Arguments.of("seventy-nine", 79.),
				Arguments.of("eighty", 80.),
				Arguments.of("eighty-one", 81.),
				Arguments.of("eighty-two", 82.),
				Arguments.of("eighty-three", 83.),
				Arguments.of("eighty-four", 84.),
				Arguments.of("eighty-five", 85.),
				Arguments.of("eighty-six", 86.),
				Arguments.of("eighty-seven", 87.),
				Arguments.of("eighty-eight", 88.),
				Arguments.of("eighty-nine", 89.),
				Arguments.of("ninety", 90.),
				Arguments.of("ninety-one", 91.),
				Arguments.of("ninety-two", 92.),
				Arguments.of("ninety-three", 93.),
				Arguments.of("ninety-four", 94.),
				Arguments.of("ninety-five", 95.),
				Arguments.of("ninety-six", 96.),
				Arguments.of("ninety-seven", 97.),
				Arguments.of("ninety-eight", 98.),
				Arguments.of("ninety-nine", 99.),
				Arguments.of("one hundred", 100.),
		});
	}
}
