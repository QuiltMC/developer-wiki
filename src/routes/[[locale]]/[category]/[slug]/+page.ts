import { error } from "@sveltejs/kit";
import YAML from "yaml";

import type { ComponentType, SvelteComponent } from "svelte";

import { t } from "$lib/translations";

export async function load({ params }): Promise<{
	content?: ComponentType<SvelteComponent>;
	title: string;
	warningMessage: { key: string; placeholder: string } | false;
}> {
	try {
		// get the metadata from the yaml file instead of the makdown file header
		const post_metadata = YAML.parse(
			(await import(`../../../../../wiki/${params.category}/${params.slug}/+page.yml?raw`)).default
		);

		try {
			// the makrdown file only contains the content of the page
			const post = await import(
				`../../../../../wiki/${params.category}/${params.slug}/${params.locale}.md`
			);

			return {
				content: post.default,
				title: post_metadata.title,
				warningMessage: post_metadata.draft
					? {
							key: "application.draft-notice",
							placeholder: "https://github.com/QuiltMC/developer-wiki"
					  }
					: false
			};
		} catch (err) {
			if (
				!post_metadata.draft &&
				err instanceof Error &&
				err.message.match(/Unknown variable dynamic import.+/)
			) {
				// Display a warning message if the page isn't
				// translated in the current locale
				return {
					title: post_metadata.title,
					warningMessage: {
						key: "error.not-translated",
						placeholder: t.get(`lang.${params.locale}`)
					}
				};
			} else if (post_metadata.draft) {
				// still displays the draft warning if the markdown file is missing
				return {
					title: post_metadata.title,
					warningMessage: post_metadata.draft
						? {
								key: "application.draft-notice",
								placeholder: "https://github.com/QuiltMC/developer-wiki"
						  }
						: false
				};
			} else {
				throw err;
			}
		}
	} catch (err) {
		if (err instanceof Error && err.message.match(/^Unknown variable dynamic import.+$/)) {
			throw error(
				404,
				t.get("error.not-found", { placeholder: `/${params.category}/${params.slug}` })
			);
		} else {
			throw err;
		}
	}
}
