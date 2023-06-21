<script lang="ts">
	import type { Category } from "./types";

	export let categories: Category[] = [];
	export let url: string;
</script>

<div class="column is-narrow">
	<div class="is-hidden-tablet">
		<label class="button is-primary" for="toggle-sidebar">
			<span class="icon"><i class="fas fa-ellipsis-h" /></span>
			<span>Menu</span>
		</label>

		<br />
		<br />
	</div>

	<div class="box is-hidden-mobile is-sticky sidebar">
		<aside class="menu">
			<p class="menu-label">Articles</p>
			<ul class="menu-list">
				{#each categories as category}
					<li>
						{category.name}
						<ul>
							{#each category.pages as page}
								<li>
									<a
										href={`/${category.slug}/${page.slug}`}
										class:is-active={url === `${category.slug}/${page.slug}`}>{page.title}</a
									>
								</li>
							{/each}
						</ul>
					</li>
				{/each}
			</ul>
		</aside>
	</div>
</div>
