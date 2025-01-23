package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * This interface provides the text-to-numeric translation service.
 *
 * @author Moonstroke
 */
public interface NumericTextParser extends LocalizedNumberService {
	/**
	 * Parse the given text into a numeric value, using this parser's locale.
	 *
	 * @param text The text to parse
	 *
	 * @return The numeric value represented by the given text
	 *
	 * @throws NumberFormatException if the given text does not represent a number in this locale
	 */
	double parse(String text) throws NumberFormatException;

	/**
	 * Find a text parser supporting the specified locale.
	 *
	 * @param locale The locale to support
	 *
	 * @return a text parser supporting the given locale, or {@code null} if none matched
	 */
	static NumericTextParser getTextParserFor(Locale locale) {
		return LocalizedNumberService.getHandlerFor(locale, NumericTextParser.class);
	}
}
