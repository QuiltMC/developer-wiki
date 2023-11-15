<script lang="ts">
	import { goto } from "$app/navigation";
	import { t, locales, locale } from "$lib/translations";

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
		// Removes the locale from the route
		const current_route = window.location.pathname.replace(
			new RegExp(`^/${locale.get()}(/.+)?$`),
			"$1"
		);

		goto(`/${selected_locale}${current_route}`);
	};
</script>

<div
	class="navbar-item has-dropdown {is_dropdown_active ? 'is-active' : ''}"
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
			<span
				class="navbar-item is-clickable"
				on:click={() => selectLocale(locale)}
				on:keypress={(event) => {
					if (event.key === "Enter") {
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
