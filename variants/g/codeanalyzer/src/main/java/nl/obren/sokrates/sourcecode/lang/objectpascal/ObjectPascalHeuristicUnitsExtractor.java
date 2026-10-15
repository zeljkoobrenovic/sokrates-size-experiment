/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang.objectpascal;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.lang.EndKeywordHeuristicUnitsExtractor;

import java.util.Arrays;
import java.util.List;

public class ObjectPascalHeuristicUnitsExtractor extends EndKeywordHeuristicUnitsExtractor {
    // the search body is lower-cased, so the upper-case " AND" and " OR " never match
    private static final List<String> MCCABE_KEYWORDS = Arrays.asList(
            " if ", " repeat ", " while ", " for ", " case ", " AND", " OR ");

    public ObjectPascalHeuristicUnitsExtractor(SourceFile sourceFile, String cleanedFileContent, ObjectPascalAnalyzer analyzer) {
        super(sourceFile, cleanedFileContent, analyzer::getCommentsAndEmptyLinesCleaner);
    }

    @Override
    protected boolean isFunctionStartLine(String line) {
        String trimmedLine = line.trim().replace("\t", " ");
        return RegexUtils.matchesEntirely("(class[ ]+)?(procedure|function|method)[ ]+([a-zA-Z0-9_]+[.])+[a-zA-Z0-9_]+[ ]*(\\(|;).*", trimmedLine);
    }

    @Override
    protected boolean isUnitEnd(String line, String prefix) {
        return line.equals(prefix + "end;");
    }

    @Override
    protected String getParameterSeparatorRegex() {
        return "\\;";
    }

    @Override
    protected List<String> getMcCabeKeywords() {
        return MCCABE_KEYWORDS;
    }

    // unlike Julia and Lua, no space is put before "{" and the body is lower-cased
    @Override
    protected String getBodyForMcCabeSearch(String cleanedBody) {
        String bodyForSearch = " " + cleanedBody.replace("\n", " ");
        bodyForSearch = bodyForSearch.replace("(", " (");
        return bodyForSearch.toLowerCase();
    }

    @Override
    protected boolean countParametersOnlyIfSignatureHasParenthesis() {
        return true;
    }
}
