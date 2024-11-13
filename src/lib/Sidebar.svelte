<script lang="ts">
	import { Localized } from "@nubolab-ffwd/svelte-fluent";

	import type { Category } from "./types";

	import current from "$lib/current.svelte";

	interface Props {
		categories: Category[];
		url: string;
	}

	let { categories = [], url }: Props = $props();
</script>

<div class="column is-narrow">
	<div class="is-hidden-tablet">
		<label class="button is-primary" for="toggle-sidebar">
			<span class="icon"><div class="i-fa6-solid-ellipsis"></div></span>
			<span>
				<Localized id="menu" />
			</span>
		</label>

		<br />
		<br />
	</div>

	<div class="box is-hidden-mobile is-sticky sidebar">
		<aside class="menu">
			<p class="menu-label">
				<Localized id="articles" />
			</p>
			<ul class="menu-list">
				{#each categories as category}
					<li class:is-hidden={category.draft}>
						<Localized id={category.slug}>
							{#snippet children({ text, attrs })}
								{text}
								<ul>
									{#each category.pages as page}
										<li class:is-hidden={page.draft}>
											<a
												href={`/${current.locale}/${category.slug}/${page.slug}`}
												class:is-active={url === `${category.slug}/${page.slug}`}
											>
												{attrs[page.slug]}
											</a>
											<!-- Needed to tell SvelteKit to generate the wiki page with no locale set (with the default locale) -->
											<a class="is-hidden" href={`/${category.slug}/${page.slug}`}>
												{attrs[page.slug]}
											</a>
										</li>
									{/each}
								</ul>
							{/snippet}
						</Localized>
					</li>
				{/each}
			</ul>
		</aside>
	</div>
</div>
