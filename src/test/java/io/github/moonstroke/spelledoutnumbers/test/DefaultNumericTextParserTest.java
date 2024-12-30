package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;

class DefaultNumericTextParserTest {
	static NumericTextParser parser;


	@BeforeAll
	static void setUp() {
		parser = NumericTextParser.getTextParserFor(Locale.getDefault());
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
}
