<script lang="ts">
	import { browser } from "$app/environment";
	import { locale, locales, t } from "$lib/translations";
	import { onDestroy } from "svelte";
	import type { Unsubscriber } from "svelte/store";

	const on_lang_item_action = (selected_locale: string) => ($locale = selected_locale);

	let unsubscriber: Unsubscriber;

	if (browser) {
		const user_locale: string = window.localStorage.getItem("lang") || window.navigator.language;
		if ($locales.includes(user_locale)) {
			$locale = user_locale;
		}

		locale.subscribe((locale) => localStorage.setItem("lang", locale));
	}

	onDestroy(() => unsubscriber && unsubscriber());
</script>

<div class="navbar-item has-dropdown is-hoverable">
	<span class="navbar-link has-icon">
		<span class="icon"><i class="fas fa-language fa-xl" /></span>
		<span class="is-hidden-desktop">{$t("application.lang-dropdown.language")}</span>
	</span>

	<div class="navbar-dropdown">
		{#each $locales as value}
			<span
				class="navbar-item is-clickable"
				on:click={() => on_lang_item_action(value)}
				on:keypress={() => on_lang_item_action(value)}
				role="button"
				tabindex="0"
			>
				{$t(`lang.${value}`)}
			</span>
		{/each}
	</div>
</div>
