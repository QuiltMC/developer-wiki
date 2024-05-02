<script lang="ts">
	import type { Category } from "./types";

	import { t, locale } from "$lib/translations";

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
					<li class:is-hidden={category.draft}>
						{$t(category.name)}
						<ul>
							{#each category.pages as page}
								<li class:is-hidden={page.draft}>
									<a
										href={`/${$locale}/${category.slug}/${page.slug}`}
										class:is-active={url === `/${$locale}${category.slug}/${page.slug}`}
									>
										{$t(page.title)}
									</a>
									<!-- Needed to tell SvelteKit to generate the wiki page with no locale set (with the default locale) -->
									<a class="is-hidden" href={`/${category.slug}/${page.slug}`}>
										{$t(page.title)}
									</a>
								</li>
							{/each}
						</ul>
					</li>
				{/each}
			</ul>
		</aside>
	</div>
</div>
