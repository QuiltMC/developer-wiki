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
	draft: boolean;
	pages: Page[];
}
