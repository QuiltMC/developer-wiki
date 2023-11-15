<script lang="ts">
	import NavbarLangDropdown from "./NavbarLangDropdown.svelte";
	import { locale } from "$lib/translations/index";
</script>

<nav class="navbar is-dark" aria-label="main navigation" data-controller="navbar">
	<div class="navbar-brand no-style">
		<a class="navbar-item navbar-logo" href={`/${$locale}`}>
			<img src="https://quiltmc.org/assets/img/logo.svg" alt="Quilt Logo" height="28" width="28" />

			<span class="ml-2">DevWiki</span>
		</a>

		<label
			class="navbar-item px-2 has-tooltip-arrow has-tooltip-bottom"
			for="low-contrast"
			data-tooltip="Toggle Low Contrast"
		>
			<span id="theme-normal-contrast">
				<i class="fas fa-circle-half-stroke fa-xl" />
			</span>

			<span id="theme-low-contrast">
				<i class="fal fa-circle-half-stroke fa-xl" />
			</span>
		</label>

		<a
			class="navbar-item px-2 has-tooltip-arrow has-tooltip-bottom"
			href="https://quiltmc.org/"
			data-tooltip="Website Home"
		>
			<i class="fas fa-home fa-lg" />
		</a>

		<label class="navbar-burger" for="toggle-navbar" aria-label="menu">
			<span aria-hidden="true" />
			<span aria-hidden="true" />
			<span aria-hidden="true" />
		</label>
	</div>

	<div class="navbar-menu" data-navbar-target="menu">
		<div class="navbar-start">
			<NavbarLangDropdown />
		</div>

		<div class="navbar-end">
			<a
				href="https://github.com/QuiltMC/developer-wiki"
				class="navbar-item has-icon"
				target="_blank"
			>
				<span class="icon"><i class="fab fa-github fa-xl" /></span>
				<span class="is-hidden-desktop">GitHub</span>
			</a>
		</div>
	</div>
</nav>
