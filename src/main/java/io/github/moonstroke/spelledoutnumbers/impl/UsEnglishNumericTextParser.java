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

	private static final Map<String, Integer> ZILLION_PREFIXES = Map.of(
			"m", 2,
			"b", 3,
			"tr", 4,
			"quadr", 5,
			"quint", 6,
			"sext", 7,
			"sept", 8,
			"oct", 9,
			"non", 10,
			"dec", 11
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
		double parsedValue = 0;
		String[] words = text.split(" ");
		int i = 0;
		double previousWordValue = -1;
		for (; i < words.length; ++i) {
			String word = words[i];
			if (word.equals("point")) {
				/* Decimal separator found; end of the integral part */
				break;
			}
			if (word.equals("hundred")) {
				if (previousWordValue < 0 || previousWordValue >= 10) {
					throw error(text);
				}
				previousWordValue *= 100;
			} else if (word.equals("thousand")) {
				if (previousWordValue < 0) {
					throw error(text);
				}
				parsedValue += 1000 * previousWordValue;
				previousWordValue = -1;
			} else if (word.endsWith("illion")) {
				if (previousWordValue < 0) {
					throw error(text);
				}
				int thousandsRank = parseThousandsRank(word);
				parsedValue += Math.pow(1000, thousandsRank) * previousWordValue;
				previousWordValue = -1;
			} else {
				if (previousWordValue >= 0) {
					throw error(text);
				}
				previousWordValue = processWord(word);
			}
		}
		if (previousWordValue > 0) {
			parsedValue += previousWordValue;
		}
		if (i < words.length) {
			/* We stopped before the end: decimal separator found */
			if (i + 1 == words.length) {
				/* Trailing decimal separator: not accepted */
				throw error(text);
			}
			parsedValue += parseDecimalPart(words, i + 1);
		}
		return parsedValue;
	}

	private static NumberFormatException error(String text) {
		return new NumberFormatException("Unrecognized transcription: " + text);
	}

	/* Prerequisite: rankName ends in illion */
	private static int parseThousandsRank(String rankName) {
		String prefix = removeSuffix(rankName, "illion");
		if (ZILLION_PREFIXES.containsKey(prefix)) {
			return ZILLION_PREFIXES.get(prefix);
		}
		throw error(rankName);
	}

	/* Prerequisite: word ends in suffix */
	private static String removeSuffix(String word, String suffix) {
		return word.substring(0, word.length() - suffix.length());
	}

	private static double processWord(String word) throws NumberFormatException {
		if (DIGITS.containsKey(word)) {
			return DIGITS.get(word);
		}
		if (LOW_NUMBERS.containsKey(word)) {
			return LOW_NUMBERS.get(word);
		}
		if (word.endsWith("teen")) {
			String prefix = removeSuffix(word, "teen");
			if (!TEEN_PREFIXES.containsKey(prefix)) {
				throw error(word);
			}
			return 10. + TEEN_PREFIXES.get(prefix);
		}
		if (word.endsWith("ty")) {
			String prefix = removeSuffix(word, "ty");
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

	private static double parseDecimalPart(String[] words, int from) {
		/* Accumulate all decimals as integrals to avoid rounding issues */
		double acc = 0;
		for (int i = from; i < words.length; ++i) {
			acc = 10 * acc + DIGITS.get(words[i]);
		}
		/* Push everything down in the decimals */
		return acc / Math.pow(10, words.length - from);
	}
}
