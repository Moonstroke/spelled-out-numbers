package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

class FranceFrenchNumberSpellerTest {
	static NumberSpeller speller;


	@BeforeAll
	static void setUp() {
		speller = NumberSpeller.getNumberSpellerFor(Locale.FRANCE);
	}

	@ParameterizedTest
	@CsvFileSource(resources = {"/nombres.csv"}, numLinesToSkip = 1, maxCharsPerColumn = 4500)
	void testSpellerSpellsValidInputs(double input, String expected) {
		assertEquals(expected, speller.spellOut(input));
	}

	@Test
	void testMinusZeroIsTranscribedUnsigned() {
		assertEquals("zéro", speller.spellOut(-0.));
	}
}
