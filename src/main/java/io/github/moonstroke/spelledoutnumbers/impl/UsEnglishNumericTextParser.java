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


	/* Atomic transcriptions (that cannot be deconstructed to fit other parts of the algorithm) */
	private static final Map<String, Integer> LOW_NUMBERS;
	/* Prefixes for -teen numbers */
	private static final Map<String, Integer> TEEN_PREFIXES;
	/* Prefixes for -ty numbers */
	private static final Map<String, Integer> TY_PREFIXES;

	static {
		LOW_NUMBERS = new HashMap<>();
		LOW_NUMBERS.put("one", 1);
		LOW_NUMBERS.put("two", 2);
		LOW_NUMBERS.put("three", 3);
		LOW_NUMBERS.put("four", 4);
		LOW_NUMBERS.put("five", 5);
		LOW_NUMBERS.put("six", 6);
		LOW_NUMBERS.put("seven", 7);
		LOW_NUMBERS.put("eight", 8);
		LOW_NUMBERS.put("nine", 9);
		LOW_NUMBERS.put("ten", 10);
		LOW_NUMBERS.put("eleven", 11);
		LOW_NUMBERS.put("twelve", 12);

		TEEN_PREFIXES = new HashMap<>();
		TEEN_PREFIXES.put("thir", 3);
		TEEN_PREFIXES.put("four", 4);
		TEEN_PREFIXES.put("fif", 5);
		TEEN_PREFIXES.put("six", 6);
		TEEN_PREFIXES.put("seven", 7);
		TEEN_PREFIXES.put("eigh", 8);
		TEEN_PREFIXES.put("nine", 9);

		TY_PREFIXES = new HashMap<>();
		TY_PREFIXES.put("twen", 2);
		TY_PREFIXES.put("thir", 3);
		TY_PREFIXES.put("for", 4);
		TY_PREFIXES.put("fif", 5);
		TY_PREFIXES.put("six", 6);
		TY_PREFIXES.put("seven", 7);
		TY_PREFIXES.put("eigh", 8);
		TY_PREFIXES.put("nine", 9);
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
		if (LOW_NUMBERS.containsKey(text)) {
			return LOW_NUMBERS.get(text);
		}
		if (text.endsWith("teen")) {
			String prefix = text.substring(0, text.length() - "teen".length());
			if (TEEN_PREFIXES.containsKey(prefix)) {
				return 10. + TEEN_PREFIXES.get(prefix);
			}
		} else if (text.endsWith("ty")) {
			String prefix = text.substring(0, text.length() - "ty".length());
			if (TY_PREFIXES.containsKey(prefix)) {
				return 10. * TY_PREFIXES.get(prefix);
			}
		}
		int compositionIndex = text.indexOf("ty-");
		if (compositionIndex > 0) {
			String ten = text.substring(0, compositionIndex);
			String unit = text.substring(compositionIndex + "ty-".length());
			if (TY_PREFIXES.containsKey(ten) && LOW_NUMBERS.containsKey(unit)) {
				int digit = LOW_NUMBERS.get(unit);
				if (digit < 10) {
					return 10. * TY_PREFIXES.get(ten) + digit;
				}
			}
		}
		throw new NumberFormatException("Unrecognized transcription: " + text);
	}
}
