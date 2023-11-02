<script lang="ts">
	export let data;
</script>

<svelte:head>
	<title>{data.title}</title>
</svelte:head>

{#if data.isDraft}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				You were linked to a page in the wiki that is planned, but not yet implemented. If you want
				to help building out this wiki, go and take a look at
				<a href="https://github.com/QuiltMC/developer-wiki">
					https://github.com/QuiltMC/developer-wiki
				</a>
			</h1>
		</div>
	</article>
{/if}
<svelte:component this={data.content} />
