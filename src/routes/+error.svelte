<script lang="ts">
	import { getFluentContext, Localized, Overlay } from "@nubolab-ffwd/svelte-fluent";

	import { page } from "$app/stores";
	import { isLocale } from "$l10n";
	import current from "$lib/current.svelte";

	const { localize } = getFluentContext();

	let is_locale_in_route = $state(false);
	$effect(() => {
		// Atempts to get the locale from the pathname
		let path_match = $page.url.pathname.match(/^\/([^/]+)\/.+$/);

		if (path_match && isLocale(path_match[1])) {
			current.locale = path_match[1];

			is_locale_in_route = true;
		}
	});
</script>

<svelte:head>
	<title>{localize("error-title", { error_code: $page.status })}</title>
</svelte:head>

<h1 class="title">
	<Localized id="error-title" args={{ error_code: $page.status }} />
</h1>

{#if $page.status === 404}
	{#if is_locale_in_route}
		<Overlay
			id="article-not-found-error"
			args={{ article_path: $page.url.pathname.replace(`/${current.locale}`, "") }}
		/>
	{:else}
		<Overlay id="not-found-error" args={{ page_path: $page.url.pathname }} />
	{/if}
{:else}
	<p>
		<Localized id="server-error" />
	</p>
{/if}
