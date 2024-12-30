package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;

/**
 * Text parser that supports the default JVM locale, usually {@link Locale#US en-US}.
 *
 * It only supports all-lowercase text.
 */
public class DefaultNumericTextParser implements NumericTextParser {
	@Override
	public Locale getSupportedLocale() {
		return Locale.getDefault();
	}

	@Override
	public double parse(String text) throws NumberFormatException {
		return Double.NaN; // TODO
	}
}
