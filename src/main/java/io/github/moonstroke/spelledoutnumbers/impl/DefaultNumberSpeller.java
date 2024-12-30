package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

/**
 * Number speller that supports the default JVM locale, usually {@link Locale#US en-US}.
 *
 * It spells numbers in all-lowercase text.
 */
public class DefaultNumberSpeller implements NumberSpeller {
	@Override
	public Locale getSupportedLocale() {
		return Locale.getDefault();
	}

	@Override
	public String spellOut(double doubleValue) throws NumberFormatException {
		if (Double.isNaN(doubleValue)) {
			return "not a number";
		}
		if (doubleValue < 0) {
			return "minus " + spellOut(-doubleValue);
		}
		if (Double.isInfinite(doubleValue)) {
			return "infinity";
		}
		if (doubleValue == 0) {
			return "zero";
		}
		return ""; // TODO
	}
}
