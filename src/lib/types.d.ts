import type { Locale } from "$l10n";

export interface Page {
	slug: string;
	index: number;
	draft: boolean;
	availableLocales: Locale[];
}

export interface Category {
	slug: string;
	index: number;
	draft: boolean;
	pages: Page[];
}
