package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.stream.Collectors;

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

	static Iterable<Arguments> validInputs() throws IOException {
		try (InputStream is = DefaultNumberSpellerTest.class.getResourceAsStream("/numbers.csv");
		     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			return br.lines()
			         .skip(1) /* Header line */
			         .map(line -> line.split(",", 2))
			         .map(fields -> Arguments.of(Double.parseDouble(fields[0]), fields[1]))
			         .collect(Collectors.toList());
		}
	}
}
