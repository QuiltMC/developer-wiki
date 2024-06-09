import { defineConfig, presetIcons } from "unocss";

export default defineConfig({
	presets: [
		presetIcons({
			// Collections are automatically imported from inconify packages
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
