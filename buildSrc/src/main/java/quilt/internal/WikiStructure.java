package quilt.internal;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record WikiStructure(List<WikiType> libraries, List<WikiType> versions, String librariesNavbar,
                            String versionsNavbar, String masterSidebar) {
    public record WikiType(String name, String title, String content, String sidebar, Path path,
                           List<WikiSubEntry> wikis, String description) implements WikiEntry {
        @Override
        public boolean isProjectRoot() {
            return true;
        }

        public static final class Builder {
            private String name;
            private String title;
            private String content;
            private Path path;
            private List<WikiSubEntry> wikis;
			private String description;

            public Builder() {
                wikis = new ArrayList<>();
            }

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder withContent(String content) {
                this.content = content;
                return this;
            }

            public Builder withPath(Path path) {
                this.path = path;
                return this;
            }

            public Builder withWiki(WikiSubEntry wiki) {
                this.wikis.add(wiki);
                return this;
            }

			public Builder withDescription(String description) {
				this.description = description;
				return this;
			}

            public WikiType build(String path) {
                String sidebar = "- [" + this.title + "](" + path + "/)\n" +
                        wikis.stream().map(wiki -> generateMdSidebar(wiki, path + "/" + wiki.name, 0)).collect(Collectors.joining(""));
                String rendered = WikiBuildPlugin.RENDERER.render(WikiBuildPlugin.PARSER.parse(sidebar));
                return new WikiType(name, title, content, rendered.substring(rendered.indexOf("\n") + 1, rendered.lastIndexOf("\n") + 1), this.path, wikis, description);
            }

            private String generateMdSidebar(WikiEntry tree, String path, int i) {
                StringBuilder sidebar = new StringBuilder();
                String indent = "\t".repeat(i);

                if (tree.path() != null) {
                    sidebar.append(indent)
                            .append("- ")
                            .append("[" + tree.title() + "](" + path.replace("\\", "/") + "/)")
                            .append("\n");
                } else {
                    sidebar.append(indent)
                            .append("- ")
                            .append("~~" + tree.title() + "~~")
                            .append("\n");
                }

                for (WikiEntry entry : tree.wikis()) {
                    sidebar.append(generateMdSidebar(entry, path + "/" + entry.name(), i + 1));
                }
                return sidebar.toString();
            }
        }
    }

    public record WikiSubEntry(String name, String title, String content, Path path, boolean isProjectRoot,
                               List<WikiSubEntry> wikis, String description) implements WikiEntry {
        public static final class Builder {
            private String name;
            private String title;
            private String content;
            private Path path;
            private boolean isProjectRoot;
            private List<WikiSubEntry> wikis;
			private String description;

            public Builder() {
                this.wikis = new ArrayList<>();
            }

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder withContent(String content) {
                this.content = content;
                return this;
            }

            public Builder withPath(Path path) {
                this.path = path;
                return this;
            }

            public Builder isProjectRoot(boolean isProjectRoot) {
                this.isProjectRoot = isProjectRoot;
                return this;
            }

            public Builder withWiki(WikiSubEntry wiki) {
                this.wikis.add(wiki);
                return this;
            }

			public Builder withDescription(String description) {
				this.description = description;
				return this;
			}

            public WikiSubEntry build() {
                return new WikiSubEntry(name, title, content, path, isProjectRoot, wikis, description);
            }
        }
    }

    public interface WikiEntry {
        String name();

        String title();

        String content();

		String description();

        Path path();

        boolean isProjectRoot();

        List<WikiSubEntry> wikis();
    }

    public static final class Builder {
        private final List<WikiType> libraries;
        private final List<WikiType> versions;

        public Builder() {
            libraries = new ArrayList<>();
            versions = new ArrayList<>();
        }

        public Builder addLibrary(WikiType library) {
            this.libraries.add(library);
            return this;
        }

        public Builder addVersion(WikiType version) {
            this.versions.add(version);
            return this;
        }

        public WikiStructure build(String wikiPath) {
            String versionNavbar = versions.stream()
                    .map(entry -> "<a href=\"" + wikiPath + "/versions/" + entry.name + "/\" class = \"navbar-item\"> <span>" + entry.title + "</span></a>")
                    .collect(Collectors.joining(" "));
            String librariesNavbar = libraries.stream()
                    .map(entry -> "<a href=\"" + wikiPath + "/libraries/" + entry.name + "/\" class = \"navbar-item\"> <span>" + entry.title + "</span></a>")
                    .collect(Collectors.joining(" "));

            String masterSidebar =
                    "- Versions:\n"
                    + versions.stream().map(wikiType -> "\t- [" + wikiType.title + "](" + wikiPath + "/versions/" + wikiType.name + "/)").collect(Collectors.joining("\n"))
                    + "\n- Libraries:\n"
                    + libraries.stream().map(wikiType -> "\t- [" + wikiType.title + "](" + wikiPath + "/libraries/" + wikiType.name + "/)").collect(Collectors.joining("\n"));

            String renderedSidebar = WikiBuildPlugin.RENDERER.render(WikiBuildPlugin.PARSER.parse(masterSidebar));


            return new WikiStructure(libraries, versions, librariesNavbar, versionNavbar, renderedSidebar.substring(renderedSidebar.indexOf("\n") + 1, renderedSidebar.lastIndexOf("\n") + 1));
        }
    }
}
