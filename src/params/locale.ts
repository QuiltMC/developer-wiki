import type { ParamMatcher } from "@sveltejs/kit";

import { locales } from "$lib/translations";

export const match: ParamMatcher = (param) => {
	return locales.get().includes(param);
};
