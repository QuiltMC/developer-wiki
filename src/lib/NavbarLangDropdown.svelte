<script lang="ts">
	import { Localized } from "@nubolab-ffwd/svelte-fluent";

	import { browser } from "$app/environment";
	import { page } from "$app/stores";
	import { extractRoute, supportedLocales } from "$l10n";

	let is_dropdown_active = $state(false);
	function toggleNavbar() {
		is_dropdown_active = !is_dropdown_active;
	}

	let dropdown_toggle: HTMLDivElement | undefined = $state();
	function onblur(event: FocusEvent) {
		// Check that the newly focused element is not a child
		// of the dropdown toggle element so that users
		// can actually select a locale
		if (
			!event.relatedTarget ||
			!(event.relatedTarget instanceof Node) ||
			!dropdown_toggle?.contains(event.relatedTarget)
		) {
			is_dropdown_active = false;
		}
	}

	const currentRoute = $derived(extractRoute($page.url.pathname));
</script>

<div
	class="navbar-item has-dropdown"
	class:is-active={is_dropdown_active}
	class:is-hoverable={!browser}
	aria-expanded={browser ? is_dropdown_active : undefined}
	aria-labelledby="lang-dropdown-title"
	onclick={toggleNavbar}
	onkeypress={(event) => {
		if (event.key === "Enter") {
			toggleNavbar();
		}
	}}
	{onblur}
	bind:this={dropdown_toggle}
	role="button"
	tabindex="0"
>
	<span class="navbar-link has-icon">
		<span class="icon"><span class="i-fa6-solid-language i-2xl"></span></span>
		<span class="is-hidden-desktop" id="lang-dropdown-title">
			<Localized id="language" />
		</span>
	</span>

	<div class="navbar-dropdown">
		{#each supportedLocales as locale}
			<a
				class="navbar-item"
				href={`/${locale}${currentRoute}`}
				data-sveltekit-preload-data="false"
				{onblur}
			>
				<Localized id={locale} />
			</a>
		{/each}
	</div>
</div>

<style>
	/* Aligns the language icon with the
		 text on smaller screens */
	.navbar-link.has-icon {
		display: flex;
		align-items: center;
	}
	/* Removes the set height and width
		 of the navbar link's icon */
	.icon {
		height: unset;
		width: unset;
	}
</style>
