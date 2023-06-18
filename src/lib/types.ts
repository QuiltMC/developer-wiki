export interface Page {
	slug: string;
	title: string;
}

export interface Category {
	name: string;
	pages: Page[];
}

export interface Post {
	metadata: {
		title: string;
		categories: string[];
	};
	default: Promise<unknown>;
}
