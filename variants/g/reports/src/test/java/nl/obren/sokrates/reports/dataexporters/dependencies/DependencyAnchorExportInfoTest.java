package nl.obren.sokrates.reports.dataexporters.dependencies;

import nl.obren.sokrates.reports.dataexporters.ExportParity;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.dependencies.DependencyAnchor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DependencyAnchorExportInfoTest {
    // sourceFiles are exported as files (FileExportInfo); filters are not exported
    private static final Set<String> MODEL_ONLY = new HashSet<>(Arrays.asList("sourceFiles", "filters"));
    private static final Set<String> EXPORT_ONLY = Collections.singleton("files");

    @Test
    void from() {
        Set<String> shared = ExportParity.assertParity(DependencyAnchor.class, DependencyAnchorExportInfo.class, MODEL_ONLY, EXPORT_ONLY);

        SourceFile sourceFile = new SourceFile(new File("src/A.java"), "a");
        sourceFile.setRelativePath("src/A.java");
        DependencyAnchor anchor = new DependencyAnchor("a.b");
        anchor.setCodeFragment("import a.b");
        anchor.setDependencyPatterns(Arrays.asList("import a.b.*"));
        anchor.setSourceFiles(Arrays.asList(sourceFile));

        DependencyAnchorExportInfo export = DependencyAnchorExportInfo.from(anchor);

        ExportParity.assertValuesCopied(anchor, export, shared);
        assertEquals(1, export.getFiles().size());
        assertEquals("src/A.java", export.getFiles().get(0).getRelativePath());
    }
}
