<script lang="ts">
	import { currentLocale, isRtl } from "$l10n";
	import { Localized, Overlay, localize } from "@nubolab-ffwd/svelte-fluent";

	export let data;
</script>

<svelte:head>
	<title>{$localize(data.title)}</title>
</svelte:head>

{#if data.draft}
	<article class="message is-danger mx-3 mt-3 mb-3">
		<div class="message-body has-text-centered">
			<h1>
				<Overlay
					id="draft-notice"
					args={{ wiki_source: "https://github.com/QuiltMC/developer-wiki" }}
				>
					<a data-l10n-name="link" href="https://github.com/QuiltMC/developer-wiki">
						<!-- Translated content is inserted here -->
					</a>
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
						current_locale: $localize($currentLocale),
						fallback_locale: $localize(data.fallbackLocale)
					}}
				/>
			</h1>
		</div>
	</article>
{/if}

{#if data.content}
	{#if !data.translated}
		<!-- Display the article in the correct direction for the fallback locale -->
		<div dir={isRtl(data.fallbackLocale) ? "rtl" : "ltr"}>
			<svelte:component this={data.content} />
		</div>
	{:else}
		<svelte:component this={data.content} />
	{/if}
{/if}
