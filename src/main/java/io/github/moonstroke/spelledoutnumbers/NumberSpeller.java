package io.github.moonstroke.spelledoutnumbers;

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
}
