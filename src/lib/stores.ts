import { derived } from "svelte/store";

import { page } from "$app/stores";
import { isLocale, currentLocale } from "$l10n";

// Removes the current locale from the current route
// (only if the current route does contain the locale)
export const currentRoute = derived(
	[page, currentLocale],
	([$page, $currentLocale], set) => {
		const routeExtractor = new RegExp(`^(/${$currentLocale})?(/.+)?$`);

		const routeMatch = $page.url.pathname.match(routeExtractor);
		// prettier-ignore
		if (
			routeMatch &&
			(
				// We match the locale
				routeMatch[1] ||
				// We don't match the locale and a locale isn't in the route
				routeMatch[2] && !isLocale(routeMatch[2].replace(/^\/([^/]+)(\/.+)?$/, "$1"))
			)
		) {
			set(routeMatch[2] ?? "");
		}
	},
	""
);
