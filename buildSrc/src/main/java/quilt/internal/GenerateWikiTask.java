package quilt.internal;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiTask extends DefaultTask {
    private final PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(false).build();

    public GenerateWikiTask() {
        setGroup("wiki");

        dependsOn("generateSidebars", "generateContent");
    }

    @TaskAction
    public void generateWiki() throws IOException {
        // Get the built values
        Map<GenerateWikiTreeTask.FileEntry, String> content = ((GenerateContentTask) getProject().getTasks().getByPath("generateContent")).getGenerated();
        Map<String, String> sidebars = ((GenerateSidebarsTask) getProject().getTasks().getByPath("generateSidebars")).getSidebars();
        GenerateWikiTreeTask.FileEntry root = ((GenerateWikiTreeTask) getProject().getTasks().getByPath("generateWikiTree")).getRoot();

        // Clean the output
        String outputPath = (String) getProject().property("output_path");
        getProject().delete(outputPath);
        Path output = getProject().file(outputPath).toPath();
        Files.createDirectory(output);

        // Generate the files
        for (GenerateWikiTreeTask.FileEntry subWiki : root.subEntries()) {
            outputFile(subWiki, output, content, sidebars.get(subWiki.name()));
        }

        // Copy static files
        Path staticFiles = this.getProject().getRootDir().toPath().resolve("static");
        Files.walkFileTree(staticFiles, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path outputPath = output.resolve(staticFiles.relativize(file));
                Files.copy(file, outputPath);
                return super.visitFile(file, attrs);
            }
        });
    }

    private void outputFile(GenerateWikiTreeTask.FileEntry tree, Path parent, Map<GenerateWikiTreeTask.FileEntry, String> content, String sidebar) throws IOException {
        // Create the path to the current tree
        Path current = parent.resolve(tree.name());
        Files.createDirectory(current);

        if (tree.path() != null) {
            // Create the wiki path
            Path output = current.resolve("index.html");
            Files.createFile(output);

            // Parse the template and write to the path
            PebbleTemplate compiled = engine.getTemplate("templates/tutorial_page.html");
            Map<String, Object> context = Map.of("title", tree.name(), "content", content.get(tree), "sidebar", sidebar, "wiki_path", getProject().property("wiki_path"));
            Writer writer = Files.newBufferedWriter(output);
            compiled.evaluate(writer, context);
            writer.close();

            // Copy the image if and only if the tree is the root entry in the project
            if (!tree.project().equals(tree.parent().project())) {
                Path inputImages = tree.project().file("images").toPath();
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

        // Output the sub entries
        for (GenerateWikiTreeTask.FileEntry entry : tree.subEntries()) {
            outputFile(entry, current, content, sidebar);
        }
    }
}
