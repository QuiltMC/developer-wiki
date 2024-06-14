<script lang="ts">
	import { currentRoute } from "./stores";

	import { Localized } from "@nubolab-ffwd/svelte-fluent";

	import { browser } from "$app/environment";
	import { supportedLocales } from "$l10n";

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
		<span class="icon"><span class="i-fa6-solid-language i-2xl" /></span>
		<span class="is-hidden-desktop">
			<Localized id="language" />
		</span>
	</span>

	<div class="navbar-dropdown">
		{#each supportedLocales as locale}
			<a
				class="navbar-item"
				href={`/${locale}${$currentRoute}`}
				data-sveltekit-preload-data="false"
				on:blur={handleBlur}
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
