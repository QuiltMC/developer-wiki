import { browser } from "$app/environment";
import { invalidateAll } from "$app/navigation";
import { locale } from "$lib/translations/index.js";

locale.subscribe(() => {
	if (browser) {
		invalidateAll();
	}
});
