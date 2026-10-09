package nl.obren.sokrates.sourcecode.units;

import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.duplication.DuplicationInstance;
import nl.obren.sokrates.sourcecode.duplication.UnitDuplicatesExtractor;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzer;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzerFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Acceptance test of task c01: the text inside backtick template literals is ignored when units are
 * compared for duplication, like the text of double- and single-quoted strings already is.
 */
public class TemplateLiteralUnitDuplicationTest {
    @Test
    public void unitsThatDifferOnlyInsideTemplateLiteralsAreUnitDuplicates() {
        SourceFile alpha = jsFile("alpha.js", "alpha");
        SourceFile beta = jsFile("beta.js", "beta");
        assertNotEquals(alpha.getContent(), beta.getContent());

        List<UnitInfo> alphaUnits = extractUnits(alpha);
        List<UnitInfo> betaUnits = extractUnits(beta);
        assertEquals(1, alphaUnits.size());
        assertEquals(1, betaUnits.size());

        List<UnitInfo> allUnits = new ArrayList<>(alphaUnits);
        allUnits.addAll(betaUnits);
        List<DuplicationInstance> unitDuplicates = new UnitDuplicatesExtractor().findDuplicatedUnits(allUnits, 6);
        assertEquals(1, unitDuplicates.size(), "the two functions are reported as one duplicated unit");
        assertEquals(2, unitDuplicates.get(0).getDuplicatedFileBlocks().size());
    }

    @Test
    public void unitsThatDifferOutsideTemplateLiteralsAreStillDistinct() {
        SourceFile alpha = jsFile("alpha.js", "alpha");
        SourceFile other = new SourceFile(new File("other.js"), jsFunction("alpha").replace("parts.join('-')", "parts.join('-').trim()"));
        other.setRelativePath("other.js");

        List<UnitInfo> allUnits = new ArrayList<>(extractUnits(alpha));
        allUnits.addAll(extractUnits(other));
        assertEquals(2, allUnits.size());
        assertEquals(0, new UnitDuplicatesExtractor().findDuplicatedUnits(allUnits, 6).size(),
                "a difference in code outside the literals still tells the units apart");
    }

    private List<UnitInfo> extractUnits(SourceFile sourceFile) {
        LanguageAnalyzer analyzer = LanguageAnalyzerFactory.getInstance().getLanguageAnalyzer(sourceFile);
        return analyzer.extractUnits(sourceFile);
    }

    private SourceFile jsFile(String name, String variant) {
        SourceFile sourceFile = new SourceFile(new File(name), jsFunction(variant));
        sourceFile.setRelativePath(name);
        return sourceFile;
    }

    private String jsFunction(String variant) {
        return "function buildMessage(name, count) {\n" +
                "    const greeting = `Hello " + variant + " ${name}`;\n" +
                "    const summary = `You have ${count} " + variant + " items`;\n" +
                "    const parts = [];\n" +
                "    parts.push(greeting);\n" +
                "    parts.push(summary);\n" +
                "    const text = parts.join('-');\n" +
                "    return text;\n" +
                "}\n";
    }
}
