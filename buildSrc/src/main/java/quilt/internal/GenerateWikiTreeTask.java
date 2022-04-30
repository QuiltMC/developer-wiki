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
                if (subFile.equals(index) || !subFile.getParent().endsWith("markdown")) {
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

    public static final class FileEntry {
        private final String name;
        @Nullable
        private final File file;
        private final List<FileEntry> subEntries;
        private final Project project;
        private FileEntry parent;

        public FileEntry(String name, @Nullable File file, List<FileEntry> subEntries, Project project) {
            this.name = name;
            this.file = file;
            this.subEntries = subEntries;
            this.project = project;
            this.subEntries.forEach(sub -> sub.setParent(this));
        }

        private void setParent(FileEntry parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return toString(0);
        }

        public String toString(int depth) {
            return "FileEntry{" +
                   "name='" + name + '\'' +
                   ", file=" + file +
                   ", subEntries=[" + (subEntries.isEmpty() ? "" : "\n" + "\t".repeat(depth + 1)) + String.join(",\n" + "\t".repeat(depth + 1), subEntries.stream().map(fileEntry -> fileEntry.toString(depth + 1)).toList()) +
                   "], " + (subEntries.isEmpty() ? "" : "\n" + "\t".repeat(depth)) + "project=" + project +
                   '}';
        }

        public String name() {
            return name;
        }

        @Nullable
        public File file() {
            return file;
        }

        public FileEntry parent() {
            return parent;
        }

        public List<FileEntry> subEntries() {
            return subEntries;
        }

        public Project project() {
            return project;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (FileEntry) obj;
            return Objects.equals(this.name, that.name) &&
                   Objects.equals(this.file, that.file) &&
                   Objects.equals(this.subEntries, that.subEntries) &&
                   Objects.equals(this.project, that.project) &&
                   Objects.equals(this.parent, that.parent);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, file, subEntries, project, parent);
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
