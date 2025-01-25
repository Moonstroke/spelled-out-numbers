package io.github.moonstroke.spelledoutnumbers.impl;

import java.util.Locale;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

public class FranceFrenchNumberSpeller implements NumberSpeller {

	@Override
	public Locale getSupportedLocale() {
		return Locale.FRANCE;
	}

	@Override
	public String spellOut(double doubleValue) {
		throw new UnsupportedOperationException("Not implemente yet"); // TODO
	}

}
