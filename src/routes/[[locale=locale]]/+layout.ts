import { redirect } from "@sveltejs/kit";

import { browser } from "$app/environment";
import { locale, locales } from "$lib/translations/index";

export function load({ params }) {
	// We know that the locale is a valid locale
	// because of the /src/params/locale ParamMatcher
	if (params.locale) {
		locale.set(params.locale);
	} else if (browser) {
		let current_route = window.location.pathname;
		// Removes any locale from the route
		locales.get().forEach((locale) => {
			current_route = current_route.replace(`/${locale}`, "");
		});

		if (window.navigator.language) {
			throw redirect(308, `/${window.navigator.language}${current_route}`);
		} else {
			throw redirect(308, `/en${current_route}`);
		}
	}
}

export const prerender = true;
