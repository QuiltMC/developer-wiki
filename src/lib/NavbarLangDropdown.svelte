<script lang="ts">
	import { browser } from '$app/environment';
	import { locale, locales, t } from '$lib/translations';
	import { onDestroy } from 'svelte';
	import type { Unsubscriber } from 'svelte/store';

	let unsubscriber: Unsubscriber;
	onDestroy(() => unsubscriber && unsubscriber());

	if (browser) {
		const user_locale: string = window.localStorage.getItem('lang') || window.navigator.language;
		if ($locales.includes(user_locale)) {
			$locale = user_locale;
		}

		unsubscriber = locale.subscribe((locale) => localStorage.setItem('lang', locale));
	}

	let is_dropdown_active = false;
	function toggleNavbar() {
		is_dropdown_active = !is_dropdown_active;
	}

	let dropdown_toggle: HTMLDivElement;
	function handleBlur(event: FocusEvent) {
		// Check that the newly focused element is not a child
		// of the dropdown toggle element so that users
		// can actually select a locale
		if (
			!event.relatedTarget ||
			!(event.relatedTarget instanceof Node) ||
			!dropdown_toggle.contains(event.relatedTarget)
		) {
			is_dropdown_active = false;
		}
	}

	const selectLocale = (selected_locale: string) => {
		$locale = selected_locale;
	};
</script>

<div
	class="navbar-item has-dropdown {is_dropdown_active ? 'is-active' : ''}"
	on:click={toggleNavbar}
	on:keypress={(event) => {
		if (event.key === 'Enter') {
			toggleNavbar();
		}
	}}
	on:blur={handleBlur}
	bind:this={dropdown_toggle}
	role="button"
	tabindex="0"
>
	<span class="navbar-link has-icon">
		<span class="icon"><i class="fas fa-language fa-xl" /></span>
		<span class="is-hidden-desktop">{$t('application.lang-dropdown.language')}</span>
	</span>

	<div class="navbar-dropdown">
		{#each $locales as locale}
			<span
				class="navbar-item is-clickable"
				on:click={() => selectLocale(locale)}
				on:keypress={(event) => {
					if (event.key === 'Enter') {
						selectLocale(locale);
					}
				}}
				on:blur={handleBlur}
				role="button"
				tabindex="0"
			>
				{$t(`lang.${locale}`)}
			</span>
		{/each}
	</div>
</div>
