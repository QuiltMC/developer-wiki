<script>
    import Sidebar from "$lib/Sidebar.svelte";

    export let data;
</script>

<svelte:head>
    <title>Quilt Developer Wiki</title>
</svelte:head>

<section class="section">
    <main class="container">
        <div class="columns">
            <Sidebar categories="{data.menu}" />

            <div class="content column">
                <h1>Quilt Developer Wiki :P</h1>

                <a href="/example">Checkout the example page!</a>
            </div>
        </div>
    </main>
</section>
