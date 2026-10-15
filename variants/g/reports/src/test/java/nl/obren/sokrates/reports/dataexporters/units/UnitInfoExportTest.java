package nl.obren.sokrates.reports.dataexporters.units;

import nl.obren.sokrates.reports.dataexporters.ExportParity;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.units.UnitInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitInfoExportTest {
    // sourceFile is exported as relativeFileName, fileLinesCount and components; bodies and children are not exported
    private static final Set<String> MODEL_ONLY = new HashSet<>(Arrays.asList("sourceFile", "body", "cleanedBody", "children"));
    private static final Set<String> EXPORT_ONLY = new HashSet<>(Arrays.asList("relativeFileName", "fileLinesCount", "components"));

    @Test
    void from() {
        Set<String> shared = ExportParity.assertParity(UnitInfo.class, UnitInfoExport.class, MODEL_ONLY, EXPORT_ONLY);

        SourceFile sourceFile = new SourceFile(new File("src/A.java"), "a\nb\nc");
        sourceFile.setRelativePath("src/A.java");
        UnitInfo unit = new UnitInfo();
        unit.setSourceFile(sourceFile);
        unit.setShortName("a()");
        unit.setLongName("A.a()");
        unit.setStartLine(2);
        unit.setEndLine(3);
        unit.setLinesOfCode(4);
        unit.setMcCabeIndex(5);
        unit.setNumberOfParameters(6);
        unit.setNumberOfLiterals(7);
        unit.setNumberOfStatements(8);
        unit.setNumberOfExpressions(9);

        UnitInfoExport export = UnitInfoExport.from(unit);

        ExportParity.assertValuesCopied(unit, export, shared);
        assertEquals("src/A.java", export.getRelativeFileName());
        assertEquals(3, export.getFileLinesCount());
    }
}
