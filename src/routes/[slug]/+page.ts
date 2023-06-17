import fs from "fs/promises";

export async function load({ params }) {
  const post = await import(`../${params.slug}.md` /* @vite-ignore */);
  const { title } = post.metadata;
  const content = post.default;

  return { content, title, slug: params.slug };
}

export async function entries() {
  const files = await fs.readdir("src/routes");
  const articles = files.filter((path) =>
    path.endsWith(".md") && !path.startsWith("+")
  );
  return articles.map((path) => {
    return { slug: path.slice(0, -3) };
  });
}
