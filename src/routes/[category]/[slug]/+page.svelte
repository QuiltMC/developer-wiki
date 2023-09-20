<script lang="ts">
	export let data;
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

<main class="column container">
	<!-- TODO: This looks ugly on draft pages (when linked to them, alternative is to disable id checks on links) -->
	{#if data.isDraft}<svelte:component this={data.draft}/> {/if}
	<svelte:component this={data.content} />
</main>
