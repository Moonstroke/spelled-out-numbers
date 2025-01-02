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
		if (Math.rint(doubleValue) < doubleValue) {
			/* The value has a decimal part */
			transcription += " point" + spellOutDecimalPart(doubleValue);
		}
		return transcription;
	}

	private static String spellOutIntegralPart(double doubleValue) {
		if (doubleValue < Long.MAX_VALUE) {
			/* It fits in a long integer. Process it as such */
			return spellOutAsLong((long) doubleValue);
		}
		return ""; // TODO
	}

	private static String spellOutAsLong(long longValue) {
		if (longValue < 20) {
			return DIGITS_TEENS[(int) longValue];
		}
		if (longValue < 100) {
			String transcription = TENS_PREFIXES[(int) longValue / 10 - 2] + "ty";
			int remainder = (int) longValue % 10;
			if (remainder != 0) {
				transcription += "-" + DIGITS_TEENS[remainder];
			}
			return transcription;
		}
		if (longValue < 1000) {
			String transcription = DIGITS_TEENS[(int) longValue / 100] + " hundred";
			int remainder = (int) longValue % 100;
			if (remainder != 0) {
				transcription += " " + spellOutAsLong(remainder);
			}
			return transcription;
		}
		return ""; // TODO
	}

	private static String spellOutDecimalPart(double doubleValue) {
		return ""; // TODO
	}
}
