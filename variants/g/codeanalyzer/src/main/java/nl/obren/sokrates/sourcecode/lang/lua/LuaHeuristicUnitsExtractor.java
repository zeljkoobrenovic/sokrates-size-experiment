/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang.lua;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.lang.EndKeywordHeuristicUnitsExtractor;

import java.util.Arrays;
import java.util.List;

public class LuaHeuristicUnitsExtractor extends EndKeywordHeuristicUnitsExtractor {
    private static final List<String> MCCABE_KEYWORDS = Arrays.asList(
            " if ", " elseif ", " while ", " repeat ", " for ", " && ", " || ");

    public LuaHeuristicUnitsExtractor(SourceFile sourceFile, String cleanedFileContent, LuaAnalyzer analyzer) {
        super(sourceFile, cleanedFileContent, analyzer::getCommentsAndEmptyLinesCleaner);
    }

    @Override
    protected boolean isFunctionStartLine(String line) {
        String trimmedLine = line.trim();
        return (trimmedLine.startsWith("function ") || trimmedLine.startsWith("function(")
                || RegexUtils.matchesEntirely(".*[=][ ]*function[ ]*[(].*", trimmedLine))
                && !(trimmedLine.endsWith("end)") || trimmedLine.endsWith("end"));
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
