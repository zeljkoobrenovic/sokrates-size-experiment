/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang.julia;

import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.lang.EndKeywordHeuristicUnitsExtractor;

import java.util.Arrays;
import java.util.List;

public class JuliaHeuristicUnitsExtractor extends EndKeywordHeuristicUnitsExtractor {
    // " \\? " is passed as is to countMatches (not a regex), so it only matches a literal " \? "
    private static final List<String> MCCABE_KEYWORDS = Arrays.asList(
            " if ", " elseif ", " ifelse ", " \\? ", " for ", " while ", " catch ", " && ", " || ");

    public JuliaHeuristicUnitsExtractor(SourceFile sourceFile, String cleanedFileContent, JuliaAnalyzer analyzer) {
        super(sourceFile, cleanedFileContent, analyzer::getCommentsAndEmptyLinesCleaner);
    }

    @Override
    protected boolean isFunctionStartLine(String line) {
        String trimmedLine = line.trim();
        return trimmedLine.startsWith("function ") || trimmedLine.startsWith("function(")
                || (trimmedLine.startsWith("@") && trimmedLine.contains(" function"));
    }

    @Override
    protected boolean isUnitEnd(String line, String prefix) {
        return line.startsWith(prefix + "end");
    }

    @Override
    protected String getParameterSeparatorRegex() {
        return "\\,";
    }

    @Override
    protected List<String> getMcCabeKeywords() {
        return MCCABE_KEYWORDS;
    }
}
