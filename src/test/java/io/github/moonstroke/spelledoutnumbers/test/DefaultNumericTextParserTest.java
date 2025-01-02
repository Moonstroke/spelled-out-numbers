package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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
	@CsvFileSource(resources = {"/numbers.csv"}, numLinesToSkip = 1)
	void testParserParsesValidValues(double expected, String input) {
		assertEquals(expected, parser.parse(input));
	}

	@Test
	void testMinusZeroIsParsedAsMinusZero() {
		assertEquals(-0., parser.parse("minus zero"));
	}
}
