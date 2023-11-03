import sectionize from "@hbsnow/rehype-sectionize";
import toc from "@jsdevtools/rehype-toc";
import adapter from "@sveltejs/adapter-static";
import { vitePreprocess } from "@sveltejs/kit/vite";
import { mdsvex } from "mdsvex";
import rehypeAutolinkHeadings from "rehype-autolink-headings";
import rehypeRewrite from "rehype-rewrite";
import rehypeSlug from "rehype-slug";

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
				[
					rehypeAutolinkHeadings,
					{
						behavior: "append",
						content: {
							type: "element",
							tagName: "span",
							properties: { className: "icon header-anchor-container pl-3" },
							children: [
								{
									type: "element",
									tagName: "i",
									properties: {
										className: "header-anchor fas fa-lg fa-link has-text-link is-size-5"
									}
								}
							]
						}
					}
				],
				[sectionize, { properties: { className: "" } }],
				[
					toc,
					{
						nav: false,
						customizeTOC: (toc) => {
							toc.properties.className = "menu-list";
							return {
								type: "element",
								tagName: "div",
								properties: {
									className: "box is-hidden-mobile is-sticky sidebar table-of-contents"
								},
								children: [
									{
										type: "element",
										tagName: "aside",
										properties: { className: "menu" },
										children: [
											{
												type: "element",
												tagName: "p",
												properties: { className: "menu-label" },
												children: [{ type: "text", value: "Contents" }]
											},
											toc
										]
									}
								]
							};
						}
					}
				],
				[
					rehypeRewrite,
					{
						rewrite: (node, index, parent) => {
							if (
								node.type == "element" &&
								node.tagName == "ol" &&
								(node.properties.className == "menu-list" ||
									(node.properties.className && node.properties.className.includes("toc-level")))
							) {
								node.tagName = "ul";
							}
							if (node.type === "element" && node.tagName === "section") {
								if (
									node.properties &&
									node.properties.dataHeadingRank &&
									node.properties.dataHeadingRank === 1
								) {
									node.properties.className = "content";
								} else {
									parent.children.push(...node.children); // TODO: quick fix for heading padding bug, moves all child elements of the section in to the main section, and then ereases all section data (replacing them with divs), in order to try and keep the tree clean
									node.children = [];
									node.properties = {};
									node.tagName = "div";
								}
							}
						}
					}
				]
			]
		})
	]
};
