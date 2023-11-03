import lang from "./lang.json";

import i18n from "@sveltekit-i18n/base";
import parser from "@sveltekit-i18n/parser-default";

import type { Config } from "@sveltekit-i18n/parser-default";

export const config: Config<{ placeholder?: string }> = {
	initLocale: "en",
	fallbackLocale: "en",
	parser: parser(),
	translations: {
		en: { lang },
		fr: { lang }
	},
	loaders: [
		{
			locale: "en",
			key: "application",
			loader: async () => (await import("./en/application.json")).default
		},
		{
			locale: "en",
			key: "error",
			loader: async () => (await import("./en/error.json")).default
		},
		{
			locale: "en",
			key: "wiki",
			loader: async () => (await import("./en/wiki.json")).default
		},
		{
			locale: "fr",
			key: "application",
			loader: async () => (await import("./fr/application.json")).default
		},
		{
			locale: "fr",
			key: "error",
			loader: async () => (await import("./fr/error.json")).default
		},
		{
			locale: "fr",
			key: "wiki",
			loader: async () => (await import("./fr/wiki.json")).default
		}
	]
};

export const { t, loading, locales, locale, loadTranslations } = new i18n(config);
