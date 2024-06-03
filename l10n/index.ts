import translationsEn from "./en.ftl?raw";
import translationsFr from "./fr.ftl?raw";

import { FluentResource, FluentBundle } from "@fluent/bundle";
import { negotiateLanguages } from "@fluent/langneg";
import { derived, writable, type Readable, type Writable } from "svelte/store";

import { browser } from "$app/environment";

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

export const currentLocale: Writable<Locale> = writable(defaultLocale);

/**
 * List of supported locales that are rtl (none for now)
 */
const rtlLocales: Locale[] = [];

export function isRtl(locale: Locale): boolean {
	return rtlLocales.includes(locale);
}

export const currentDir: Readable<"ltr" | "rtl"> = derived(currentLocale, ($currentLocale, set) => {
	set(isRtl($currentLocale) ? "rtl" : "ltr");
});

/**
 * The prefered locales of the user
 * with the current locale as the first element
 */
export const currentLocales: Readable<Locale[]> = derived(currentLocale, ($currentLocale, set) => {
	if (browser) {
		const fallbackLocales = negotiateLanguages(navigator.languages, supportedLocales, {
			defaultLocale
		}).filter((locale) => locale !== $currentLocale) as Locale[]; // Remove the current locale to not add it twice

		set([
			$currentLocale,
			// Add the user's prefered languages as fallback
			...fallbackLocales
		]);
	} else if ($currentLocale != defaultLocale) {
		// Only add the default locale when generating on the server
		set([$currentLocale, defaultLocale]);
	} else {
		set([defaultLocale]);
	}
});

/**
 * All the translations as fluent resources
 */
const resources: { [L in Locale]: FluentResource } = {
	en: new FluentResource(translationsEn),
	fr: new FluentResource(translationsFr)
};

/**
 * The bundle with the translations resource
 * of the current locales
 */
export const currentBundle: Readable<FluentBundle> = derived(
	[currentLocales],
	([$currentLocales], set) => {
		const bundle = new FluentBundle($currentLocales);

		$currentLocales.forEach((locale) => {
			bundle.addResource(resources[locale]);
		});

		set(bundle);
	}
);
