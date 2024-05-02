<script lang="ts">
	import { onMount } from "svelte";

	import SvelteMarkdown from "svelte-markdown";

	import Footer from "$lib/Footer.svelte";
	import HtmlHead from "$lib/HtmlHead.svelte";
	import Header from "$lib/Header.svelte";
	import Sidebar from "$lib/Sidebar.svelte";
	import NoPreloadLink from "$lib/svelte-markdown/NoPreloadLink.svelte";
	import { locale, t } from "$lib/translations/index";
	import { current_route } from "$lib/stores.js";

	onMount(async () => {
		const lowContrast = document.getElementById("low-contrast") as HTMLInputElement;
		const lowContrastState = localStorage.getItem("lowContrast");

		if (lowContrastState) {
			lowContrast.checked = lowContrastState == "true";
		}

		lowContrast.onchange = () => {
			localStorage.setItem("lowContrast", lowContrast.checked.toString());
		};
	});

	export let data;
</script>

<HtmlHead />

<input id="low-contrast" type="checkbox" class="is-hidden" />
<input id="toggle-navbar" type="checkbox" class="is-hidden" />
<input id="toggle-sidebar" type="checkbox" class="is-hidden" />

<Header />

<div class="section" style="padding: 0.5rem;">
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<SvelteMarkdown
					source={$t("application.dev-notice", {
						placeholder: "https://github.com/QuiltMC/developer-wiki"
					})}
				/>
			</h1>
		</div>
	</article>
	{#if $locale !== "en"}
		<article class="message is-danger mx-3 mt-3 mb-3">
			<div class="message-body has-text-centered">
				<h1>
					<SvelteMarkdown
						source={$t("application.translation-notice", { placeholder: `/en${$current_route}` })}
						renderers={{ link: NoPreloadLink }}
					/>
				</h1>
			</div>
		</article>
	{/if}
	<div class="columns">
		<Sidebar categories={data.categories} url={data.category + "/" + data.slug} />

		<main class="column container">
			<slot />
		</main>
	</div>
</div>
<Footer />
