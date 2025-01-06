package io.github.moonstroke.spelledoutnumbers.impl;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

/**
 * Number speller that transcribes numbers in {@link Locale#US US English}.
 *
 * @implSpec Numbers are transcribed in lowercase text.
 */
public class UsEnglishNumberSpeller implements NumberSpeller {
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
		return Locale.US;
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
		StringBuilder transcriber = new StringBuilder();
		if (doubleValue < 0) {
			transcriber.append("minus ");
			doubleValue = -doubleValue;
		}
		if (Double.isInfinite(doubleValue)) {
			transcriber.append("infinity");
		} else { /* doubleValue is a finite number */
			spellOutIntegralPart(doubleValue, transcriber);
			if (Math.floor(doubleValue) < doubleValue) {
				/* The value has a decimal part */
				transcriber.append(" point");
				spellOutDecimalPart(doubleValue, transcriber);
			}
		}
		return transcriber.toString();
	}

	private static void spellOutIntegralPart(double doubleValue, StringBuilder transcriber) {
		if (doubleValue < 0x1p63) {
			/* Lower than Long.MAX_VALUE (not possibly equal to it, as it is not representable
			 * as a IEEE-754 double) => fits in a long. Process it as such */
			spellOutAsLong((long) doubleValue, 1_000_000_000_000_000_000L, 4, transcriber);
			return;
		}
		// TODO
	}

	private static void spellOutAsLong(long longValue, long initialRank, int initialRankIndex, StringBuilder transcriber) {
		long rank = initialRank;
		for (int i = initialRankIndex; i >= 0; --i) {
			if (longValue >= rank) {
				String rankName = THOUSANDS_SCALE_PREFIXES[i] + "illion";
				spellOutThousandGroup(longValue / rank, transcriber);
				transcriber.append(' ').append(rankName);
				long remainder = longValue % rank;
				if (remainder != 0) {
					transcriber.append(' ');
					spellOutAsLong(remainder, rank / 1000, i - 1, transcriber);
				}
				return;
			}
			rank /= 1000;
		}
		if (longValue >= 1000) {
			spellOutThousandGroup(longValue / 1000, transcriber);
			transcriber.append(" thousand");
			long remainder = longValue % 1000;
			if (remainder != 0) {
				transcriber.append(' ');
				spellOutThousandGroup(remainder, transcriber);
			}
			return;
		}
		spellOutThousandGroup(longValue, transcriber);
	}

	/* Prerequisite: 0 <= longValue <= 999 */
	private static void spellOutThousandGroup(long longValue, StringBuilder transcriber) {
		if (longValue >= 100) {
			transcriber.append(DIGITS_TEENS[(int) longValue / 100]).append(" hundred");
			long remainder = longValue % 100;
			if (remainder != 0) {
				transcriber.append(' ');
				spellOutUnderOneHundred(remainder, transcriber);
			}
			return;
		}
		spellOutUnderOneHundred(longValue, transcriber);
	}

	/* Prerequisite: 0 <= longValue <= 99 */
	private static void spellOutUnderOneHundred(long longValue, StringBuilder transcriber) {
		if (longValue >= 20) {
			transcriber.append(TENS_PREFIXES[(int) longValue / 10 - 2]).append("ty");
			int remainder = (int) longValue % 10;
			if (remainder != 0) {
				transcriber.append('-').append(DIGITS_TEENS[remainder]);
			}
			return;
		}
		transcriber.append(DIGITS_TEENS[(int) longValue]);
	}

	/* Prerequisite: doubleValue has a decimal part (not integral) */
	private static void spellOutDecimalPart(double doubleValue, StringBuilder transcriber) {
		/* Hack: format the value to a numeric string and parse the digits
		 * following the decimal separator */
		NumberFormat fmt = NumberFormat.getInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumFractionDigits(Integer.MAX_VALUE);

		StringBuffer buffer = new StringBuffer();
		/* Request boundaries of the decimal (fraction) part of the number */
		FieldPosition pos = new FieldPosition(NumberFormat.FRACTION_FIELD);

		fmt.format(doubleValue, buffer, pos);
		for (int i = pos.getBeginIndex(); i < pos.getEndIndex(); ++i) {
			char digit = buffer.charAt(i);
			transcriber.append(' ').append(DIGITS_TEENS[digit - '0']);
		}
		// TODO
	}
}
