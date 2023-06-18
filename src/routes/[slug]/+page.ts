import type { Post } from "$lib/types";
import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent) {
	const articles = await import.meta.glob(`$wiki/*.md`);
	const path = Object.keys(articles).find((article) => article.endsWith(params.slug + ".md")) || "";
	const post: Post = await articles[path]();
	const { title } = post.metadata;
	const content = post.default;

	return { content, title, slug: params.slug };
}

export async function entries() {
	const articles = Object.keys(import.meta.glob(["$wiki/*.md", "!$wiki/+*.md"]));

	return articles.map((path) => {
		return { slug: path.slice(path.lastIndexOf("/") + 1, -3) };
	});
}
