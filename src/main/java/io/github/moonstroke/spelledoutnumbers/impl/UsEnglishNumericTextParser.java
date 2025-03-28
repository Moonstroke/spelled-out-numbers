package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
	private static final List<String> DIGITS = List.of(
			"one",
			"two",
			"three",
			"four",
			"five",
			"six",
			"seven",
			"eight",
			"nine"
	);
	/* Transcriptions from numbers ten to twelve (do not fit other parts of the algorithm) */
	private static final List<String> LOW_NUMBERS = List.of(
			"ten",
			"eleven",
			"twelve"
	);
	/* Prefixes for -teen numbers */
	private static final List<String> TEEN_PREFIXES = List.of(
			"thir",
			"four",
			"fif",
			"six",
			"seven",
			"eigh",
			"nine"
	);
	/* Prefixes for -ty numbers */
	private static final List<String> TY_PREFIXES = List.of(
			"twen",
			"thir",
			"for",
			"fif",
			"six",
			"seven",
			"eigh",
			"nine"
		);

	private static final List<String> ZILLION_PREFIXES = List.of(
			"m",
			"b",
			"tr",
			"quadr",
			"quint",
			"sext",
			"sept",
			"oct",
			"non",
			"dec"
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
		Iterator<String> wordIterator = iterate(text);
		double previousWordValue = -1;
		while (wordIterator.hasNext()) {
			String word = wordIterator.next();
			if (word.equals("point")) {
				/* Decimal separator found; end of the integral part */
				if (!wordIterator.hasNext()) {
					/* Trailing decimal separator: not accepted */
					throw error(text);
				}
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
				/* Word group processed entirely. Reset value to be able to detect invalid transcriptions */
				previousWordValue = -1;
			} else if (previousWordValue >= 100) {
				/* The current word group is in the hundreds */
				previousWordValue += processWord(word);
			} else {
				if (previousWordValue >= 0) {
					/* There is an unfinished word group, but there shouldn't be */
					throw error(text);
				}
				previousWordValue = processWord(word);
			}
		}
		if (previousWordValue > 0) {
			parsedValue += previousWordValue;
		}
		if (wordIterator.hasNext()) {
			/* We stopped before the end: decimal separator found */
			parsedValue += parseDecimalPart(wordIterator);
		}
		return parsedValue;
	}

	/* Prerequisite: text != null; it can be empty, in which case a single empty word is yielded */
	private static Iterator<String> iterate(String text) {
		return new Iterator<String>() {
			private int currentWordIndex = 0;


			@Override
			public String next() {
				int nextWordIndex = text.indexOf(' ', currentWordIndex + 1);
				String word;
				if (nextWordIndex < 0) {
					word = text.substring(currentWordIndex);
					currentWordIndex = -1;
				} else {
					word = text.substring(currentWordIndex, nextWordIndex);
					currentWordIndex = nextWordIndex + 1;
				}
				return word;
			}

			@Override
			public boolean hasNext() {
				return currentWordIndex >= 0;
			}
		};
	}

	private static NumberFormatException error(String text) {
		return new NumberFormatException("Unrecognized transcription: " + text);
	}

	/* Prerequisite: rankName ends in illion */
	private static int parseThousandsRank(String rankName) {
		String prefix = removeSuffix(rankName, "illion");
		int rank = ZILLION_PREFIXES.indexOf(prefix);
		if (rank < 0) {
			throw error(rankName);
		}
		return rank + 2;
	}

	/* Prerequisite: word ends in suffix */
	private static String removeSuffix(String word, String suffix) {
		return word.substring(0, word.length() - suffix.length());
	}

	private static double processWord(String word) throws NumberFormatException {
		int digit = DIGITS.indexOf(word);
		if (digit >= 0) {
			return digit + 1;
		}
		digit = LOW_NUMBERS.indexOf(word);
		if (digit >= 0) {
			return digit + 10;
		}
		if (word.endsWith("teen")) {
			String prefix = removeSuffix(word, "teen");
			digit = TEEN_PREFIXES.indexOf(prefix);
			if (digit < 0) {
				throw error(word);
			}
			return digit + 13;
		}
		if (word.endsWith("ty")) {
			String prefix = removeSuffix(word, "ty");
			digit = TY_PREFIXES.indexOf(prefix);
			if (digit < 0) {
				throw error(word);
			}
			return 10. * (digit + 2);
		}
		int compositionIndex = word.indexOf("ty-");
		if (compositionIndex > 0) {
			String ten = word.substring(0, compositionIndex);
			String unit = word.substring(compositionIndex + "ty-".length());
			int tenDigit = TY_PREFIXES.indexOf(ten);
			int unitDigit = DIGITS.indexOf(unit);
			if (tenDigit < 0 || unitDigit < 0) {
				throw error(word);
			}
			return 10. * (tenDigit + 2) + unitDigit + 1;
		}
		throw error(word);
	}

	private static double parseDecimalPart(Iterator<String> wordIterator) {
		/* Accumulate all decimals as integrals to avoid rounding issues */
		double acc = 0;
		int decimalsCount = 0;
		while (wordIterator.hasNext()) {
			String word = wordIterator.next();
			++decimalsCount;
			int digit = DIGITS.indexOf(word);
			if (digit < 0) {
				throw error(word);
			}
			acc = 10 * acc + digit + 1;
		}
		/* Push everything down in the decimals */
		return acc / Math.pow(10, decimalsCount);
	}
}
