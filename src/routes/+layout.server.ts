import fs from "fs/promises";

import type { Category } from "$lib/types";

export async function load() {
	const files = await fs.readdir("src/routes");
	const articles = files
		.filter((path) => path.endsWith(".md") && !path.startsWith("+"))
		.map((path) => path.slice(0, -3));

	const categories: Category[] = [];

	for (const article of articles) {
		const post = await import(`../routes/${article}.md` /* @vite-ignore */); // Don't ask, it works

		for (const category of post.metadata.categories) {
			const cat = categories.find((cat) => cat.name === category);

			if (cat) {
				cat.pages.push(post.metadata);
			} else {
				categories.push({
					name: category,
					pages: [{ slug: article, title: post.metadata.title }]
				});
			}
		}
	}

	return { categories };
}
