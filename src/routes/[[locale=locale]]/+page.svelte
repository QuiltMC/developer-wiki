<script lang="ts">
	import { t } from "$lib/translations";

	export let data;
</script>

<svelte:head>
	<title>{$t("application.title")}</title>
</svelte:head>

{#if data.content}
	<svelte:component this={data.content} />
{/if}
