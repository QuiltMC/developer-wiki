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
import java.util.stream.Collectors;

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
			Matcher matcher = Pattern.compile("```([a-z]+):(.+?)(?:@(.+?))?\\n").matcher(input);

			input = matcher.replaceAll(matchResult -> replaceSimpleMatch(matchResult, entry));

			// Parse tabbed path includes
			matcher = Pattern.compile("```tabbed-files\\s*(([a-z]+)(?:@([a-z]+?))?:(.+?)(?:@(.+?))?\\s+)+```").matcher(input);

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
		StringBuilder outerClasses = new StringBuilder();
		boolean isFirst = true;
		List<Matcher> matchers = Arrays.stream(full).map(tab -> Pattern.compile("([a-z]+)(?:@([a-z]+?))?:(.+?)(?:@(.+?))?").matcher(tab)).toList();
		boolean isAnyMapped = matchers.stream().anyMatch(it -> it.matches() && it.group(2)!=null);
		if (isAnyMapped)
			outerClasses.append("has-mappable ");
		for (Matcher matcher : matchers) {
			if (matcher.matches()) {
				String language = matcher.group(1);
				String mappings = matcher.group(2);
				if (mappings == null && isAnyMapped)
					mappings = MAPPING_DEFAULT_FOR_LANG.getOrDefault(language.toLowerCase(Locale.ROOT), "qm");
				String name = capitalizeLanguage(language);
				String tag = language+(mappings == null ? "" : ("-"+mappings));
				if (!isFirst)
					outerClasses.append(' ');
				outerClasses.append("has-lang-"+tag);
				if (mappings != null) {
					outerClasses.append(" has-mappings-" + mappings);
					name += "<span class=\"mappings-optional-selector\" style=\"display:none\">&nbsp("+capitalizeMapping(mappings)+")</span>";
				}
				tabs.append("<li class=\"tab"+
						(mappings!=null?" is-mappings-"+mappings:"")
						+(isFirst ? " is-active" : "")
						+"\" onclick=\"switchTab(event,'" + tag + "')\" data-tablang=\""+language+"\"><a>"+name+"</a></li>\n");
				String codeChunk = replaceMatch(matcher.group(3), matcher.group(4), language, entry);
				sections.append("<section class=\"tab-contents"+
						(mappings!=null?" is-mappings-"+mappings:"")
						+" lang-selected-" + tag + "\" data-tablang=\""+language+"\""
						+(!isFirst ? " style=\"display:none\"" : "")+">\n\n" + codeChunk + "```\n\n</section>\n");
				isFirst = false;
			}
		}
		String prefixedOuterClasses = outerClasses.isEmpty()?"":" "+outerClasses;
		return "<div class=\"lang-tab-holder\">\n" +
				"<div class=\"tabs lang-tab-wrapper is-boxed\">\n" +
				"<ul class=\"lang-tab-list"+ prefixedOuterClasses +"\">\n" +
				tabs +
				"</ul>\n" +
				"</div>\n" +
				"<div class=\"lang-tab-contents"+ prefixedOuterClasses +"\">\n" +
				sections +
				"</div>\n" +
				"</div>\n<p>\n";
	}

	private String replaceSimpleMatch(MatchResult matchResult, GenerateWikiFileTreeTask.FileEntry entry) {
		String requestedFile = matchResult.group(2);
		String region = matchResult.group(3);
		String fileType = matchResult.group(1).equals("file") ?
				requestedFile.substring(requestedFile.lastIndexOf(".") + 1) : matchResult.group(1);

		return replaceMatch(requestedFile, region, fileType, entry);
	}

	private String replaceMatch(String requestedFile, String region, String fileType, GenerateWikiFileTreeTask.FileEntry entry) {
		Path file = entry.project().file(requestedFile).toPath();

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
			int maxLeading = fileText.lines().filter(line->!line.isBlank()).mapToInt(line->{
				Matcher matcher = Pattern.compile("^(\\s*).*").matcher(line);
				if(!matcher.matches())
					return 0;
				return matcher.group(1).length();
			}).min().orElse(0);
			fileText = fileText.lines().map(line -> {
				if(line.isBlank())
					return line;
				return line.substring(maxLeading);
			}).collect(Collectors.joining("\n"))+"\n";

			fileText = "// ..." + fileText + "// ...";
		}

		fileText = fileText.lines().filter(line ->
				!Pattern.compile("// @start .*")
						.matcher(line.trim()).matches() ||
				!Pattern.compile("// @end .*")
						.matcher(line.trim()).matches()).collect(Collectors.joining("\n"));

		String filePath = getProject().file(".").toPath().relativize(file).toString().replace("\\", "/");

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

	private static final Map<String, String> MAPPING_LOOKUP = Map.of(
			"qm", "Quilt Mappings",
			"mojmaps", "Mojang Mappings",
			"yarn", "Yarn"
	);

	private static final Map<String, String> MAPPING_DEFAULT_FOR_LANG = Map.of(
			"groovy", "mojmaps"
	);

	private String capitalizeLanguage(String string) {
		string = string.toLowerCase(Locale.ROOT);
		String lookup = CAPITALIZATION_LOOKUP.get(string);
		if (lookup != null)
			return lookup;
		return string.substring(0,1).toUpperCase(Locale.ROOT)+string.substring(1);
	}

	private String capitalizeMapping(String string) {
		string = string.toLowerCase(Locale.ROOT);
		String lookup = MAPPING_LOOKUP.get(string);
		if (lookup != null)
			return lookup;
		return string.substring(0,1).toUpperCase(Locale.ROOT)+string.substring(1);
	}
}
