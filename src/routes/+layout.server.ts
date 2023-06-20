import type { Category, GlobImport } from "$lib/types";
import type { LayoutServerLoadEvent } from "./$types";


export async function load({ params }: LayoutServerLoadEvent) {
	const articles: GlobImport = import.meta.glob("$wiki/**/*.md");

	const categories: Category[] = [];

	for (const [path, resolver] of Object.entries(articles)) {
		const post = await resolver();

		if (post instanceof Function) continue; // The resolver can return itself, we need to filter that out

		const [, , category, slug] = path.split("/");
		const cat = categories.find((cat) => cat.name === category);
		const page = {
			slug: slug.slice(0, -3),
			title: post.metadata.title,
			category: post.metadata.category
		};

		if (cat) {
			cat.pages.push(page);
		} else {
			categories.push({ name: page.category, slug: category, pages: [page] });
		}
	}

	return { category: params.category, slug: params.slug, categories };
}
