<script lang="ts">
	import { t } from "$lib/translations/index.js";
	import SvelteMarkdown from "svelte-markdown";

	export let data;
</script>

<svelte:head>
	<title>{$t(data.title)}</title>
</svelte:head>

{#if data.warningMessage}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<SvelteMarkdown
					source={$t(data.warningMessage.key, { placeholder: data.warningMessage.placeholder })}
				/>
			</h1>
		</div>
	</article>
{/if}

{#if data.content}
	<svelte:component this={data.content} />
{/if}
