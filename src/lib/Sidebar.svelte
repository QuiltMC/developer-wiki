<script>
	export let categories;
	export let slug;
</script>

<div class="column is-narrow" data-controller="sidebar">
	<div class="is-hidden-tablet">
		<button data-action="click->sidebar#toggle" class="button is-primary">
			<span class="icon"><i class="fas fa-ellipsis-h" /></span>
			<span>Menu</span>
		</button>

		<br />
		<br />
	</div>

	<div class="box is-hidden-mobile is-sticky" data-sidebar-target="sidebar">
		<div class="menu">
			<ul class="menu-list">
				{#each categories as category}
					<li>
						{category.name}
						{#each category.pages as page}
							<ul>
								{#if slug === page.slug}
									<li><a href="/{page.slug}" class="is-active">{page.title}</a></li>
								{:else}
									<li><a href="/{page.slug}">{page.title}</a></li>
								{/if}
							</ul>
						{/each}
					</li>
				{/each}
			</ul>
		</div>
	</div>
</div>
