import adapter from '@sveltejs/adapter-static';
import { mdsvex } from 'mdsvex';

export default {
	kit: {
		adapter: adapter()
	},

	extensions: ['.svelte', '.md'],

	preprocess: [
		mdsvex({
			extensions: ['.md']
		})
	]
};
