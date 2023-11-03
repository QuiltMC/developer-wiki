<script lang="ts">
	import SvelteMarkdown from "svelte-markdown";

	import { t } from "$lib/translations";

	export let data;
</script>

<svelte:head>
	<title>{$t(data.title)}</title>
</svelte:head>

{#if data.isDraft}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<SvelteMarkdown
					source={$t("application.draft-notice", {
						placeholder: "https://github.com/QuiltMC/developer-wiki"
					})}
				/>
			</h1>
		</div>
	</article>
{/if}

{#if data.content}
	<svelte:component this={data.content} />
{/if}
