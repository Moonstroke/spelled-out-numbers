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
		return processWord(text);
	}

	private static double processWord(String word) throws NumberFormatException {
		if (DIGITS.containsKey(word)) {
			return DIGITS.get(word);
		}
		if (LOW_NUMBERS.containsKey(word)) {
			return LOW_NUMBERS.get(word);
		}
		if (word.endsWith("teen")) {
			String prefix = word.substring(0, word.length() - "teen".length());
			if (!TEEN_PREFIXES.containsKey(prefix)) {
				throw error(word);
			}
			return 10. + TEEN_PREFIXES.get(prefix);
		}
		if (word.endsWith("ty")) {
			String prefix = word.substring(0, word.length() - "ty".length());
			if (!TY_PREFIXES.containsKey(prefix)) {
				throw error(word);
			}
			return 10. * TY_PREFIXES.get(prefix);
		}
		int compositionIndex = word.indexOf("ty-");
		if (compositionIndex > 0) {
			String ten = word.substring(0, compositionIndex);
			String unit = word.substring(compositionIndex + "ty-".length());
			if (!TY_PREFIXES.containsKey(ten) || !DIGITS.containsKey(unit)) {
				throw error(word);
			}
			return 10. * TY_PREFIXES.get(ten) + DIGITS.get(unit);
		}
		throw error(word);
	}

	private static NumberFormatException error(String text) {
		return new NumberFormatException("Unrecognized transcription: " + text);
	}
}
