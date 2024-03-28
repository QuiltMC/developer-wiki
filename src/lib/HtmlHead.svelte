<script lang="ts">
	import prism_dark from "prismjs/themes/prism-tomorrow.min.css?url";
	import prism_light from "prismjs/themes/prism.min.css?url";
	import styles_dark from "quilt-bulma/dist/style-dark.min.css?url";
	import styles_light from "quilt-bulma/dist/style-light.min.css?url";
	import { onMount } from "svelte";

	let userPrefersDarkMode: boolean = false;
	onMount(function () {
		userPrefersDarkMode =
			window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
		window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", (e) => {
			userPrefersDarkMode = e.matches;
		});
	});
</script>

<svelte:head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />

	{#if userPrefersDarkMode}
		<meta name="darkreader-lock" />
	{/if}

	<link rel="preconnect" href="https://quiltmc.org" />
	<link rel="preconnect" href="https://fonts.bunny.net" />
	<link rel="preconnect" href="https://kit.fontawesome.com" />
	<link rel="preconnect" href="https://ka-p.fontawesome.com" />

	<link
		rel="icon"
		type="image/png"
		sizes="32x32"
		href="https://quiltmc.org/favicon/favicon-32x32.png"
	/>
	<link
		rel="icon"
		type="image/png"
		sizes="16x16"
		href="https://quiltmc.org/favicon/favicon-16x16.png"
	/>
	<link rel="shortcut icon" href="https://quiltmc.org/favicon/favicon.ico" />

	<link rel="stylesheet" href={styles_dark} media="(prefers-color-scheme:dark)" />
	<link rel="stylesheet" href={styles_light} media="(prefers-color-scheme:light)" />

	<style global>
		.token.number {
			background-color: initial;
			border-radius: initial;
			display: initial;
			font-size: 1em;
			margin-right: initial;
			padding: initial;
			text-align: left;
			vertical-align: initial;
		}
		@media screen and (min-width: 1260px) {
			.table-of-contents {
				float: right;
				width: 340px;
				margin-left: 0.9rem;
			}
		}
	</style>
	<link rel="stylesheet" href={prism_dark} media="(prefers-color-scheme:dark)" />
	<link rel="stylesheet" href={prism_light} media="(prefers-color-scheme:light)" />
</svelte:head>
