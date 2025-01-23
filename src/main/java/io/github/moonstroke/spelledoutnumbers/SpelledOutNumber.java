package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

public class SpelledOutNumber extends Number {
	private static final long serialVersionUID = -2400427537564695903L;

	private final double doubleValue;
	private final Locale locale;
	private final String transcription;


	public SpelledOutNumber(Locale locale, double doubleValue) {
		this.locale = locale;
		this.transcription = null; // TODO
		this.doubleValue = doubleValue;
	}

	@Override
	public int intValue() {
		return (int) doubleValue;
	}

	@Override
	public long longValue() {
		return (long) doubleValue;
	}

	@Override
	public float floatValue() {
		return (float) doubleValue;
	}

	@Override
	public double doubleValue() {
		return doubleValue;
	}
}
