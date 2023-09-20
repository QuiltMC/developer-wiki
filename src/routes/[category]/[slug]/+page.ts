import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent) {
	const post = await import(`../../../../wiki/${params.category}/${params.slug}.md`);

	if (!post.metadata.draft) post.metadata.draft = false

	const draft = await import(`../../../../draft.md`);
	return { content: post.default, title: post.metadata.title, draft: draft.default, isDraft: post.metadata.draft };
}

export async function entries() {
	const articles = Object.keys(import.meta.glob("$wiki/**/*.md"));

	return articles.map((path) => {
		const [, , category, slug] = path.split("/");
		return { category, slug: slug.slice(0, -3) };
	});
}
