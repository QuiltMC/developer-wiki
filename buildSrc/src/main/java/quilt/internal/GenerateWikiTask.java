package quilt.internal;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiTask extends DefaultTask {
    private final PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(false).build();

    public GenerateWikiTask() {
        setGroup("wiki");

        dependsOn("generateWikiTree");
    }

    @TaskAction
    public void generateWiki() throws IOException {
        // Get the built values
        WikiStructure wiki = ((GenerateWikiTreeTask) getProject().getTasks().getByPath("generateWikiTree")).getStructure();

        // Clean the output
        String outputPath = (String) getProject().property("output_path");
        getProject().delete(outputPath);
        Path output = getProject().file(outputPath).toPath();
        Files.createDirectory(output);

        Map<String, Object> defaultOptions = Map.of(
                "version_navbar", wiki.versionsNavbar(),
                "libraries_navbar", wiki.librariesNavbar(),
                "wiki_path", getProject().property("wiki_path"),
                "git_repo_tree", getProject().property("git_repo_tree"));

        // Generate the files
        Path versionsPath = Files.createDirectory(output.resolve("versions"));
        for (WikiStructure.WikiType wikiType : wiki.versions()) {
            outputFile(wikiType, versionsPath, wikiType.sidebar(), defaultOptions);
        }

        Path librariesPath = Files.createDirectory(output.resolve("libraries"));
        for (WikiStructure.WikiType wikiType : wiki.libraries()) {
            outputFile(wikiType, librariesPath, wikiType.sidebar(), defaultOptions);
        }

		Map<String, Object> indexOptions = new HashMap<>(defaultOptions);
		indexOptions.put("sidebar", wiki.masterSidebar());

		generateStaticPage("wiki/templates/index.html", defaultOptions, indexOptions, output, "index.html", "Quilt Developer Wiki", "The Quilt Developer Wiki.", (String)getProject().property("wiki_path"));
		generateStaticPage("wiki/templates/404.html", defaultOptions, indexOptions, output, "404.html", "Page not found", "The Quilt Developer Wiki.", (String)getProject().property("wiki_path") + "/404");

        // Copy static files
        Path staticFiles = this.getProject().getRootDir().toPath().resolve("wiki/static");
        Files.walkFileTree(staticFiles, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path outputPath = output.resolve(staticFiles.relativize(file));
                Files.copy(file, outputPath);
                return super.visitFile(file, attrs);
            }
        });
    }

	private void generateStaticPage(String templateName, Map<String, Object> defaultOptions, Map<String, Object> indexOptions, Path output, String outputUrl, String title, String description, String url) throws IOException {
		PebbleTemplate compiled = engine.getTemplate(templateName);
		StringWriter writer = new StringWriter();
		compiled.evaluate(writer, indexOptions);
		compileHtmlFile(defaultOptions, output.resolve(outputUrl), writer.toString(), title, url, description);
		writer.close();
	}

    private void outputFile(WikiStructure.WikiEntry tree, Path parent, String sidebar, Map<String, Object> defaultOptions) throws IOException {
        // Create the path to the current tree
        Path current = parent.resolve(tree.name());
        Files.createDirectory(current);

        if (Files.exists(tree.path())) {
            // Create the wiki path
            Path output = current.resolve("index.html");
            Files.createFile(output);

            // Parse the template and write to the path
            PebbleTemplate compiled = engine.getTemplate("wiki/templates/tutorial_page.html");
            Map<String, Object> context = Map.of("content", tree.content(),
                    "sidebar", sidebar);
            context = new HashMap<>(context);
            context.putAll(defaultOptions);
            StringWriter writer = new StringWriter();
            compiled.evaluate(writer, context);
            String outputUrl = getProject().property("wiki_path") + current.toString().replace("\\", "/").replaceAll(".+/" + (String)getProject().property("output_path"), "");
            compileHtmlFile(defaultOptions, output, writer.toString(), tree.title(), outputUrl, tree.description());
            writer.close();

            // Copy the image if and only if the tree is the root entry in the project
            if (tree.isProjectRoot()) {
                Path inputImages = tree.path().getParent().getParent().resolve("images");
                if (Files.exists(inputImages)) {
                    Path outputImages = current.resolve("images");
                    Files.createDirectory(outputImages);
                    Files.walkFileTree(inputImages, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.copy(file, outputImages.resolve(inputImages.relativize(file)));
                            return super.visitFile(file, attrs);
                        }
                    });
                }
            }
        }

        for (WikiStructure.WikiSubEntry wiki : tree.wikis()) {
            outputFile(wiki, current, sidebar, defaultOptions);
        }
    }

    private void compileHtmlFile(Map<String, Object> defaultOptions, Path output, String body, String title, String url, String description) throws IOException {
        PebbleTemplate compiled = engine.getTemplate("wiki/templates/master_template.html");
        Map<String, Object> context = Map.of("body", body,
			"title", title,
			"url", url,
			"description", description);
        context = new HashMap<>(context);
        context.putAll(defaultOptions);
        Writer writer = Files.newBufferedWriter(output);
        compiled.evaluate(writer, context);
        writer.close();
    }
}
