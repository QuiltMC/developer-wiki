<script lang="ts">
	import { page } from "$app/stores";
	import { t, locales, defaultLocale, setLocale } from "$lib/translations";
	import SvelteMarkdown from "svelte-markdown";

	let locale_in_route: string | undefined;
	$: {
		// Atempts to get the locale from the pathname
		locale_in_route = $locales.find((locale) =>
			new RegExp(`^/${locale}/.+$`).test($page.url.pathname)
		);

		// Set the locale from the pathname,
		// defaults to the default locale
		setLocale(locale_in_route || defaultLocale);
	}
</script>

<svelte:head>
	<title>{$t("error.title", { placeholder: `${$page.status}` })}</title>
</svelte:head>

<h1 class="title">{$t("error.title", { placeholder: `${$page.status}` })}</h1>

{#if $page.status === 404}
	{#if locale_in_route}
		<SvelteMarkdown
			source={$t("wiki.not-found", {
				placeholder: $page.url.pathname.replace(`/${locale_in_route}`, "")
			})}
		/>
	{:else}
		<SvelteMarkdown
			source={$t("error.404", {
				placeholder: $page.url.pathname
			})}
		/>
	{/if}
{:else}
	<p>{$t("error.500")}</p>
{/if}
