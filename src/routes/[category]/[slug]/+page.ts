import { error } from "@sveltejs/kit";

import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent) {
	try {
		const post = await import(`../../../../wiki/${params.category}/${params.slug}.md`);

		return {
			content: post.default,
			title: post.metadata.title,
			isDraft: post.metadata.draft as boolean
		};
	} catch (err) {
		if (err instanceof Error && err.message.match(/Unknown variable dynamic import.+/)) {
			throw error(404, `No wiki page found at: /${params.category}/${params.slug}`);
		} else {
			throw err;
		}
	}
}

// export async function entries() {
// 	const articles = Object.keys(import.meta.glob("$wiki/**/*.md"));

// 	return articles.map((path) => {
// 		const [, , category, slug] = path.split("/");
// 		return { category, slug: slug.replace(".md", "") };
// 	});
// }
