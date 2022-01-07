package quilt.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class WikiBuildPlugin implements Plugin<Project> {
    public static final DataHolder OPTIONS = new MutableDataSet()
            .set(HtmlRenderer.INDENT_SIZE, 2)
            .toImmutable();

    public static final Parser PARSER = Parser.builder(OPTIONS).build();
    public static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    @Override
    public void apply(Project target) {
        target.getTasks().register("generateWikiTree", GenerateWikiTreeTask.class);
        target.getTasks().register("generateSidebars", GenerateSidebarsTask.class);
        target.getTasks().register("generateContent", GenerateContentTask.class);
        target.getTasks().register("generateWiki", GenerateWikiTask.class);

//                                template = template.replace("${TITLE}", "Title Placeholder")
//                                        .replace("${CONTENT}", renderedContent)
//                                        .replace("${SIDEBAR}", sidebar);

    }
}
