<script lang="ts">
	import type { Category } from "./types";

	export let categories: Category[] = [];
	export let slug: string | undefined = undefined;
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
								<li>
									<a href="/{page.slug}" class:is-active={slug === page.slug}>{page.title}</a>
								</li>
							</ul>
						{/each}
					</li>
				{/each}
			</ul>
		</div>
	</div>
</div>
