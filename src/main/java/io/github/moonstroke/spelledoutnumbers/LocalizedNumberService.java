package io.github.moonstroke.spelledoutnumbers;

import java.util.Locale;
import java.util.ServiceLoader;

/**
 * Interface representing any service regarding numeric values that depends on a locale.
 *
 * @author Moonstroke
 */
interface LocalizedNumberService {
	/**
	 * Return the locale in which this object handles numeric values.
	 *
	 * @return this service handler's locale
	 */
	Locale getSupportedLocale();

	/**
	 * Retrieve an implementation of the given service class that supports the specified locale.
	 * 
	 * @param <S>          The locale-dependent number service interface
	 * @param locale       The locale to support
	 * @param serviceClass The interface for the service to provide
	 * 
	 * @return The first discovered matching implementation of the given service interface, or {@code null} if none
	 *         matches.
	 *
	 * @apiNote This method is not to be used directly by users of this interface; it is a helper method for
	 *          subinterfaces.
	 */
	static <S extends LocalizedNumberService> S getHandlerFor(Locale locale, Class<S> serviceClass) {
		for (S serviceImpl : ServiceLoader.load(serviceClass)) {
			if (serviceImpl.getSupportedLocale().equals(locale)) {
				return serviceImpl;
			}
		}
		return null;
	}
}
