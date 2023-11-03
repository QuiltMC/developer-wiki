<script>
	import { page } from "$app/stores";
	import { t } from "$lib/translations";
	import SvelteMarkdown from "svelte-markdown";
</script>

<svelte:head>
	<title>{$t("error.title", { placeholder: `${$page.status}` })}</title>
</svelte:head>

<h1 class="title">{$t("error.title", { placeholder: `${$page.status}` })}</h1>
<SvelteMarkdown source={$page.error?.message} />
