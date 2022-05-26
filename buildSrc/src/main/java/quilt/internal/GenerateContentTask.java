package quilt.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vladsch.flexmark.util.ast.Node;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class GenerateContentTask extends DefaultTask {
    @Internal
    private Map<GenerateWikiFileTreeTask.FileEntry, String> generated;

    public GenerateContentTask() {
        setGroup("wiki");

        dependsOn("generateWikiFileTree");
    }

    @TaskAction
    public void generateContent() {
        GenerateWikiFileTreeTask wikiTreeTask = (GenerateWikiFileTreeTask) getProject().getTasks().getByName("generateWikiFileTree");
        GenerateWikiFileTreeTask.FileEntry root = wikiTreeTask.getRoot();

        generated = new HashMap<>();
        root.subEntries().forEach(entry -> {
            try {
                this.generateContent(entry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void generateContent(GenerateWikiFileTreeTask.FileEntry entry) throws IOException {
        if (Files.exists(entry.path())) {
            // Read the path
            String input = Files.readString(entry.path()).replace("\r", "");
            // Parse the path includes
            Matcher matcher = Pattern.compile("```file:(.+?)(?:@(.+?))?\\n").matcher(input);
            input = matcher.replaceAll(matchResult -> replaceMatch(matchResult, entry));

            // Set the current entry for link parsing
            WikiBuildPlugin.currentEntry = entry;

            // Parse and save the path
            Node node = WikiBuildPlugin.PARSER.parse(input);
            String html = WikiBuildPlugin.RENDERER.render(node);
            generated.put(entry, html);
        }

        // Generate the sub files
        for (GenerateWikiFileTreeTask.FileEntry subEntry : entry.subEntries()) {
            generateContent(subEntry);
        }
    }

    private String replaceMatch(MatchResult matchResult, GenerateWikiFileTreeTask.FileEntry entry) {
        String requestedFile = matchResult.group(1);
        String region = matchResult.group(2);

        Path file = entry.project().file(requestedFile).toPath();

        // TODO: Strip extra region comments
        // TODO: have different file type be able to set the single line comment character(s)

        // Read the path
        String fileText;
        try {
            fileText = Files.readString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Parse if the region is set
        if (region != null) {
            int startRegion = fileText.indexOf("// @start " + region);
            int endRegion = fileText.indexOf("// @end " + region);

            fileText = fileText.substring(startRegion + 10 + region.length(), endRegion).stripTrailing() + "\n";
            fileText = "// ..." + fileText + "// ...\n";
        }

        String filePath = getProject().file(".").toPath().relativize(file).toString().replace("\\", "/");
        String fileType = requestedFile.substring(requestedFile.lastIndexOf(".") + 1);

        return "From [`" + requestedFile.substring(requestedFile.lastIndexOf("/") + 1) + "`](" + getProject().property("git_repo_tree") + "/" + filePath + "):\n```" + fileType + "\n" + fileText;
    }

    public Map<GenerateWikiFileTreeTask.FileEntry, String> getGenerated() {
        return generated;
    }
}
