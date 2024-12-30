package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

public class DefaultNumberSpeller implements NumberSpeller {
	@Override
	public Locale getSupportedLocale() {
		return Locale.getDefault();
	}

	@Override
	public String spellOut(double doubleValue) throws NumberFormatException {
		return ""; // TODO
	}
}
