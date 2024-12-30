package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * A (real) number, with its textual representation in a given locale.
 *
 * @author Moonstroke
 */
public class SpelledOutNumber extends Number {
	private static final long serialVersionUID = -2400427537564695903L;

	private final double doubleValue;
	private final Locale locale;
	private final String transcription;


	/**
	 * Construct a spelled-out number from its numeric value for the specified locale.
	 *
	 * @param locale      The locale in which to transcribe the number
	 * @param doubleValue The real numeric value
	 */
	public SpelledOutNumber(Locale locale, double doubleValue) {
		this.locale = locale;
		this.transcription = null; // TODO
		this.doubleValue = doubleValue;
	}

	/**
	 * Construct a spelled-out number from its transcription in a given locale.
	 *
	 * @param locale        The local in which the number is transcribed
	 * @param transcription The textual transcription of the number
	 */
	public SpelledOutNumber(Locale locale, String transcription) {
		this.locale = locale;
		this.transcription = transcription;
		this.doubleValue = Double.NaN; // TODO
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
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpelledOutNumber)) {
			return false;
		}
		SpelledOutNumber that = (SpelledOutNumber) o;
		return this.locale.equals(that.locale) && this.doubleValue == that.doubleValue;
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
