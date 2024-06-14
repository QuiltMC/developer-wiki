<script lang="ts">
	import { localize } from "@nubolab-ffwd/svelte-fluent";

	import { isRtl } from "$l10n";

	export let data;
</script>

<svelte:head>
	<title>{$localize("application-title")}</title>
</svelte:head>

{#if data.content}
	{#if !data.translated}
		<!-- Display the landing page in the correct direction for the fallback locale -->
		<div dir={isRtl(data.fallbackLocale) ? "rtl" : "ltr"}>
			<svelte:component this={data.content} />
		</div>
	{:else}
		<svelte:component this={data.content} />
	{/if}
{/if}
