package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * This interface provides the numeric-to-text translation service.
 *
 * @author Moonstroke
 */
public interface NumberSpeller {
	/**
	 * Return the locale in which this speller transcribes numeric values.
	 *
	 * @return this speller's locale
	 */
	public Locale getSupportedLocale();

	/**
	 * Transcribe the given real numeric value, using this speller's locale.
	 *
	 * @param doubleValue The value to transcribe
	 *
	 * @return A textual representation of the given number
	 */
	public String spellOut(double doubleValue);
}
