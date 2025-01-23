package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;

/**
 * Interface representing any service regarding numeric values that depends on a locale.
 *
 * @author Moonstroke
 */
public interface LocalizedNumberService {
	/**
	 * Return the locale in which this object handles numeric values.
	 *
	 * @return this service handler's locale
	 */
	Locale getSupportedLocale();
}
