package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

class DefaultNumberSpellerTest {
	static NumberSpeller speller;


	@BeforeAll
	static void setUp() {
		speller = NumberSpeller.getNumberSpellerFor(Locale.getDefault());
	}

	@ParameterizedTest
	@MethodSource("validInputs")
	void testSpellerSpellsValidInputs(double input, String expected) {
		assertEquals(expected, speller.spellOut(input));
	}

	static Stream<Arguments> validInputs() {
		/* Explicit array allows trailing comma for cleaner diffs */
		return Stream.of(new Arguments[] {
				Arguments.of(Double.NaN, "not a number"),
				Arguments.of(Double.POSITIVE_INFINITY, "infinity"),
				Arguments.of(Double.NEGATIVE_INFINITY, "minus infinity"),
				Arguments.of(0., "zero"),
				Arguments.of(-0., "zero"),
		});
	}
}
