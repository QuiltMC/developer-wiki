export interface Page {
  slug: string;
  title: string;
}

export interface Category {
  name: string;
  pages: Page[];
}
