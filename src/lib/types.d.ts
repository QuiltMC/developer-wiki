import type { SvelteComponentTyped } from "svelte";

export interface Page {
	slug: string;
	index: number;
	title: string;
	draft: boolean;
}

export interface Category {
	slug: string;
	index: number;
	name: string;
	pages: Page[];
}

export interface Post {
	default: Promise<SvelteComponentTyped>;
}

export type PostResolver = () => Promise<Post | PostResolver>;

export type GlobImport = Record<string, PostResolver>;
