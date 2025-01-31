package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * A (real) number, with its textual representation in a given locale.
 *
 * @author Moonstroke
 */
public class SpelledOutNumber extends Number implements Comparable<SpelledOutNumber> {
	private static final long serialVersionUID = -2400427537564695903L;

	private final double doubleValue;
	private final Locale locale;
	private final String transcription;


	private SpelledOutNumber(Locale locale, double doubleValue, String transcription) {
		this.locale = locale;
		this.transcription = transcription;
		this.doubleValue = doubleValue;
	}

	/**
	 * Construct a spelled-out number from its numeric value for the specified locale.
	 *
	 * @param locale      The locale in which to transcribe the number
	 * @param doubleValue The real numeric value
	 */
	public SpelledOutNumber(Locale locale, double doubleValue) {
		this(locale, doubleValue, spellOut(locale, doubleValue));
	}

	/* Compute the transcription of the given value */
	private static String spellOut(Locale locale, double doubleValue) {
		return NumberSpeller.getNumberSpellerFor(locale).spellOut(doubleValue);
	}

	/**
	 * Construct a spelled-out number from its transcription in a given locale.
	 *
	 * @param locale        The local in which the number is transcribed
	 * @param transcription The textual transcription of the number
	 *
	 * @throws NumberFormatException if the transcription does not represent a valid number
	 */
	public SpelledOutNumber(Locale locale, String transcription) throws NumberFormatException {
		this(locale, parse(locale, transcription), transcription);
	}

	/* Compute the numeric value represented by the given text */
	private static double parse(Locale locale, String transcription) {
		return NumericTextParser.getTextParserFor(locale).parse(transcription);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int intValue() {
		return (int) doubleValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		return (long) doubleValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		return (float) doubleValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doubleValue() {
		return doubleValue;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @apiNote This method is not fit for numeric comparison: it performs field-wise object comparison. Therefore, two
	 *          objects referring to the same number will be considered unequal by this method. To perform numeric
	 *          comparison, use {@link #compareTo}.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpelledOutNumber)) {
			return false;
		}
		SpelledOutNumber that = (SpelledOutNumber) o;
		return this.locale.equals(that.locale)
		       /* Bitwise check to avoid false positives (e.g. -0.0 == 0.0) */
		       && Double.doubleToLongBits(this.doubleValue) == Double.doubleToLongBits(that.doubleValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return locale.hashCode() * 31 & Double.hashCode(doubleValue);
	}

	/**
	 * Return the textual transcription, in its locale, of this number.
	 */
	@Override
	public String toString() {
		return transcription;
	}

	/**
	 * Compare this number to the given one.
	 *
	 * @apiNote The objects are compared numerically: only the numbers they represent are compared (not the locales,
	 *          nor the transcriptions). To compare the objects as objects, prefer {@link #equals}.
	 */
	@Override
	public int compareTo(SpelledOutNumber o) {
		return Double.compare(doubleValue, o.doubleValue);
	}

	/**
	 * Construct a spelled-out number for the same numeric value in a different locale.
	 *
	 * @param otherLocale The other locale in which to transcribe this number
	 *
	 * @return a copy of this number, transcribed in a different locale
	 */
	public SpelledOutNumber toLocale(Locale otherLocale) {
		return new SpelledOutNumber(otherLocale, doubleValue);
	}
}
