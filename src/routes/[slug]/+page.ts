import fs from "fs/promises";

import type { PageLoadEvent } from "./$types";

export async function load({ params }: PageLoadEvent){
	const post = await import(`../${params.slug}.md`);
	const { title, categories } = post.metadata;
	const content = post.default;

	return { content, title, categories };
}

export async function entries(){
	const articles = await fs.readdir("src/routes").then(files => files.filter(path => path.endsWith(".md") && !path.startsWith("+")));
	return articles.map(path => { return { slug: path.slice(0, -3) }});
}
