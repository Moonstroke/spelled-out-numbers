package io.github.moonstroke.spelledoutnumbers.impl;

import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
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

	private static final String[] ZILLIONS_UNITS_PREFIXES = {
		"un",
		"duo",
		"tre",
		"quattuor",
		"quin",
		"se",
		"septe",
		"octo",
		"nove",
	};

	private static final String[] ZILLIONS_TENS_PREFIXES = {
		"dec",
		"vigint",
		"trigint",
		"quadragint",
		"quinquagint",
		"sexagint",
		"septuagint",
		"octogint",
		"nonagint",
	};

	private static final String[] ZILLIONS_HUNDREDS_PREFIXES = {
		"cent",
		"ducent",
		"trecent",
		"quadringent",
		"quingent",
		"sescent",
		"septingent",
		"octigent",
		"nonigent",
	};

	/* Prerequisite: 0 <= rankIndex < 1000 */
	private static String getThousandsRankName(int rankIndex) {
		if (rankIndex == 0) {
			return "thousand";
		}
		if (rankIndex <= 10) {
			return THOUSANDS_SCALE_PREFIXES[rankIndex - 1] + "illion";
		}
		int rankIndexUnit = rankIndex % 10;
		int rankIndexTen = (rankIndex / 10) % 10;
		int rankIndexHundred = rankIndex / 100;
		String rankName = "";
		if (rankIndexUnit != 0) {
			rankName = ZILLIONS_UNITS_PREFIXES[rankIndexUnit - 1];
			if ((rankIndexUnit == 3 && (2 <= rankIndexTen && rankIndexTen <= 5 || rankIndexTen == 8))) {
				rankName += "s";
			} else if (rankIndexUnit == 6) {
				if (2 <= rankIndexTen && rankIndexTen <= 5) {
					rankName += "s";
				} else if (rankIndexTen == 8) {
					rankName += "x";
				}
			} else if (rankIndexUnit == 7 || rankIndexUnit == 9) {
				if (rankIndexTen == 2 || rankIndexTen == 8) {
					rankName += "m";
				} else if (rankIndexTen != 9) {
					rankName += "n";
				}
			}
		}
		if (rankIndexTen != 0) {
			rankName += ZILLIONS_TENS_PREFIXES[rankIndexTen - 1];
			if (rankIndexHundred != 0) {
				if (rankIndexTen == 1 || rankIndexTen == 2) {
					rankName += "i";
				} else {
					rankName += "a";
				}
			}
		}
		if (rankIndexHundred != 0) {
			rankName += ZILLIONS_HUNDREDS_PREFIXES[rankIndexHundred - 1];
		}
		return rankName + "illion";
	}


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
		if (doubleValue == 0) {
			return "zero";
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

	private static final BigInteger THOUSAND = BigInteger.valueOf(1000);


	/* Prerequisite: doubleValue >= 0 */
	private static void spellOutIntegralPart(double doubleValue, StringBuilder transcriber) {
		List<String> words = new ArrayList<>();

		if (doubleValue < 0x1p63) {
			/* Lower than Long.MAX_VALUE (not possibly equal to it, as it is not representable
			 * as a IEEE-754 double) => fits in a long. Process it as such */
			spellOutAsLong((long) doubleValue, words);
		} else {
			spellOutThousandGroup((long) (doubleValue % 1000.), words);
			BigInteger bigValue = asBigInteger(doubleValue);
			for (int i = 0; i < 102; ++i) {
				bigValue = bigValue.divide(THOUSAND);
				if (bigValue.equals(BigInteger.ZERO)) {
					break;
				}
				long thisGroup = bigValue.remainder(THOUSAND).longValue();
				if (thisGroup > 0) {
					String rankName = getThousandsRankName(i);
					words.add(rankName);
					spellOutThousandGroup(thisGroup, words);
				}
			}
		}
		for (int i = words.size() - 1; i > 0; --i) {
			transcriber.append(words.get(i)).append(' ');
		}
		transcriber.append(words.get(0));
	}

	/* Prerequisite: 0 <= longValue <= Long.MAX_VALUE */
	private static void spellOutAsLong(long longValue, List<String> words) {
		/* A quintillion is the highest power of a thousand (a "rank") in the range of a long */
		/* ... and 5 is the index of the quintillion's prefix in the ranks array. For longs,
		 * we won't go above this */
		spellOutThousandGroup(longValue % 1000, words);
		for (int i = 0; i < 6; ++i) {
			longValue /= 1000;
			if (longValue == 0) {
				break;
			}
			long thisGroup = longValue % 1000;
			if (thisGroup > 0) {
				String rankName = getThousandsRankName(i);
				words.add(rankName);
				spellOutThousandGroup(thisGroup, words);
			}
		}
	}

	/* Prerequisite: 0 <= longValue <= 999 */
	private static void spellOutThousandGroup(long longValue, List<String> words) {
		spellOutUnderOneHundred(longValue % 100, words);
		if (longValue >= 100) {
			words.add("hundred");
			spellOutDigit(longValue / 100, words);
		}
	}

	/* Prerequisite: 0 <= longValue <= 99 */
	private static void spellOutUnderOneHundred(long longValue, List<String> words) {
		if (longValue >= 20) {
			String groupTranscription = TENS_PREFIXES[(int) longValue / 10 - 2] + "ty";
			int remainder = (int) longValue % 10;
			if (remainder != 0) {
				groupTranscription += "-" + DIGITS_TEENS[remainder];
			}
			words.add(groupTranscription);
		} else if (longValue > 0) {
			spellOutDigit(longValue, words);
		}
	}

	/* Prerequisite: 0 <= longValue <= 19 */
	private static void spellOutDigit(long longValue, List<String> words) {
		words.add(DIGITS_TEENS[(int) longValue]);
	}

	/* Prerequisite: Long.MAX_VALUE < doubleValue */
	/* No risk of losing a fractional part: in this range, only integral numbers are representable */
	private static BigInteger asBigInteger(double doubleValue) {
		/* Subtract the significand width so that 1 <= significand < 2 */
		int exponent = Math.getExponent(doubleValue) - 52;
		/* Clip mantissa bits and add back implicit leading unit bit */
		long significand = (Double.doubleToLongBits(doubleValue) & 0xfffffffffffffL) + 0x10000000000000L;
		int additionalPowersOfTwo = Long.numberOfTrailingZeros(significand);
		/* Move powers of two from the significand to the exponent to minimize trhe former and maximize the latter */
		exponent += additionalPowersOfTwo;
		significand >>= additionalPowersOfTwo;
		return BigInteger.valueOf(significand).multiply(BigInteger.TWO.pow(exponent));
	}


	private static final NumberFormat FMT = NumberFormat.getInstance(Locale.US);

	static {
		FMT.setGroupingUsed(false);
		FMT.setMaximumFractionDigits(Integer.MAX_VALUE);
	}


	/* Prerequisite: doubleValue has a decimal part (not integral) */
	private static void spellOutDecimalPart(double doubleValue, StringBuilder transcriber) {
		/* Hack: format the value to a numeric string and parse the digits
		 * following the decimal separator */

		StringBuffer buffer = new StringBuffer();
		/* Request boundaries of the decimal (fraction) part of the number */
		FieldPosition pos = new FieldPosition(NumberFormat.FRACTION_FIELD);

		FMT.format(doubleValue, buffer, pos);
		for (int i = pos.getBeginIndex(); i < pos.getEndIndex(); ++i) {
			char digit = buffer.charAt(i);
			transcriber.append(' ').append(DIGITS_TEENS[digit - '0']);
		}
		// TODO
	}
}
