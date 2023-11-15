import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { locale, locales } from "$lib/translations/index";

export function load({ params }) {
	if (params.locale && locales.get().includes(params.locale)) {
		locale.set(params.locale);
	} else if (browser) {
		if (window.navigator.language && locales.get().includes(window.navigator.language)) {
			throw redirect(308, `/${window.navigator.language}${window.location.pathname}`);
		} else {
			throw redirect(308, `/en${window.location.pathname}`);
		}
	}
}
