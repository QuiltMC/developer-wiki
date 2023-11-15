import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { locale, locales } from "$lib/translations/index";

export function load({ params }) {
	if (params.locale && locales.get().includes(params.locale)) {
		locale.set(params.locale);
	} else if (browser) {
		// Get the first supported locale in the user's browser's selected languages
		const navigator_language = window.navigator.languages.find((navigator_language) =>
			locales.get().includes(navigator_language)
		);

		if (navigator_language) {
			throw redirect(308, `/${navigator_language}${window.location.pathname}`);
		} else {
			throw redirect(308, `/en${window.location.pathname}`);
		}
	}
}
