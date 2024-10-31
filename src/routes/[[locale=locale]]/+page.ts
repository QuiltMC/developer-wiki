import { negotiateLanguages } from "@fluent/langneg";

import type { Component } from "svelte";

import { type Locale, defaultLocale } from "$l10n";
import current from "$lib/current.svelte";

export async function load({ data, parent }): Promise<
	{
		content: Component;
	} & ({ translated: true } | { translated: false; fallbackLocale: Locale })
> {
	// Wait for the current locale to be set by the parent layout
	await parent();

	if (data.availableLocales.includes(current.locale)) {
		// Load the content of the landing page in the current locale if available
		const content = await import(`../../../wiki/landing-page/${current.locale}.md`).then(
			(c) => c.default
		);

		return {
			content,
			translated: true
		};
	} else {
		// Load the content of the landing page in one of the user's fallback locales
		// (taking the assumptions that the landing page is at least translated in english)
		const fallbackLocale = negotiateLanguages(current.locales, data.availableLocales, {
			defaultLocale,
			strategy: "lookup"
		})[0] as Locale;

		const content = await import(`../../../wiki/landing-page/${fallbackLocale}.md`).then(
			(c) => c.default
		);

		return {
			content,
			translated: false,
			fallbackLocale
		};
	}
}
