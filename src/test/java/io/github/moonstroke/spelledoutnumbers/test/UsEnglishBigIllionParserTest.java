package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import io.github.moonstroke.spelledoutnumbers.impl.UsEnglishNumericTextParser;

class UsEnglishBigIllionParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = { "/illions-up-to-101.txt" })
	void testParserParsesValidValues(String rankName, int expectedRankValue) {
		assertEquals(expectedRankValue, UsEnglishNumericTextParser.parseBigRankName(rankName));
	}

	@ParameterizedTest
	@CsvFileSource(resources = {"/invalid-illions.txt"})
	void testParserFailsOnInvalidTranscriptions(String input) {
		assertThrows(NumberFormatException.class, () -> UsEnglishNumericTextParser.parseBigRankName(input));
	}
}
