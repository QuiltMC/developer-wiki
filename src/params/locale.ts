import type { ParamMatcher } from "@sveltejs/kit";

import { isLocale } from "$l10n";

export const match: ParamMatcher = (param) => {
	return isLocale(param);
};
