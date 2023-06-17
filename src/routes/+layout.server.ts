import fs from "fs/promises";


interface Page {
	slug: string;
	title: string;
}

interface Category {
	name: string;
	pages: Page[]
}

export async function load(){
	const files = await fs.readdir("src/routes");
	const articles = files.filter(path => path.endsWith(".md") && !path.startsWith("+")).map(path => path.slice(0, -3));

	let categories: Category[] = [];

	for (const article of articles) {
		const post = await import(`../routes/${article}.md` /* @vite-ignore */ ); // Don't ask, it works

		for (const category of post.metadata.categories) {
			const idx = categories.findIndex(cat => cat.name === category);

			if (idx != -1) {
				categories[idx].pages.push(post.metadata)
			} else {
				categories.push({ name: category, pages: [{ slug: article, title: post.metadata.title }] })
			}
		}
	}

	return { menu: categories };
}
