package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * This interface provides the numeric-to-text translation service.
 *
 * @author Moonstroke
 */
public interface NumberSpeller extends LocalizedNumberService {
	/**
	 * Transcribe the given real numeric value, using this speller's locale.
	 *
	 * @param doubleValue The value to transcribe
	 *
	 * @return A textual representation of the given number
	 */
	String spellOut(double doubleValue);

	/**
	 * Find a number speller supporting the specified locale.
	 *
	 * @param locale The locale to support
	 *
	 * @return a number speller supporting the given locale, or {@code null} if none matched
	 */
	static NumberSpeller getNumberSpellerFor(Locale locale) {
		return LocalizedNumberService.getHandlerFor(locale, NumberSpeller.class);
	}
}
