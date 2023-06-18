import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent) {
	const post = await import(`../../../wiki/${params.slug}.md`);

	return { content: post.default, title: post.metadata.title };
}

export async function entries() {
	const articles = Object.keys(import.meta.glob(["$wiki/*.md", "!$wiki/+*.md"]));

	return articles.map((path) => {
		return { slug: path.slice(path.lastIndexOf("/") + 1, -3) };
	});
}
