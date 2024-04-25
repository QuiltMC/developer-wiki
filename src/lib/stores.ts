import { locale, locales } from "./translations";

import { derived } from "svelte/store";

import { page } from "$app/stores";

// Removes the current locale from the current route
// (only if the current route does contain the locale)
export const current_route = derived([page, locale, locales], ([$page, $locale, $locales], set) => {
	const routeExtractor = new RegExp(`^(/${$locale})?(/.+)?$`);

	const routeMatch = $page.url.pathname.match(routeExtractor);
	// prettier-ignore
	if (
			routeMatch &&
			(
				// We match the locale
				routeMatch[1] ||
				// We don't match the locale and a locale isn't in the route
				routeMatch[2] && !$locales.includes(routeMatch[2].replace(/^\/([^/]+)(\/.+)?$/, "$1"))
			)
		) {
			set(routeMatch[2] || "");
		}
});
