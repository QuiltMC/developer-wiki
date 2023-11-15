<script lang="ts">
	import SvelteMarkdown from "svelte-markdown";

	import { t } from "$lib/translations";
	import { page } from "$app/stores";
	import { invalidateAll } from "$app/navigation";
	import { browser, dev } from "$app/environment";

	// Reload the page content on route change
	// (when the locale changes)
	// (this is only needed in dev mode)
	let old_pathname = "";
	$: if (dev && browser) {
		if (!old_pathname) old_pathname = $page.url.pathname;
		else if (old_pathname !== $page.url.pathname) {
			old_pathname = $page.url.pathname;
			invalidateAll();
		}
	}

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
