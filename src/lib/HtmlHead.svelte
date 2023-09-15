<script lang="ts">
	import prism_dark from "prismjs/themes/prism-tomorrow.min.css?url";
	import prism_light from "prismjs/themes/prism.min.css?url";
	import styles_dark from "quilt-bulma/dist/style-dark.min.css?url";
	import styles_light from "quilt-bulma/dist/style-light.min.css?url";
</script>

<svelte:head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />

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

	<script async src="https://kit.fontawesome.com/b1b70f724e.js" crossorigin="anonymous"></script>
	<noscript>
		<link href="https://quiltmc.org/assets/fontawesome/css/all.min.css" rel="stylesheet" />
	</noscript>

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
			}
		}
	</style>
	<link rel="stylesheet" href={prism_dark} media="(prefers-color-scheme:dark)" />
	<link rel="stylesheet" href={prism_light} media="(prefers-color-scheme:light)" />
</svelte:head>
