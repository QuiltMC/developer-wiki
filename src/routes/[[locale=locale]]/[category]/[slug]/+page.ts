import { error } from "@sveltejs/kit";
import { get } from "svelte/store";
import YAML from "yaml";

import type { PageLoadEvent } from "./$types";

import { locale, t } from "$lib/translations";

export async function load({ params }: PageLoadEvent) {
	try {
		// get the metadata from the yaml file instead of the makdown file header
		const post_metadata = YAML.parse(
			(await import(`../../../../../wiki/${params.category}/${params.slug}/+page.yml?raw`)).default
		);

		try {
			// the makrdown file only contains the content of the page
			const post = await import(
				`../../../../../wiki/${params.category}/${params.slug}/${get(locale)}.md`
			);

			return {
				content: post.default,
				title: post_metadata.title,
				isDraft: post_metadata.draft || false
			};
		} catch (err) {
			if (
				!post_metadata.draft &&
				err instanceof Error &&
				err.message.match(/Unknown variable dynamic import.+/)
			) {
				throw error(
					404,
					t.get("error.not-translated", { placeholder: t.get(`lang.${get(locale)}`) })
				);
			} else if (post_metadata.draft) {
				// still displays the draft warning if the markdown file is missing
				return {
					title: post_metadata.title,
					isDraft: post_metadata.draft || false
				};
			} else {
				throw err;
			}
		}
	} catch (err) {
		if (err instanceof Error && err.message.match(/Unknown variable dynamic import.+/)) {
			throw error(
				404,
				t.get("error.not-found", { placeholder: `/${params.category}/${params.slug}` })
			);
		} else {
			throw err;
		}
	}
}
