package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;

/**
 * Text parser that reads numbers transcribed in {@link Locale#US US English}.
 *
 * @implSpec The parser only accepts all-lowercase text.
 */
public class UsEnglishNumericTextParser implements NumericTextParser {
	@Override
	public Locale getSupportedLocale() {
		return Locale.US;
	}


	private static final Map<String, Integer> DIGITS;
	private static final Map<String, Integer> PREFIXES;

	static {
		DIGITS = new HashMap<>();
		DIGITS.put("one", 1);
		DIGITS.put("two", 2);
		DIGITS.put("three", 3);
		DIGITS.put("four", 4);
		DIGITS.put("five", 5);
		DIGITS.put("six", 6);
		DIGITS.put("seven", 7);
		DIGITS.put("eight", 8);
		DIGITS.put("nine", 9);
		DIGITS.put("ten", 10);
		DIGITS.put("eleven", 11);
		DIGITS.put("twelve", 12);
		DIGITS.put("fourteen", 14);
		DIGITS.put("twenty", 20);
		DIGITS.put("forty", 40);

		PREFIXES = new HashMap<>();
		PREFIXES.put("thir", 3);
		PREFIXES.put("fif", 5);
		PREFIXES.put("six", 6);
		PREFIXES.put("seven", 7);
		PREFIXES.put("eigh", 8);
		PREFIXES.put("nine", 9);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @implNote The text {@code "minus zero"} is parsed as the special IEEE-754 value negative zero.
	 */
	@Override
	public double parse(String text) throws NumberFormatException {
		if (text.equals("not a number")) {
			return Double.NaN;
		}
		if (text.startsWith("minus ")) {
			return -parse(text.substring("minus ".length()));
		}
		if (text.equals("infinity")) {
			return Double.POSITIVE_INFINITY;
		}
		if (text.equals("zero")) {
			return 0;
		}
		if (DIGITS.containsKey(text)) {
			return DIGITS.get(text);
		}
		if (text.endsWith("teen")) {
			String prefix = text.substring(0, text.length() - "teen".length());
			if (PREFIXES.containsKey(prefix)) {
				return 10. + PREFIXES.get(prefix);
			}
		} else if (text.endsWith("ty")) {
			String prefix = text.substring(0, text.length() - "ty".length());
			if (PREFIXES.containsKey(prefix)) {
				return 10. * PREFIXES.get(prefix);
			}
		}
		throw new NumberFormatException("Unrecognized transcription: " + text);
	}
}
