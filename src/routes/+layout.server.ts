import YAML from "yaml";

import fs from "fs";

import type { Category, Page } from "$lib/types";
import type { LayoutServerLoadEvent } from "../$types";

export async function load({ params }: LayoutServerLoadEvent) {
	const wiki_path = `${process.cwd()}/wiki/`;

	const categories: Category[] = fs
		// get the name of each file in the wiki folder
		.readdirSync(wiki_path)
		// remove files with extensions (not directories)
		.filter((file_name) => !file_name.match(/.+\..+/))
		.map((category_slug) => {
			const pages: Page[] = fs
				// get the name if each file in this category's folder
				.readdirSync(`${wiki_path}${category_slug}/`)
				// remove files with extensions (not directories)
				.filter((file_name) => !file_name.match(/.+\..+/))
				.map((page_slug) => {
					// get the metadata of each file from their yaml file
					const page_metadata = YAML.parse(
						fs.readFileSync(`${wiki_path}${category_slug}/${page_slug}/+page.yml`, "utf-8")
					);

					return {
						slug: page_slug,
						index:
							page_metadata.index && typeof page_metadata.index === "number"
								? page_metadata.index
								: Number.MAX_SAFE_INTEGER, // Put the pages with no index last
						title: page_metadata.title,
						draft: page_metadata.draft || false
					};
				})
				// exlude draft pages
				.filter((page) => !page.draft)
				.sort((a, b) => a.index - b.index);

			// get the metadata of each category from their yaml file
			const category_metadata = YAML.parse(
				fs.readFileSync(`${wiki_path}${category_slug}/+category.yml`, "utf-8")
			);

			return {
				slug: category_slug,
				index:
					category_metadata.index && typeof category_metadata.index === "number"
						? category_metadata.index
						: Number.MAX_SAFE_INTEGER, // Put the categories with no index last
				name: category_metadata.name,
				pages: pages
			};
		})
		// exckude categories with no pages
		.filter((category) => category.pages.length)
		.sort((a, b) => a.index - b.index);

	return { category: params.category, slug: params.slug, categories };
}
