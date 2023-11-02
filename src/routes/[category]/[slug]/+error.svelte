<script lang="ts">
	import { page } from "$app/stores";
</script>

<svelte:head>
	<title>Error {$page.status}</title>
</svelte:head>

<h1 class="title">Error {$page.status}</h1>
<p>{$page.error?.message}</p>
