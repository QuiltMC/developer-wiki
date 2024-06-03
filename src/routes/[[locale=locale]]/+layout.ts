import { negotiateLanguages } from "@fluent/langneg";
import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { supportedLocales, defaultLocale, currentLocale, type Locale } from "$l10n";

export async function load({ params }) {
	// We know that the locale is valid thanks to the
	// /src/params/locale matcher
	if (params.locale) {
		currentLocale.set(params.locale as Locale);
	} else if (browser) {
		// Get the best possible language for the user
		const navigator_language = negotiateLanguages(window.navigator.languages, supportedLocales, {
			defaultLocale,
			strategy: "lookup"
		})[0];

		redirect(308, `/${navigator_language}${window.location.pathname}`);
	}
}
