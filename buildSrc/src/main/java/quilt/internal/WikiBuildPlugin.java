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
    public static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    public static GenerateWikiTreeTask.FileEntry currentEntry;

    @Override
    public void apply(Project target) {
        target.getTasks().register("generateWikiTree", GenerateWikiTreeTask.class);
        target.getTasks().register("generateSidebars", GenerateSidebarsTask.class);
        target.getTasks().register("generateContent", GenerateContentTask.class);
        target.getTasks().register("generateWiki", GenerateWikiTask.class);
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
            if (!resolvedLink.getUrl().startsWith("https://") && resolvedLink.getUrl().endsWith(".md")) {
                String strippedMd = resolvedLink.getUrl().substring(0, resolvedLink.getUrl().length() - 3);
                if (strippedMd.startsWith("..")) {
                    strippedMd = strippedMd.substring(3);
                    String[] path = strippedMd.split("/");
                    GenerateWikiTreeTask.FileEntry entry = currentEntry;
                    for (int i = 0; i < path.length; i++) {
                        if (path[i].equals("markdown")) {
                            path[i] = null;
                        } else if (path[i].equals(entry.name())) {
                            path[i] = null;
                        } else if (path[i].equals(".")) {
                            // no op
                        } else if (path[i].equals("..")) {
                            entry = entry.parent();
                        } else {
                            int finalI = i;
                            entry = entry.subEntries().stream().filter(subEntry -> subEntry.name().equals(path[finalI])).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Unknown path " + resolvedLink.getUrl()));
                        }
                    }
                    strippedMd = Arrays.stream(path).filter(Predicate.not(Objects::isNull)).collect(Collectors.joining("/"));
                }
                return resolvedLink.withUrl(strippedMd);
            }

            if (!resolvedLink.getUrl().startsWith("https://") && resolvedLink.getUrl().endsWith(".png")) {
                return resolvedLink.withUrl(resolvedLink.getUrl().substring(2));
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
