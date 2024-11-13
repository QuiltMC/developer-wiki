<script lang="ts">
	import { getFluentContext } from "@nubolab-ffwd/svelte-fluent";

	import { localesDir } from "$l10n";

	const { localize } = getFluentContext();

	let { data } = $props();
</script>

<svelte:head>
	<title>{localize("application-title")}</title>
</svelte:head>

{#if !data.translated}
	<!-- Display the landing page in the correct direction for the fallback locale -->
	<div dir={localesDir[data.fallbackLocale]}>
		<data.content />
	</div>
{:else}
	<data.content />
{/if}
