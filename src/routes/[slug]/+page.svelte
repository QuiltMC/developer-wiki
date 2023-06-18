<script lang="ts">
	export let data;
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

<main class="content column">
	<h1>{data.title}</h1>
	<h3>Categories: {data.categories.map((cat) => cat.name)}</h3>

	<svelte:component this={data.content} />
</main>
