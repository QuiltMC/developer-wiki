package quilt.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiTreeTask extends DefaultTask {
    @Internal
    private WikiStructure structure;

    public GenerateWikiTreeTask() {
        setGroup("wiki");
        dependsOn("generateContent");
    }

    @TaskAction
    public void generateTree() {
        // Get the built values
        Map<GenerateWikiFileTreeTask.FileEntry, String> content = ((GenerateContentTask) getProject().getTasks().getByPath("generateContent")).getGenerated();
        GenerateWikiFileTreeTask.FileEntry root = ((GenerateWikiFileTreeTask) getProject().getTasks().getByPath("generateWikiFileTree")).getRoot();

        WikiStructure.Builder builder = new WikiStructure.Builder();

        GenerateWikiFileTreeTask.FileEntry versionsEntry = root.subEntries().stream().filter(fileEntry -> fileEntry.name().equals("versions")).findFirst().get();
        GenerateWikiFileTreeTask.FileEntry librariesEntry = root.subEntries().stream().filter(fileEntry -> fileEntry.name().equals("libraries")).findFirst().get();

        buildRootEntries(versionsEntry, content).forEach(builder::addVersion);
        buildRootEntries(librariesEntry, content).forEach(builder::addLibrary);

        structure = builder.build((String) getProject().property("wiki_path"));
    }

    private List<WikiStructure.WikiType> buildRootEntries(GenerateWikiFileTreeTask.FileEntry root, Map<GenerateWikiFileTreeTask.FileEntry, String> content) {
        return root.subEntries().stream().map(entry -> {
            WikiStructure.WikiType.Builder builder = new WikiStructure.WikiType.Builder();
            builder.withName(entry.name()).withPath(entry.path()).withContent(content.getOrDefault(entry, ""));

            builder.withTitle(getEntryTitle(content, entry));
			builder.withDescription(getFileDescription(entry));

            buildSubEntries(entry, content, 0).forEach(builder::withWiki);

            return builder.build(getProject().property("wiki_path") + "/" + root.name() + "/" + entry.name());
        }).toList();
    }

    private List<WikiStructure.WikiSubEntry> buildSubEntries(GenerateWikiFileTreeTask.FileEntry root, Map<GenerateWikiFileTreeTask.FileEntry, String> content, int depth) {
        if (depth > 3) {
            throw new RuntimeException("Wiki tree is too deep, unable to add file: " + root.path());
        }

        return root.subEntries().stream().map(entry -> {
            WikiStructure.WikiSubEntry.Builder builder = new WikiStructure.WikiSubEntry.Builder();
            builder.withName(entry.name()).withPath(entry.path()).withContent(content.getOrDefault(entry, "")).isProjectRoot(root.project() != entry.project());

            builder.withTitle(getEntryTitle(content, entry));
			builder.withDescription(getFileDescription(entry));

            buildSubEntries(entry, content, depth + 1).forEach(builder::withWiki);

            return builder.build();
        }).toList();
    }

	private String getEntryTitle(Map<GenerateWikiFileTreeTask.FileEntry, String> content, GenerateWikiFileTreeTask.FileEntry entry) {
        if (content.containsKey(entry)) {
            String fileContent = content.get(entry);
            Pattern p = Pattern.compile("<h1><a href=\".+?\" id=\".+?\">(.+?)<");
            Matcher matcher = p.matcher(fileContent);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return entry.name();
    }

	private String getFileDescription(GenerateWikiFileTreeTask.FileEntry entry) {
		try {
			List<String> lines = Files.readAllLines(entry.path());
			for (String line : lines) {
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}

				if (line.length() < 160) {
					return line;
				}

				return line.substring(0, 157) + "...";
			}
		} catch (IOException ignored) {
		}
		return "The Quilt Developer Wiki";
	}

    public WikiStructure getStructure() {
        return structure;
    }
}
