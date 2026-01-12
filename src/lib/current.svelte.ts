import { FluentBundle } from "@fluent/bundle";
import { negotiateLanguages } from "@fluent/langneg";

import { browser } from "$app/environment";
import { type Locale, defaultLocale, supportedLocales, resources } from "$l10n";

let currentLocale: Locale = $state(defaultLocale);

/**
 * The prefered locales of the user
 * with the current locale as the first element
 */
const currentLocales: Locale[] = $derived.by(() => {
	if (browser) {
		const fallbackLocales = negotiateLanguages(navigator.languages, supportedLocales, {
			defaultLocale
		}).filter((locale) => locale !== currentLocale) as Locale[]; // Remove the current locale to not add it twice

		return [
			currentLocale,
			// Add the user's prefered languages as fallback
			...fallbackLocales
		];
	} else if (currentLocale != defaultLocale) {
		// Only add the default locale when generating on the server
		return [currentLocale, defaultLocale];
	} else {
		return [defaultLocale];
	}
});

/**
 * The bundle with the translations resource
 * of the current locales
 */
const currentBundle: FluentBundle = $derived.by(() => {
	const bundle = new FluentBundle(currentLocales);

	currentLocales.forEach((locale) => bundle.addResource(resources[locale]));

	return bundle;
});

// We export everything in an object so that we can use the get and set functions
export default {
	get locale(): Locale {
		return currentLocale;
	},
	set locale(locale: Locale) {
		currentLocale = locale;
	},
	get locales(): Locale[] {
		return currentLocales;
	},
	get bundle(): FluentBundle {
		return currentBundle;
	}
};
