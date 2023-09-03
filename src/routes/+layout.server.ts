import YAML from "yaml";

import fs from "fs";

import type { Category, GlobImport } from "$lib/types";
import type { LayoutServerLoadEvent } from "./$types";

export async function load({ params }: LayoutServerLoadEvent) {
	const articles: GlobImport = import.meta.glob("$wiki/**/*.md");

	const categories: Category[] = [];

	for (const [path, resolver] of Object.entries(articles)) {
		const post = await resolver();

		if (post instanceof Function) continue; // The resolver can return itself, we need to filter that out

		const [, , category, slug] = path.split("/");
		const cat = categories.find((cat) => cat.slug === category);
		if (!post.metadata.index) post.metadata.index = 0;
		const page = {
			slug: slug.slice(0, -3),
			index: post.metadata.index,
			title: post.metadata.title,
		};

		const metadata = YAML.parse(
			fs.readFileSync(`${process.cwd()}/wiki/${category}/+category.yml`, "utf-8")
		);
		const categoryName = metadata.name;

		if (cat) {
			cat.pages.push(page);
		} else {
			categories.push({ name: categoryName, slug: category, pages: [page] });
		}
	}

	for (let category of categories) {
		category.pages.sort((a, b) => a.index - b.index);
	}

	return { category: params.category, slug: params.slug, categories };
}
