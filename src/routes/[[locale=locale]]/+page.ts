import { negotiateLanguages } from "@fluent/langneg";
import { get } from "svelte/store";

import type { ComponentType, SvelteComponent } from "svelte";

import { currentLocale, currentLocales, defaultLocale, type Locale } from "$l10n";

export async function load({
	data,
	parent
}): Promise<
	{ content: ComponentType<SvelteComponent> } & (
		| { translated: true }
		| { translated: false; fallbackLocale: Locale }
	)
> {
	// Wait for the current locale to be set by the parent layout
	await parent();

	if (data.availableLocales.includes(get(currentLocale))) {
		// Load the content of the landing page in the current locale if available
		const landingPage = await import(`../../../wiki/landing-page/${get(currentLocale)}.md`);

		return {
			content: landingPage.default,
			translated: true
		};
	} else {
		// Load the content of the landing page in one of the user's fallback locales
		// (taking the assumptions that the landing page is at least translated in english)
		const fallbackLocale = negotiateLanguages(get(currentLocales), data.availableLocales, {
			defaultLocale,
			strategy: "lookup"
		})[0] as Locale;

		const landingPage = await import(`../../../wiki/landing-page/${fallbackLocale}.md`);

		return {
			content: landingPage.default,
			translated: false,
			fallbackLocale
		};
	}
}
