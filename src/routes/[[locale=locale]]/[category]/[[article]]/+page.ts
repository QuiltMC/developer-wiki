import { negotiateLanguages } from "@fluent/langneg";
import { error } from "@sveltejs/kit";
import { get } from "svelte/store";

import type { ComponentType, SvelteComponent } from "svelte";

import { type Locale, defaultLocale, currentLocales, currentLocale } from "$l10n";

export async function load({ params, parent }): Promise<
	{
		content?: ComponentType<SvelteComponent>;
		title: string;
		draft: boolean;
	} & (
		| { translated: true }
		| {
				translated: false;
				fallbackLocale: Locale;
		  }
	)
> {
	// Get the data loaded in the parent layouts
	const parent_data = await parent();

	// Get the category from the data loaded in the root `+layout.server.ts`
	const category = parent_data.categories.find((category) => category.slug === params.category);

	// Category not found
	if (!category) {
		error(404);
	}

	// Get the article from the loaded category
	const article = category.pages.find((page) => page.slug === params.article);

	// Article not found
	if (!article) {
		error(404);
	}

	if (article.availableLocales.includes(get(currentLocale))) {
		// Load the content of the article for the current locale if available
		const content = await import(
			`../../../../../wiki/${category.slug}/${article.slug}/${get(currentLocale)}.md`
		);

		return {
			title: `${category.slug}.${article.slug}`,
			draft: article.draft,
			content: content.default,
			translated: true
		};
	} else if (article.availableLocales.length) {
		// Load the content of the article for one of the user's prefered locales
		// (taking the assumptions that if the article exists in a locale, it should exist in english)
		const fallbackLocale = negotiateLanguages(get(currentLocales), article.availableLocales, {
			defaultLocale,
			strategy: "lookup"
		})[0] as Locale;

		const content = await import(
			`../../../../../wiki/${category.slug}/${article.slug}/${fallbackLocale}.md`
		);

		return {
			title: `${category.slug}.${article.slug}`,
			draft: article.draft,
			content: content.default,
			translated: false,
			fallbackLocale
		};
	} else if (article.draft) {
		// Still display the draft message if the article doesn't have any content yet
		return {
			title: `${category.slug}.${article.slug}`,
			draft: true,
			translated: true
		};
	} else {
		error(404);
	}
}
