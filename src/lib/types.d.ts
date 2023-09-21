import type { SvelteComponentTyped } from "svelte";

export interface Page {
	slug: string;
	index: number;
	title: string;
}

export interface Category {
	name: string;
	slug: string;
	index: number;
	pages: Page[];
}

export interface Post {
	metadata: {
		title: string;
		index: number?;
		draft: boolean?;
	};
	default: Promise<SvelteComponentTyped>;
}

export type PostResolver = () => Promise<Post | PostResolver>;

export type GlobImport = Record<string, PostResolver>;
