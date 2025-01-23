package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * This interface provides the text-to-numeric translation service.
 *
 * @author Moonstroke
 */
public interface NumericTextParser {
	/**
	 * Return the locale in which this parser computes numeric values.
	 *
	 * @return this parser's locale
	 */
	Locale getSupportedLocale();

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
}
