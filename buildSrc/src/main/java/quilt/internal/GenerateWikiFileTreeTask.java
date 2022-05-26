package quilt.internal;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class GenerateWikiFileTreeTask extends DefaultTask {
    @Internal
    protected FileEntry root;

    public GenerateWikiFileTreeTask() {
        setGroup("wiki");
    }

    @TaskAction
    public void generateTree() {
        List<FileEntry> subTree = new ArrayList<>();
        root = new FileEntry("/", null, subTree, getProject(), null);
        subTree.addAll(recurseTree(getProject(), root));
    }

    private List<FileEntry> recurseTree(Project root, FileEntry parent) {
        // List of entries to return
        List<FileEntry> newEntries = new ArrayList<>();

        List<Project> directSubprojects = getDirectSubprojects(root);
        for (Project subproject : directSubprojects) {
            // List of entries for the current entry being built
            List<FileEntry> subEntries = new ArrayList<>();

            // Make sure the path exists for the entry
            Path index = subproject.file("markdown/" + subproject.getName() + ".md").toPath();
            if (!Files.exists(index)) {
                this.getLogger().error("[Warning]: File " + index + " does not exist. This path should exist.");
//                index = null;
            }

            // Create the entry
            FileEntry entry = new FileEntry(subproject.getName(), index, subEntries, subproject, parent);

            // Add all the entries from subprojects
            subEntries.addAll(recurseTree(subproject, entry));

            // Add all additional entries from the project
            for (File subFile : subproject.fileTree("markdown")) {
                if (subFile.toPath().equals(index) || !subFile.getParent().endsWith("markdown")) {
                    continue;
                }
                subEntries.add(new FileEntry(getNameWithoutExtension(subFile), subFile.toPath(), List.of(), subproject, entry));
            }

            newEntries.add(entry);
        }

        return newEntries;
    }

    private List<Project> getDirectSubprojects(Project project) {
        return project.getSubprojects().stream().filter(it -> Objects.equals(it.getParent(), project)).toList();
    }

    public record FileEntry(String name, Path path, List<FileEntry> subEntries, Project project,
                            GenerateWikiFileTreeTask.FileEntry parent) {
        @Override
        public String toString() {
            return toString(0);
        }

        public String toString(int depth) {
            return "FileEntry{" + "name='" + name + '\'' + ", path=" + path + ", subEntries=[" + (subEntries.isEmpty() ? "" : "\n" + "\t".repeat(depth + 1)) + String.join(",\n" + "\t".repeat(depth + 1), subEntries.stream().map(fileEntry -> fileEntry.toString(depth + 1)).toList()) + "], " + (subEntries.isEmpty() ? "" : "\n" + "\t".repeat(depth)) + "project=" + project + '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, path, subEntries, project);
        }
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
