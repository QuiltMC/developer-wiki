# Quilt Developer Wiki

## Developing

In order to preview what will appear on the site as you write wiki articles, you'll need to install [PNPM](https://pnpm.io/).
After installing, run the following commands to host a preview:

Install dependencies:

```bash
pnpm install
```

Run a development server with:

```bash
pnpm dev

# or start the server and open the app in a new browser tab
pnpm dev -- --open
```

### Building

To create a production version of the wiki:

```bash
pnpm build
```

You can preview the production build with `pnpm preview`.
