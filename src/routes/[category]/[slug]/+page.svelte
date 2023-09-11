<script lang="ts">
	export let data;
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

<main class="column container">
	<svelte:component this={data.content} />
</main>
