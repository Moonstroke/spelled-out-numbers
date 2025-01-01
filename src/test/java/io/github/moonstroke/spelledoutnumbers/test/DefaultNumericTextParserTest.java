package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.stream.Collectors;

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

	static Iterable<Arguments> validInputs() throws IOException {
		try (InputStream is = DefaultNumberSpellerTest.class.getResourceAsStream("/numbers.csv");
		     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			return br.lines()
			         .skip(1) /* Header line */
			         .map(line -> line.split(",", 2))
			         .map(fields -> Arguments.of(fields[1], Double.parseDouble(fields[0])))
			         .collect(Collectors.toList());
		}
	}
}
