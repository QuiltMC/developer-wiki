<script>
	import Sidebar from '$lib/Sidebar.svelte';

	export let data;
	console.log(data);
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

<section class="section">
	<main class="container">
		<div class="columns">
			<Sidebar categories={data.categories} slug={data.slug} />

			<div class="content column">
				<h1>{data.title}</h1>
				<h3>Categories: {data.categories.map(cat => cat.name)}</h3>

				<svelte:component this={data.content} />
			</div>
		</div>
	</main>
</section>
