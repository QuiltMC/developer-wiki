<script lang="ts">
	import type { Category } from "./types";

	import { t, locale, locales } from "$lib/translations";

	export let categories: Category[] = [];
	export let url: string;
</script>

<div class="column is-narrow">
	<div class="is-hidden-tablet">
		<label class="button is-primary" for="toggle-sidebar">
			<span class="icon"><div class="i-fa6-solid-ellipsis" /></span>
			<span>{$t("application.sidebar.menu")}</span>
		</label>

		<br />
		<br />
	</div>

	<div class="box is-hidden-mobile is-sticky sidebar">
		<aside class="menu">
			<p class="menu-label">{$t("application.sidebar.articles")}</p>
			<ul class="menu-list">
				{#each categories as category}
					<li>
						{$t(category.name)}
						<ul>
							{#each category.pages as page}
								<li>
									<a
										href={`/${$locale}/${category.slug}/${page.slug}`}
										class:is-active={url === `/${$locale}${category.slug}/${page.slug}`}
									>
										{$t(page.title)}
									</a>
									{#each $locales as locale}
										<!-- Needed to tell SvelteKit to generate the non english pages of the wiki -->
										<a class="is-hidden" href={`/${locale}/${category.slug}/${page.slug}`}>
											{$t(page.title)}
										</a>
									{/each}
								</li>
							{/each}
						</ul>
					</li>
				{/each}
			</ul>
		</aside>
	</div>
</div>
