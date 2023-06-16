<script>
    export let data;
</script>

<svelte:head>
    <title>{data.title}</title>
</svelte:head>

<div>
    <div class="columns">
        <div class="content column">
            <h1>{data.title}</h1>
            <h3>Categories: {data.categories}</h3>

            <svelte:component this={data.content} />
        </div>
    </div>
</div>

