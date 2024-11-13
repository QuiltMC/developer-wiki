import { negotiateLanguages } from "@fluent/langneg";
import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { type Locale, defaultLocale, supportedLocales } from "$l10n";
import current from "$lib/current.svelte";

export async function load({ params }) {
	// We know that the locale is valid thanks to the
	// /src/params/locale matcher
	if (params.locale) {
		current.locale = params.locale as Locale;
	} else if (browser) {
		// Get the best possible language for the user
		const navigator_language = negotiateLanguages(window.navigator.languages, supportedLocales, {
			defaultLocale,
			strategy: "lookup"
		})[0];

		redirect(308, `/${navigator_language}${window.location.pathname}`);
	}
}
