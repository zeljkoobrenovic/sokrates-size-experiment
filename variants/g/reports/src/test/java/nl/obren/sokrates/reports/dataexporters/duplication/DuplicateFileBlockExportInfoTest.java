package nl.obren.sokrates.reports.dataexporters.duplication;

import nl.obren.sokrates.reports.dataexporters.ExportParity;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.duplication.DuplicatedFileBlock;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateFileBlockExportInfoTest {
    // sourceFile is exported as file (FileExportInfo); blockSize and percentage are derived and not exported
    private static final Set<String> MODEL_ONLY = new HashSet<>(Arrays.asList("sourceFile", "blockSize", "percentage"));
    private static final Set<String> EXPORT_ONLY = Collections.singleton("file");

    @Test
    void from() {
        Set<String> shared = ExportParity.assertParity(DuplicatedFileBlock.class, DuplicateFileBlockExportInfo.class, MODEL_ONLY, EXPORT_ONLY);

        SourceFile sourceFile = new SourceFile(new File("src/A.java"), "a\nb\nc");
        sourceFile.setRelativePath("src/A.java");
        DuplicatedFileBlock fileBlock = new DuplicatedFileBlock();
        fileBlock.setSourceFile(sourceFile);
        fileBlock.setSourceFileCleanedLinesOfCode(10);
        fileBlock.setStartLine(2);
        fileBlock.setEndLine(8);
        fileBlock.setCleanedStartLine(1);
        fileBlock.setCleanedEndLine(6);

        DuplicateFileBlockExportInfo export = DuplicateFileBlockExportInfo.from(fileBlock);

        ExportParity.assertValuesCopied(fileBlock, export, shared);
        assertEquals("src/A.java", export.getFile().getRelativePath());
    }
}
