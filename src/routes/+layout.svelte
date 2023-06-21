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
<main class="section">
	<div class="columns">
		<Sidebar categories={data.categories} url={data.category + "/" + data.slug} />

		<slot />
	</div>
</main>
<Footer />
