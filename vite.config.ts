import svelteFluent from "@nubolab-ffwd/svelte-fluent/vite";
import { sveltekit } from "@sveltejs/kit/vite";
import extractorSvelte from "@unocss/extractor-svelte";
import UnoCSS from "unocss/vite";
import { defineConfig } from "vite";

export default defineConfig({
	plugins: [
		UnoCSS({
			extractors: [extractorSvelte()]
		}),
		svelteFluent(),
		sveltekit()
	],
	server: {
		fs: {
			allow: ["."]
		}
	}
});
