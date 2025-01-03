package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

/**
 * Number speller that supports the default JVM locale, usually {@link Locale#US en-US}.
 *
 * It spells numbers in all-lowercase text.
 */
public class DefaultNumberSpeller implements NumberSpeller {
	private static final String[] DIGITS_TEENS = {
		"zero",
		"one",
		"two",
		"three",
		"four",
		"five",
		"six",
		"seven",
		"eight",
	    "nine",
	    "ten",
	    "eleven",
	    "twelve",
	    "thirteen",
	    "fourteen",
	    "fifteen",
	    "sixteen",
	    "seventeen",
	    "eighteen",
	    "nineteen"
	};

	private static final String[] TENS_PREFIXES = {
		"twen",
		"thir",
		"for",
		"fif",
		"six",
		"seven",
		"eigh",
		"nine"
	};

	private static final String[] THOUSANDS_SCALE_PREFIXES = {
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
	};


	@Override
	public Locale getSupportedLocale() {
		return Locale.getDefault();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implNote The special IEEE-754 value negative zero is spelled out unsigned (simply {@code "zero"}).
	 */
	@Override
	public String spellOut(double doubleValue) {
		if (Double.isNaN(doubleValue)) {
			return "not a number";
		}
		if (doubleValue < 0) {
			return "minus " + spellOut(-doubleValue);
		}
		if (Double.isInfinite(doubleValue)) {
			return "infinity";
		}
		String transcription = spellOutIntegralPart(doubleValue);
		if (Math.floor(doubleValue) < doubleValue) {
			/* The value has a decimal part */
			transcription += " point" + spellOutDecimalPart(doubleValue);
		}
		return transcription;
	}

	private static String spellOutIntegralPart(double doubleValue) {
		if (doubleValue < 0x1p63) {
			/* Lower than Long.MAX_VALUE (not possibly equal to it, as it is not representable
			 * as a IEEE-754 double) => fits in a long. Process it as such */
			return spellOutAsLong((long) doubleValue);
		}
		return ""; // TODO
	}

	private static String spellOutAsLong(long longValue) {
		if (longValue < 20) {
			return DIGITS_TEENS[(int) longValue];
		}
		if (longValue < 100) {
			/* Structurally similar to constructTranscription but not quite fitting, so not refactored */
			String transcription = TENS_PREFIXES[(int) longValue / 10 - 2] + "ty";
			int remainder = (int) longValue % 10;
			if (remainder != 0) {
				transcription += "-" + DIGITS_TEENS[remainder];
			}
			return transcription;
		}
		if (longValue < 1000) {
			return constructTranscription(longValue, 100, "hundred");
		}
		if (longValue < 1_000_000) {
			return constructTranscription(longValue, 1000, "thousand");
		}
		/* From million to quintillion. Not above, because Long.MAX_VALUE ~~ 9 quintillion */
		for (int i = 0; i < 5; ++i) {
			long rank = (long) Math.pow(1000, i + 2);
			if (longValue / 1000 < rank) { /* Not longValue < rank * 1000 because of long overflow */
				return constructTranscription(longValue, rank, THOUSANDS_SCALE_PREFIXES[i] + "illion");
			}
		}
		return ""; // TODO
	}

	private static String constructTranscription(long longValue, long rank, String rankName) {
		String transcription = spellOutAsLong(longValue / rank) + " " + rankName;
		long remainder = longValue % rank;
		if (remainder != 0) {
			transcription += " " + spellOutAsLong(remainder);
		}
		return transcription;
	}

	private static String spellOutDecimalPart(double doubleValue) {
		return ""; // TODO
	}
}
