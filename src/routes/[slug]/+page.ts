import type { PageLoadEvent } from "./$types";
import fs from "fs";

export async function load({ params }: PageLoadEvent){
	const post = await import(`../${params.slug}.md`);
	const { title, categories } = post.metadata;
	const content = post.default;

	return { content, title, categories };
}

export async function entries(){
	const articles = fs.readdirSync("src/routes").filter(path => path.endsWith(".md") && !path.startsWith("+page"));
	return articles.map(path => { return { slug: path.slice(0, -3) }});
}
