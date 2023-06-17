<script lang="ts">
	import Footer from "$lib/Footer.svelte";
	import Header from "$lib/Header.svelte";
	import HtmlHead from "$lib/HtmlHead.svelte";
</script>

<HtmlHead />

<Header />
<slot />
<Footer />
