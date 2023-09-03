import adapter from "@sveltejs/adapter-static";
import { vitePreprocess } from "@sveltejs/kit/vite";
import { mdsvex } from "mdsvex";
import rehypeAutolinkHeadings from 'rehype-autolink-headings';
import rehypeSlug from 'rehype-slug';
import toc from '@jsdevtools/rehype-toc';
import sectionize from '@hbsnow/rehype-sectionize';
import { children } from "svelte/internal";

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
				rehypeAutolinkHeadings,
				[sectionize, {properties: {className: "column content"}}],
				[toc, {
					nav: false,
					customizeTOC: (toc) =>{
						toc.properties.className = "menu-list";
						function change_tagname(node) {
							if (node.tagName == "ol") node.tagName = "ul";
							if (node.children) node.children.forEach(change_tagname);
						}
						change_tagname(toc);
						return {
							type: 'element',
							tagName: 'div',
							properties: {className: "column is-narrow"},
							children: [{
								type: 'element',
								tagName: 'div',
								properties: {className: "box is-hidden-mobile is-sticky sidebar"},
								children: [{
									type: 'element',
									tagName: 'aside',
									properties: {className: "menu"},
									children: [{
										type: 'element',
										tagName: 'p',
										properties: {className: 'menu-label'},
										children: [{type: 'text', value: 'Contents'}]
									},
									toc
								]}]
							}]
						};
					}
				}]
			]
		})
	]
};
