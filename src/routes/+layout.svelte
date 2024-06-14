<script lang="ts">
	import { FluentProvider } from "@nubolab-ffwd/svelte-fluent";
	import { Overlay } from "@nubolab-ffwd/svelte-fluent";
	import { onMount } from "svelte";

	import { currentLocale, currentBundle } from "$l10n";
	import Footer from "$lib/Footer.svelte";
	import Header from "$lib/Header.svelte";
	import HtmlHead from "$lib/HtmlHead.svelte";
	import Sidebar from "$lib/Sidebar.svelte";
	import { currentRoute } from "$lib/stores.js";

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

<!-- Provides the fluent bundle for all children elements so that they can use the fluent translations -->
<FluentProvider bundles={[$currentBundle]}>
	<!-- Needed to tell unocss that the link icon is indeed used -->
	<div class="i-fa6-solid-link is-hidden" />

	<HtmlHead />

	<input id="low-contrast" type="checkbox" class="is-hidden" />
	<input id="toggle-navbar" type="checkbox" class="is-hidden" />
	<input id="toggle-sidebar" type="checkbox" class="is-hidden" />

	<Header />

	<div class="section p-12">
		<article class="message is-danger mx-3 mt-3 mb-3">
			<div class="message-body has-text-centered">
				<h1>
					<Overlay
						id="dev-notice"
						args={{ wiki_source: "https://github.com/QuiltMC/developer-wiki" }}
					>
						<a data-l10n-name="link" href="https://github.com/QuiltMC/developer-wiki">
							<!-- Translated content is inserted here -->
						</a>
					</Overlay>
				</h1>
			</div>
		</article>
		{#if $currentLocale !== "en"}
			<article class="message is-danger mx-3 mt-3 mb-3">
				<div class="message-body has-text-centered">
					<h1>
						<Overlay id="translation-notice">
							<a
								data-l10n-name="link"
								href={`/en${$currentRoute}`}
								data-sveltekit-preload-data="false"
							>
								<!-- Translated content is inserted here -->
							</a>
						</Overlay>
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
</FluentProvider>
