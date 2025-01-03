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
		long rank = 1_000_000_000_000_000_000L;
		for (int i = 4; i >= 0; --i) {
			if (longValue >= rank) {
				String rankName = THOUSANDS_SCALE_PREFIXES[i] + "illion";
				String transcription = spellOutAsLong(longValue / rank) + " " + rankName;
				long remainder = longValue % rank;
				if (remainder != 0) {
					transcription += " " + spellOutAsLong(remainder);
				}
				return transcription;
			}
			rank /= 1000;
		}
		if (longValue >= 1000) {
			String transcription = spellOutAsLong(longValue / 1000) + " thousand";
			long remainder = longValue % 1000;
			if (remainder != 0) {
				transcription += " " + spellOutAsLong(remainder);
			}
			return transcription;
		}
		if (longValue >= 100) {
			String transcription = DIGITS_TEENS[(int) longValue / 100] + " hundred";
			long remainder = longValue % 100;
			if (remainder != 0) {
				transcription += " " + spellOutUnderOneHundred(remainder);
			}
			return transcription;
		}
		return spellOutUnderOneHundred(longValue);
	}

	/* Prerequisite: 0 <= longValue <= 99 */
	private static String spellOutUnderOneHundred(long longValue) {
		if (longValue >= 20) {
			String transcription = TENS_PREFIXES[(int) longValue / 10 - 2] + "ty";
			int remainder = (int) longValue % 10;
			if (remainder != 0) {
				transcription += "-" + DIGITS_TEENS[remainder];
			}
			return transcription;
		}
		return DIGITS_TEENS[(int) longValue];
	}

	private static String spellOutDecimalPart(double doubleValue) {
		return ""; // TODO
	}
}
