import type { ParamMatcher } from "@sveltejs/kit";

import { locales } from "$lib/translations/index";

export const match: ParamMatcher = (param) => {
	return locales.get().includes(param);
};
