// @ts-expect-error the svelte-fluent vite plugin allows to import ftl files
import resourceEn from "./en.ftl";
// @ts-expect-error but it seams like typescript dosn´t know that
import resourceFr from "./fr.ftl";

import { FluentResource } from "@fluent/bundle";

/**
 * List of all supported locales
 * (see also kit.prerender.entries in svelte.config.js when adding locales)
 */
export const supportedLocales = ["en", "fr"] as const;

export type Locale = (typeof supportedLocales)[number];

export function isLocale(maybeLocale: string): maybeLocale is Locale {
	return supportedLocales.includes(maybeLocale as Locale);
}

export const defaultLocale: Locale = "en";

/**
 * The text direction of each supported locale
 */
export const localesDir: Record<Locale, "ltr" | "rtl"> = {
	en: "ltr",
	fr: "ltr"
};

/**
 * The fluent resource for each locale
 */
export const resources: Record<Locale, FluentResource> = {
	en: resourceEn,
	fr: resourceFr
};

/**
 * Removes the locale from the given pathname (if a locale is found)
 */
export function extractRoute(pathname: string): string {
	const routeExtractor = new RegExp(`^/([^/]+)?(/.+)?$`);

	const routeMatch = pathname.match(routeExtractor);

	// Locale found, return path without the locale, defaulting to homepage
	if (routeMatch && routeMatch[1] && isLocale(routeMatch[1])) return routeMatch[2] ?? "/";

	// Just return the pathname in all other cases
	return pathname;
}
