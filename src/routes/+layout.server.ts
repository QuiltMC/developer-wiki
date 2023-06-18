import type { Category, Post } from "$lib/types";

export async function load() {
	const articles = import.meta.glob(["$wiki/*.md", "!$wiki/+*.md"]);

	const categories: Category[] = [];

	for (const article in articles) {
		const post: Post = await articles[article]();

		for (const category of post.metadata.categories) {
			const cat = categories.find((cat) => cat.name === category);

			if (cat) {
				cat.pages.push(post.metadata);
			} else {
				categories.push({
					name: category,
					pages: [
						{ slug: article.slice(article.lastIndexOf("/") + 1, -3), title: post.metadata.title }
					]
				});
			}
		}
	}

	return { categories };
}
