import adapter from "@sveltejs/adapter-static";
import { vitePreprocess } from "@sveltejs/kit/vite";
import { mdsvex } from "mdsvex";
import rehypeAutolinkHeadings from 'rehype-autolink-headings';
import rehypeSlug from 'rehype-slug';

export default {
	kit: {
		adapter: adapter(),

		alias: {
			$wiki: "wiki"
		}
	},

	extensions: [".svelte", ".md"],

	preprocess: [
		vitePreprocess(),

		mdsvex({
			extensions: [".md"],
			rehypePlugins: [
				rehypeSlug,
				rehypeAutolinkHeadings
			]
		})
	]
};
