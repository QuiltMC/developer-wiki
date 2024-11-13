import fs from "node:fs";
import process from "node:process";

import { supportedLocales } from "$l10n";

// Get the available locales for the landing page
export async function load() {
	const landing_page_path = `${process.cwd()}/wiki/landing-page/`;

	const available_locales = supportedLocales.filter((locale) =>
		fs.existsSync(`${landing_page_path}${locale}.md`)
	);

	return {
		availableLocales: available_locales
	};
}
