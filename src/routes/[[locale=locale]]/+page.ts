import type { ComponentType, SvelteComponent } from "svelte";

import { defaultLocale } from "$lib/translations/index.js";

export async function load({ params }): Promise<{ content: ComponentType<SvelteComponent> }> {
	try {
		const landing_page = await import(
			`../../../wiki/landing-page/${params.locale || defaultLocale}.md`
		);

		return {
			content: landing_page.default
		};
	} catch (err) {
		if (err instanceof Error && err.message.match(/Unknown variable dynamic import.+/)) {
			// Return the english version if the translated version is missing
			const landing_page = await import(`../../../wiki/landing-page/${defaultLocale}.md`);

			return {
				content: landing_page.default
			};
		} else {
			throw err;
		}
	}
}
