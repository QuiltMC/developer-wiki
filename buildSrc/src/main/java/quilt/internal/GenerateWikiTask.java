package quilt.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiTask extends DefaultTask {
    public GenerateWikiTask() {
        setGroup("wiki");

        dependsOn("generateSidebars", "generateContent");
    }

    @TaskAction
    public void generateWiki() throws IOException {
        Map<File, String> content = ((GenerateContentTask) getProject().getTasks().getByPath("generateContent")).getGenerated();
        Map<String, String> sidebars = ((GenerateSidebarsTask) getProject().getTasks().getByPath("generateSidebars")).getSidebars();
        GenerateWikiTreeTask.FileEntry root = ((GenerateWikiTreeTask) getProject().getTasks().getByPath("generateWikiTree")).getRoot();

        getProject().delete("output");
        File output = getProject().file("output");
        output.mkdir();

        List<GenerateWikiTreeTask.FileEntry> subWikis = root.subEntries();

        for (GenerateWikiTreeTask.FileEntry subWiki : subWikis) {
            outputFile(subWiki, output, content, sidebars.get(subWiki.name()));
        }
    }

    private void outputFile(GenerateWikiTreeTask.FileEntry tree, File parent, Map<File, String> content, String sidebar) throws IOException {
        File current = new File(parent, tree.name());
        current.mkdirs();

        if (tree.file() != null) {
            File output = new File(current, "index.html");
            output.createNewFile();

            String template = Files.readString(getProject().file("templates/tutorial_page.html").toPath());

            template = template.replace("${TITLE}", tree.name())
                    .replace("${CONTENT}", content.get(tree.file()))
                    .replace("${SIDEBAR}", sidebar);

            Files.writeString(output.toPath(), template);

            File[] images = tree.project().file("images").listFiles();
            if (!tree.project().equals(tree.parent().project()) && images != null) {
                Files.createDirectory(current.toPath().resolve("images"));
                for (File image : images) {
                    Files.copy(image.toPath(), current.toPath().resolve("images").resolve(tree.project().file("images").toPath().relativize(image.toPath())));
                }
            }
        }

        for (GenerateWikiTreeTask.FileEntry entry : tree.subEntries()) {
            outputFile(entry, current, content, sidebar);
        }
    }
}
