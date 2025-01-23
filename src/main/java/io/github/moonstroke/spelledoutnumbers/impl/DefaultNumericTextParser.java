package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;

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
