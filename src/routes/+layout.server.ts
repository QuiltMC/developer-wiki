import type { Category, GlobImport } from "$lib/types";
import type { LayoutServerLoadEvent } from "./$types";

export async function load({ params }: LayoutServerLoadEvent) {
	const articles: GlobImport = import.meta.glob(["$wiki/*.md", "!$wiki/+*.md"]);

	const categories: Category[] = [];

	for (const [path, resolver] of Object.entries(articles)) {
		const post = await resolver();

		if (post instanceof Function) continue; // The resolver can return itself, we need to filter that out

		for (const category of post.metadata.categories) {
			const cat = categories.find((cat) => cat.name === category);
			const slug = path.slice(path.lastIndexOf("/") + 1, -3);

			if (cat) {
				cat.pages.push({ slug, ...post.metadata });
			} else {
				categories.push({
					name: category,
					pages: [{ slug, ...post.metadata }]
				});
			}
		}
	}

	return { slug: params.slug, categories };
}
