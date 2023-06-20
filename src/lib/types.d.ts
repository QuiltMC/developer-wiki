import type { SvelteComponentTyped } from "svelte";

export interface Page {
	slug: string;
	title: string;
}

export interface Category {
	name: string;
	slug: string;
	pages: Page[];
}

export interface Post {
	metadata: {
		title: string;
		category: string;
	};
	default: Promise<SvelteComponentTyped>;
}

export type PostResolver = () => Promise<Post | PostResolver>;

export type GlobImport = Record<string, PostResolver>;
