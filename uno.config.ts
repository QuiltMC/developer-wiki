import { defineConfig, presetIcons } from "unocss";

export default defineConfig({
	presets: [
		presetIcons({
			// Collections are automatically imported from inconify packages
			collections: {
				"fa6-brands": () => import("@iconify-json/fa6-brands/icons.json").then((i) => i.default),
				"fa6-solid": () => import("@iconify-json/fa6-solid/icons.json").then((i) => i.default),
				tabler: () => import("@iconify-json/tabler/icons.json").then((i) => i.default)
			},
			customizations: {
				iconCustomizer(collection, icon, props) {
					// Makes the empty cut in half circle slightly larger
					// to match the size of the font awesome's one
					if (collection === "tabler" && icon === "circle-half") {
						props.width = "1.2em";
						props.height = "1.2em";
					}

					// Change the font awesome half filled circle icon's width
					// to match that of the empty one
					if (collection === "fa6-solid" && icon === "circle-half-stroke") {
						props.width = "1.2em";
					}
				}
			}
		})
	],
	rules: [
		// Replicates font awesome's "fa-xl"
		["i-xl", { "font-size": "1.5em" }],
		["i-2xl", { "font-size": "2em" }]
	]
});
