package io.github.moonstroke.spelledoutnumbers.impl;

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


	/* Transcriptions for numbers from one through nine */
	private static final Map<String, Integer> DIGITS = Map.of(
			"one", 1,
			"two", 2,
			"three", 3,
			"four", 4,
			"five", 5,
			"six", 6,
			"seven", 7,
			"eight", 8,
			"nine", 9
	);
	/* Transcriptions from numbers ten to twelve (do not fit other parts of the algorithm) */
	private static final Map<String, Integer> LOW_NUMBERS = Map.of(
			"ten", 10,
			"eleven", 11,
			"twelve", 12
	);
	/* Prefixes for -teen numbers */
	private static final Map<String, Integer> TEEN_PREFIXES = Map.of(
			"thir", 3,
			"four", 4,
			"fif", 5,
			"six", 6,
			"seven", 7,
			"eigh", 8,
			"nine", 9
	);
	/* Prefixes for -ty numbers */
	private static final Map<String, Integer> TY_PREFIXES = Map.of(
			"twen", 2,
			"thir", 3,
			"for", 4,
			"fif", 5,
			"six", 6,
			"seven", 7,
			"eigh", 8,
			"nine", 9
	);


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
		if (LOW_NUMBERS.containsKey(text)) {
			return LOW_NUMBERS.get(text);
		}
		if (text.endsWith("teen")) {
			String prefix = text.substring(0, text.length() - "teen".length());
			if (!TEEN_PREFIXES.containsKey(prefix)) {
				throw error(text);
			}
			return 10. + TEEN_PREFIXES.get(prefix);
		}
		if (text.endsWith("ty")) {
			String prefix = text.substring(0, text.length() - "ty".length());
			if (!TY_PREFIXES.containsKey(prefix)) {
				throw error(text);
			}
			return 10. * TY_PREFIXES.get(prefix);
		}
		int compositionIndex = text.indexOf("ty-");
		if (compositionIndex > 0) {
			String ten = text.substring(0, compositionIndex);
			String unit = text.substring(compositionIndex + "ty-".length());
			if (!TY_PREFIXES.containsKey(ten) || !DIGITS.containsKey(unit)) {
				throw error(text);
			}
			return 10. * TY_PREFIXES.get(ten) + DIGITS.get(unit);
		}
		throw error(text);
	}

	private static NumberFormatException error(String text) {
		return new NumberFormatException("Unrecognized transcription: " + text);
	}
}
