import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { locales, setLocale } from "$lib/translations/index";

export async function load({ params }) {
	// We know that the locale is valid thanks to the
	// /src/params/locale matcher
	if (params.locale) {
		await setLocale(params.locale);
	} else if (browser) {
		// Get the first supported locale in the user's browser's selected languages
		const navigator_language = window.navigator.languages.find((navigator_language) =>
			locales.get().includes(navigator_language)
		);

		if (navigator_language) {
			redirect(308, `/${navigator_language}${window.location.pathname}`);
		} else {
			redirect(308, `/en${window.location.pathname}`);
		}
	}
}
