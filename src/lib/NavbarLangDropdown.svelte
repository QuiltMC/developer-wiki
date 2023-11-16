<script lang="ts">
	import { browser } from "$app/environment";
	import { page } from "$app/stores";
	import { t, locales, locale } from "$lib/translations";

	// Removes the current locale from the current route
	// (only if the current route does contain the locale)
	let current_route = "";
	$: {
		const routeExtractor = new RegExp(`^/${$locale}(/.+)?$`);

		if (routeExtractor.test($page.url.pathname)) {
			current_route = $page.url.pathname.replace(routeExtractor, "$1");
		}
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
</script>

<div
	class="navbar-item has-dropdown"
	class:is-active={is_dropdown_active}
	class:is-hoverable={!browser}
	on:click={toggleNavbar}
	on:keypress={(event) => {
		if (event.key === "Enter") {
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
		<span class="is-hidden-desktop">{$t("application.lang-dropdown.language")}</span>
	</span>

	<div class="navbar-dropdown">
		{#each $locales as locale}
			<a
				class="navbar-item"
				href={`/${locale}${current_route}`}
				data-sveltekit-preload-data="tap"
				on:blur={handleBlur}
			>
				{$t(`lang.${locale}`)}
			</a>
		{/each}
	</div>
</div>
