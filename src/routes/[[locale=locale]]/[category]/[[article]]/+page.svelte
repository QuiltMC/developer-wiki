<script lang="ts">
	import { Localized, Overlay, getFluentContext } from "@nubolab-ffwd/svelte-fluent";

	import { localesDir } from "$l10n";
	import current from "$lib/current.svelte";

	const { localize } = getFluentContext();

	let { data } = $props();
</script>

<svelte:head>
	<title>{localize(data.title)}</title>
</svelte:head>

{#if data.draft}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<Overlay
					id="draft-notice"
					args={{ wiki_source: "https://github.com/QuiltMC/developer-wiki" }}
				>
					{#snippet children()}
						<a data-l10n-name="link" href="https://github.com/QuiltMC/developer-wiki">
							<!-- Translated content is inserted here -->
						</a>
					{/snippet}
				</Overlay>
			</h1>
		</div>
	</article>
{/if}

{#if !data.translated}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<Localized
					id="article-not-translated-notice"
					args={{
						current_locale: localize(current.locale),
						fallback_locale: localize(data.fallbackLocale)
					}}
				/>
			</h1>
		</div>
	</article>
{/if}

{#if data.content}
	{#if !data.translated}
		<!-- Display the article in the correct direction for the fallback locale -->
		<div dir={localesDir[data.fallbackLocale]}>
			<data.content />
		</div>
	{:else}
		<data.content />
	{/if}
{/if}
