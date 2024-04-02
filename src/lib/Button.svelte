<script lang="ts">
	export let title: string;
	export let style: string;
	export let href: string;
	export let icon: string | undefined = undefined;
</script>

<a {title} {href} class="button is-rounded {style}" target="_blank">
	{#if icon}
		<span class="icon-text">
			<div class={icon} />
			<slot />
		</span>
	{:else}
		<slot />
	{/if}
</a>
