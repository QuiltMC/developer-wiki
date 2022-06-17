package quilt.internal;


import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.LinkResolver;
import com.vladsch.flexmark.html.LinkResolverFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverBasicContext;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WikiBuildPlugin implements Plugin<Project> {
    public static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(Extensions.GITHUB_DOCUMENT_COMPATIBLE, new MdLinkParserExtension());

    public static final Parser PARSER = Parser.builder(OPTIONS).build();
    public static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).indentSize(4).build();

    public static GenerateWikiFileTreeTask.FileEntry currentEntry;

    @Override
    public void apply(Project target) {
        target.getTasks().register("generateWikiFileTree", GenerateWikiFileTreeTask.class);
        target.getTasks().register("generateContent", GenerateContentTask.class);
        target.getTasks().register("generateWikiTree", GenerateWikiTreeTask .class);
        target.getTasks().register("generateWiki", GenerateWikiTask.class);
		target.getTasks().register("testWiki", TestWikiTask.class);
    }

    private static class MdLinkParserExtension implements HtmlRenderer.HtmlRendererExtension {
        @Override
        public void rendererOptions(@NotNull MutableDataHolder options) {

        }

        @Override
        public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
            htmlRendererBuilder.linkResolverFactory(new MdLinkParserResolver.Factory());
        }
    }

    private static class MdLinkParserResolver implements LinkResolver {

        @Override
        public @NotNull ResolvedLink resolveLink(@NotNull Node node, @NotNull LinkResolverBasicContext linkResolverBasicContext, @NotNull ResolvedLink resolvedLink) {
            // Don't process external links
            if (!resolvedLink.getUrl().startsWith("http")) {
                // Link resolving for .md files
                if (resolvedLink.getUrl().endsWith(".md")) {
                    // remove the ".md" at the end
                    String strippedMd = resolvedLink.getUrl().substring(0, resolvedLink.getUrl().length() - 3);
                    if (strippedMd.startsWith("..")) { // File is in a super tree, requires more advanced processing, otherwise it is an adjacent path and is simple
                        // Remove the "../"
                        strippedMd = strippedMd.substring(3);
                        // Break path into separate parts
                        String[] path = strippedMd.split("/");
                        // Start with the currentEntry
                        GenerateWikiFileTreeTask.FileEntry entry = currentEntry;
                        for (int i = 0; i < path.length; i++) {
                            if (path[i].equals("markdown")) { // If path includes "markdown", remove that path section
                                path[i] = null;
                            } else if (path[i].equals(entry.name())) { // If path includes the entry name, such as when entering a subtree, remove that path section
                                path[i] = null;
                            } else if (path[i].equals(".")) { // Do nothing with "."
                                // no op
                            } else if (path[i].equals("..")) { // If path includes "..", move to the parent entry
                                entry = entry.parent();
                            } else { // Need to traverse down a level and find the subtree
                                int finalI = i; // Make i final because java
                                entry = entry.subEntries().stream()
                                        .filter(subEntry -> subEntry.name().equals(path[finalI])).findFirst() // Find the sub entry with the same name as the path section
                                        .orElseThrow(() -> new RuntimeException("Unknown path " + resolvedLink.getUrl())); // Error
                            }
                        }
                        strippedMd = Arrays.stream(path).filter(Predicate.not(Objects::isNull)).collect(Collectors.joining("/")); // Combine the path into one string
                    }
                    return resolvedLink.withUrl(strippedMd);
                }

                // Image link processing
                if (resolvedLink.getUrl().endsWith(".png")){
                    // Removes the "../" from the links
                    String url = resolvedLink.getUrl().substring(3);

                    // Makes sub entries point to the right image
                    if (currentEntry.project() == currentEntry.parent().project()) {
                        url = "../" + url;
                    }

                    return resolvedLink.withUrl(url);
                }
            }

            if (!resolvedLink.getUrl().startsWith((String) currentEntry.project().property("wiki_path"))) {
                return resolvedLink.withTarget("_blank");
            }

            return resolvedLink;
        }

        static class Factory implements LinkResolverFactory {
            @Nullable
            @Override
            public Set<Class<?>> getAfterDependents() {
                return null;
            }

            @Nullable
            @Override
            public Set<Class<?>> getBeforeDependents() {
                return null;
            }

            @Override
            public boolean affectsGlobalScope() {
                return false;
            }

            @NotNull
            @Override
            public LinkResolver apply(@NotNull LinkResolverBasicContext context) {
                return new MdLinkParserResolver();
            }
        }
    }
}
