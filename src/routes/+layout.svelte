<script lang="ts">
	import { onMount } from "svelte";

	import Footer from "$lib/Footer.svelte";
	import Header from "$lib/Header.svelte";
	import HtmlHead from "$lib/HtmlHead.svelte";
	import Sidebar from "$lib/Sidebar.svelte";

	onMount(() => {
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
<input id="language-switcher" type="checkbox" class="is-hidden" />
<input id="toggle-navbar" type="checkbox" class="is-hidden" />
<input id="toggle-sidebar" type="checkbox" class="is-hidden" />

<Header />
<div class="section" style="padding: 0.5rem;">
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				Notice: This website is still under development. Please report any issues at
				<a href="https://github.com/QuiltMC/developer-wiki">https://github.com/QuiltMC/developer-wiki</a>
			</h1>
		</div>
	</article>
	<div class="columns">
		<Sidebar categories={data.categories} url={data.category + "/" + data.slug} />

		<slot />
	</div>
</div>
<Footer />
