<script lang="ts">
	interface Props {
		title: string;
		style: string;
		href: string;
		icon?: string;
	}

	let { title, style, href, icon }: Props = $props();
</script>

<a {title} {href} class="button is-rounded {style}" target="_blank">
	{#if icon}
		<span class="icon-text">
			<div class={icon}></div>
		</span>
	{/if}
</a>
