import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent) {
	const post = await import(`../../../../wiki/${params.category}/${params.slug}.md`);

	return { content: post.default, title: post.metadata.title };
}

export async function entries() {
	const articles = Object.keys(import.meta.glob("$wiki/**/*.md"));

	return articles.map((path) => {
		return { category: path.slice(path.indexOf("/", 1) + 1, path.lastIndexOf("/")), slug: path.slice(path.lastIndexOf("/") + 1, -3) };
	});
}
