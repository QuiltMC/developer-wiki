<script lang="ts">
	export let data;
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

<main class="column ">
	<div class="columns" style="flex-direction: row-reverse;">
		<svelte:component this={data.content} />
	</div>
</main>
