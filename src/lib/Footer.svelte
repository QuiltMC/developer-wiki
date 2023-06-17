<script lang="ts">
	import Button from "./Button.svelte";

	// prettier-ignore
	const buttons = [
		{ title: "Discord", style: "is-discord", href: "https://discord.quiltmc.org", icon: "fab fa-discord" },
		{ title: "Forum", style: "is-gradle", href: "https://forum.quiltmc.org", icon: "fab fa-discourse" },
		{ title: "GitHub", style: "is-github", href: "https://github.com/QuiltMC", icon: "fab fa-github" },
		{ title: "Twitter", style: "is-twitter", href: "https://twitter.com/quilt_mc", icon: "fab fa-twitter" },
		{ title: "Mastodon", style: "is-mastodon", href: "https://tech.lgbt/@quiltmc", icon: "fab fa-mastodon" },
		{ title: "Twitch", style: "is-primary", href: "https://twitch.tv/quiltmc", icon: "fab fa-twitch" },
		{ title: "RSS", style: "is-rss", href: "https://quiltmc.org/feed.xml", icon: "fa-solid fa-rss" },
	];
</script>

<footer class="footer">
	<div class="content">
		<div class="columns">
			<div class="column is-half is-offset-one-quarter">
				<div class="button-grid">
					{#each buttons as button}
						<Button {...button} />
					{/each}
				</div>

				<br />

				<div class="has-text-centered">
					<p>
						<strong>Copyright &copy; {new Date().getFullYear()} Quilt Project</strong>
					</p>

					<p>
						Quilt is not an official Minecraft product, and is not approved by or associated with
						Mojang Studios. "Minecraft" is a trademark of Mojang AB and any usage of the Minecraft
						brand on this site is used in accordance with Mojang Studios'
						<a href="https://account.mojang.com/terms?ref=ft#brand" target="_blank"
							>Brand and Asset Guidelines</a
						>.
					</p>

					<p>
						All logos used on this site are the property of their respective owners. The CurseForge
						logo recreation we use in a few places on this site
						<a
							href="https://gist.github.com/thecodewarrior/110057b210551c4ecf2c9be6d58ff824"
							target="_blank">was created by thecodewarrior</a
						>.
					</p>

					<p>
						Made with <img
							src="https://quiltmc.org/assets/img/pineapple.png"
							alt=":pineapple:"
							title="Pineapple"
							width="24"
							height="24"
							class="pineapple"
						/>,
						<a href="https://bulma.io">Bulma</a> and
						<a href="https://github.com/CreativeBulma">the CreativeBulma extensions</a>.
					</p>
				</div>
			</div>
		</div>
	</div>
</footer>

<style>
	.pineapple {
		margin-bottom: -5px;
	}
</style>
