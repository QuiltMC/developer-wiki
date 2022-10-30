package quilt.internal;

import com.vladsch.flexmark.util.ast.Node;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateContentTask extends DefaultTask {
	@Internal
	private Map<GenerateWikiFileTreeTask.FileEntry, String> generated;

	public GenerateContentTask() {
		setGroup("wiki");

		dependsOn("generateWikiFileTree");
	}

	@TaskAction
	public void generateContent() {
		GenerateWikiFileTreeTask wikiTreeTask =
				(GenerateWikiFileTreeTask) getProject().getTasks().getByName("generateWikiFileTree");

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

			input = matcher.replaceAll(matchResult -> replaceSimpleMatch(matchResult, entry));

			// Parse tabbed path includes
			matcher = Pattern.compile("```tabbed-files\\s*(([a-z]+):(.+?)(?:@(.+?))?\\s+)+```").matcher(input);

			input = matcher.replaceAll(matchResult -> replaceTabbedMatch(matchResult, entry));

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

	private String replaceTabbedMatch(MatchResult matchResult, GenerateWikiFileTreeTask.FileEntry entry) {
		String[] full = matchResult.group(0).split("\\s+");
		StringBuilder tabs = new StringBuilder();
		StringBuilder sections = new StringBuilder();
		StringBuilder langClasses = new StringBuilder();
		boolean isFirst = true;
		for (String tab : full) {
			Matcher matcher = Pattern.compile("([a-z]+):(.+?)(?:@(.+?))?").matcher(tab);
			if (matcher.matches()) {
				String language = matcher.group(1);
				if (!isFirst)
					langClasses.append(' ');
				langClasses.append("has-lang-"+language);
				tabs.append("<li class=\"tab"+(isFirst ? " is-active" : "")+"\" onclick=\"switchTab(event,'" + language + "')\"><a>"+capitalize(language)+"</a></li>\n");
				String codeChunk = replaceMatch(matcher.group(2), matcher.group(3), entry);
				sections.append("<section class=\"tab-contents lang-selected-" + language + "\""+(!isFirst ? " style=\"display:none\"" : "")+">\n\n" + codeChunk + "```\n\n</section>\n");
				isFirst = false;
			}
		}
		return "<div class=\"tab-holder\">\n" +
				"<div class=\"tabs is-boxed\">\n" +
				"<ul class=\""+langClasses+"\">\n" +
				tabs +
				"</ul>\n" +
				"</div>\n" +
				"<div class=\""+langClasses+"\">\n" +
				sections +
				"</div>\n" +
				"</div>\n<p>\n";
	}

	private String replaceSimpleMatch(MatchResult matchResult, GenerateWikiFileTreeTask.FileEntry entry) {
		String requestedFile = matchResult.group(1);
		String region = matchResult.group(2);

		return replaceMatch(requestedFile, region, entry);
	}

	private String replaceMatch(String requestedFile, String region, GenerateWikiFileTreeTask.FileEntry entry) {
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

		return "From [`" +
				requestedFile.substring(requestedFile.lastIndexOf("/") + 1) +
				"`](" +
				getProject().property("git_repo_tree") +
				"/" +
				filePath +
				"):\n```" +
				fileType +
				"\n" +
				fileText +
				"\n";
	}

	public Map<GenerateWikiFileTreeTask.FileEntry, String> getGenerated() {
		return generated;
	}

	private static final Map<String, String> CAPITALIZATION_LOOKUP = Map.of(
			"java", "Java",
			"kotlin", "Kotlin",
			"json", "JSON"
	);

	private String capitalize(String string) {
		string = string.toLowerCase(Locale.ROOT);
		String lookup = CAPITALIZATION_LOOKUP.get(string);
		if (lookup != null)
			return lookup;
		return string.substring(0,1).toUpperCase(Locale.ROOT)+string.substring(1);
	}
}
