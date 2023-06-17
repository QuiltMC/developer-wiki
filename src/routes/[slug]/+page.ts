import fs from "fs/promises";

import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent){
	const post = await import(`../${params.slug}.md` /* @vite-ignore */ );
	const { title, categories } = post.metadata;
	const content = post.default;

	return { content, title, categories, slug: params.slug };
}

export async function entries(){
	const files = await fs.readdir("src/routes");
	const articles = files.filter(path => path.endsWith(".md") && !path.startsWith("+"));
	return articles.map(path => { return { slug: path.slice(0, -3) }});
}
