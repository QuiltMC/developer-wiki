package quilt.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiTreeTask extends DefaultTask {
    @Internal
    protected FileEntry root;

    public GenerateWikiTreeTask() {
        setGroup("wiki");
    }

    @TaskAction
    public void generateTree() {
        List<FileEntry> subTree = new ArrayList<>();
        recurseTree(getProject(), subTree);
        root = new FileEntry("/", null, subTree, getProject());
    }

    private void recurseTree(Project root, List<FileEntry> toAdd) {
        List<Project> directSubprojects = getDirectSubprojects(root);

        for (Project subproject : directSubprojects) {
            List<FileEntry> subEntries = new ArrayList<>();
            recurseTree(subproject, subEntries);

            File index = subproject.file("markdown/" + subproject.getName() + ".md");
            if(!index.exists()){
                this.getLogger().error("[Warning]: File " + index + " does not exist. This file should exist.");
                index = null;
            }

            for (File subFile : subproject.fileTree("markdown")) {
                if (subFile.equals(index)) {
                    continue;
                }

                subEntries.add(new FileEntry(
                        getNameWithoutExtension(subFile),
                        subFile,
                        List.of(),
                        subproject));
            }
            FileEntry entry = new FileEntry(subproject.getName(), index, subEntries, subproject);

            toAdd.add(entry);
        }
    }

    private List<Project> getDirectSubprojects(Project project) {
        return project.getSubprojects().stream().filter(it -> Objects.equals(it.getParent(), project)).toList();
    }

    public record FileEntry(String name, @Nullable File file, List<FileEntry> subEntries, Project project) {
    }

    private String getNameWithoutExtension(File f) {
        String name = f.getName();
        int index = name.lastIndexOf(".");
        return index == -1 ? name : name.substring(0, index);
    }

    public FileEntry getRoot() {
        return root;
    }
}
