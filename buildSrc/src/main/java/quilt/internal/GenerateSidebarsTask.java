package quilt.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class GenerateSidebarsTask extends DefaultTask {
    @Internal
    protected Map<String, String> sidebars;

    public GenerateSidebarsTask() {
        setGroup("wiki");
        this.dependsOn("generateWikiFileTree");
    }

    @TaskAction
    public void generateSidebars() {
        GenerateWikiFileTreeTask wikiTreeTask = (GenerateWikiFileTreeTask) getProject().getTasks().getByName("generateWikiFileTree");
        GenerateWikiFileTreeTask.FileEntry root = wikiTreeTask.getRoot();

        sidebars = new HashMap<>();


        for (GenerateWikiFileTreeTask.FileEntry wikiType : root.subEntries()) {
            for (GenerateWikiFileTreeTask.FileEntry subWiki : wikiType.subEntries()) {
                String rendered = WikiBuildPlugin.RENDERER.render(WikiBuildPlugin.PARSER.parse(generateMdSidebar(subWiki, wikiType.name() + "/" + subWiki.name(), 0)));
                sidebars.put(subWiki.name(), rendered.substring(rendered.indexOf("\n") + 1, rendered.lastIndexOf("\n") + 1));
            }
        }
    }

    private String generateMdSidebar(GenerateWikiFileTreeTask.FileEntry tree, String path, int i) {
        StringBuilder sidebar = new StringBuilder();
        String indent = "\t".repeat(i);

        if (tree.path() != null) {
            sidebar.append(indent)
                    .append("- ")
                    .append("[" + tree.name() + "](" + getProject().property("wiki_path")+ "/" + path.replace("\\", "/") + "/)")
                    .append("\n");
        } else {
            sidebar.append(indent)
                    .append("- ")
                    .append("~~" + tree.name() + "~~")
                    .append("\n");
        }

        for (GenerateWikiFileTreeTask.FileEntry entry : tree.subEntries()) {
            sidebar.append(generateMdSidebar(entry, path + "/" + entry.name(), i + 1));
        }
        return sidebar.toString();
    }

    public Map<String, String> getSidebars() {
        return sidebars;
    }
}
