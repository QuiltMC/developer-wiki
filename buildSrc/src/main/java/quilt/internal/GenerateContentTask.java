package quilt.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private Map<File, String> generated;

    public GenerateContentTask() {
        setGroup("wiki");

        dependsOn("generateWikiTree");
    }

    @TaskAction
    public void generateContent() throws IOException {
        GenerateWikiTreeTask wikiTreeTask = (GenerateWikiTreeTask) getProject().getTasks().getByName("generateWikiTree");
        GenerateWikiTreeTask.FileEntry root = wikiTreeTask.getRoot();

        generated = new HashMap<>();

        generateContent(root);
    }

    private void generateContent(GenerateWikiTreeTask.FileEntry entry) throws IOException {
        if (entry.file() != null) {
            String input = Files.readString(entry.file().toPath()).replace("\r", "");
            Matcher matcher = Pattern.compile("```file:(.+?)(?:@(.+?))?\\n").matcher(input);

            input = matcher.replaceAll(matchResult -> replaceMatch(matchResult, entry));

            WikiBuildPlugin.currentEntry = entry;
            Node node = WikiBuildPlugin.PARSER.parse(input);
            String html = WikiBuildPlugin.RENDERER.render(node);
            generated.put(entry.file(), html);
        }

        for (GenerateWikiTreeTask.FileEntry subEntry : entry.subEntries()) {
            generateContent(subEntry);
        }
    }

    private String replaceMatch(MatchResult matchResult, GenerateWikiTreeTask.FileEntry entry) {
        String requestedFile = matchResult.group(1);
        String region = matchResult.group(2);

        File file = entry.project().file(requestedFile);

        String fileText;
        try {
            fileText = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (region != null) {
            int startRegion = fileText.indexOf("// @start " + region);
            int endRegion = fileText.indexOf("// @end " + region);

            fileText = fileText.substring(startRegion + 10 + region.length(), endRegion).stripTrailing() + "\n";
            fileText = "// ..." + fileText + "// ...\n";
        }


        return "From [`"+requestedFile.substring(requestedFile.lastIndexOf("/") + 1)+"`](https://github.com/QuiltMC/wiki/tree/"+requestedFile+"):\n```" + requestedFile.substring(requestedFile.lastIndexOf(".") + 1) + "\n" + fileText;
    }

    public Map<File, String> getGenerated() {
        return generated;
    }
}
