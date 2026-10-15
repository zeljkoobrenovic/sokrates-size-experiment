/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang;

import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.cleaners.CommentsAndEmptyLinesCleaner;
import nl.obren.sokrates.sourcecode.units.UnitInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Shared heuristic unit (function / procedure) extractor for languages whose units start with a keyword line
 * and end with an "end" line at the same indentation (Julia, Lua, ObjectPascal).
 * <p>
 * Subclasses provide the language-specific hooks: {@link #isFunctionStartLine(String)},
 * {@link #isUnitEnd(String, String)}, {@link #getParameterSeparatorRegex()} and {@link #getMcCabeKeywords()}.
 * The McCabe index is 1 plus the number of occurrences of each keyword in the cleaned unit body.
 */
public abstract class EndKeywordHeuristicUnitsExtractor {
    private UnitInfo unit = null;
    private StringBuilder unitBody = null;
    private StringBuilder cleanedUnitBody = null;
    private String prefix = "";
    private int loc = 0;
    private SourceFile sourceFile;
    private String cleanedFileContent;
    private Supplier<CommentsAndEmptyLinesCleaner> cleanerSupplier;

    protected EndKeywordHeuristicUnitsExtractor(SourceFile sourceFile, String cleanedFileContent, Supplier<CommentsAndEmptyLinesCleaner> cleanerSupplier) {
        this.sourceFile = sourceFile;
        this.cleanedFileContent = cleanedFileContent;
        this.cleanerSupplier = cleanerSupplier;
    }

    /** @return true if the line starts a new unit */
    protected abstract boolean isFunctionStartLine(String line);

    /** @return true if the line ends the current unit; prefix is the indentation of the unit's first line */
    protected abstract boolean isUnitEnd(String line, String prefix);

    /** @return the regex used to split the text between the first parentheses into parameters */
    protected abstract String getParameterSeparatorRegex();

    /** @return the strings counted (with StringUtils.countMatches) in the search body to compute the McCabe index */
    protected abstract List<String> getMcCabeKeywords();

    /**
     * @return the text searched for McCabe keywords, built from the cleaned unit body;
     * by default, newlines become spaces and a space is put before every "(" and "{"
     */
    protected String getBodyForMcCabeSearch(String cleanedBody) {
        String bodyForSearch = " " + cleanedBody.replace("\n", " ");
        bodyForSearch = bodyForSearch.replace("(", " (");
        bodyForSearch = bodyForSearch.replace("{", " {");
        return bodyForSearch;
    }

    /**
     * @return true if parameters are counted only when the unit's first line contains "(";
     * by default, the first parentheses anywhere in the body are used
     */
    protected boolean countParametersOnlyIfSignatureHasParenthesis() {
        return false;
    }

    public List<UnitInfo> extractUnits() {
        List<UnitInfo> units = new ArrayList<>();

        List<String> lines = Arrays.asList(cleanedFileContent.replace("\t", "    ").split("\n"));

        int lineIndex = 1;
        for (String line : lines) {
            if (isFunctionStartLine(line)) {
                initUnit(units, line, lineIndex);
            } else if (unit != null) {
                updateUnit(line);

                if (isUnitEnd(line, prefix)) {
                    endUnit(lineIndex);
                }
            }
            lineIndex++;
        }

        return units;
    }

    private void updateUnit(String line) {
        unitBody.append(line).append("\n");
        if (!line.trim().isEmpty()) {
            cleanedUnitBody.append(line).append("\n");
            loc += 1;
        }
    }

    private void endUnit(int lineIndex) {
        unit.setBody(unitBody.toString());
        unit.setCleanedBody(cleanedUnitBody.toString());
        unit.setLinesOfCode(loc);
        unit.setEndLine(lineIndex);

        updateParamsAndMcCabeIndex(unitBody.toString());
        unit = null;
        unitBody = null;
        cleanedUnitBody = null;
    }

    private void initUnit(List<UnitInfo> units, String line, int lineIndex) {
        String trimmedLine = line.trim();
        unit = new UnitInfo();
        units.add(unit);
        unit.setStartLine(lineIndex);

        unit.setSourceFile(sourceFile);

        prefix = "";
        int i = 0;
        while (line.charAt(i++) == ' ') {
            prefix += ' ';
        }

        unitBody = new StringBuilder();
        unitBody.append(line).append("\n");

        cleanedUnitBody = new StringBuilder();
        cleanedUnitBody.append(line).append("\n");

        unit.setShortName(trimmedLine.replaceAll("[(].*", "()"));
        loc = 1;
    }

    private CommentsAndEmptyLinesCleaner getCommentsAndEmptyLinesCleanerExtraString() {
        CommentsAndEmptyLinesCleaner cleaner = cleanerSupplier.get();
        cleaner.getCodeBlockParsers().forEach(codeBlockParser -> codeBlockParser.setRemoveWhenCleaning(true));
        return cleaner;
    }

    private void updateParamsAndMcCabeIndex(String body) {
        CommentsAndEmptyLinesCleaner cleaner = getCommentsAndEmptyLinesCleanerExtraString();
        String content = cleaner.cleanKeepEmptyLines(body);

        String bodyForSearch = getBodyForMcCabeSearch(content);

        boolean signatureHasParenthesis = unit.getShortName().contains("(");
        if (signatureHasParenthesis) {
            unit.setShortName(unit.getShortName().substring(0, unit.getShortName().indexOf("(")).trim() + "()");
        }
        if (signatureHasParenthesis || !countParametersOnlyIfSignatureHasParenthesis()) {
            updateNumberOfParameters(body);
        }

        int mcCabeIndex = 1;
        for (String keyword : getMcCabeKeywords()) {
            mcCabeIndex += StringUtils.countMatches(bodyForSearch, keyword);
        }

        unit.setMcCabeIndex(mcCabeIndex);
    }

    private void updateNumberOfParameters(String body) {
        int startOfParamsSearchIndex = body.indexOf("(");
        int endOfParamsSearchIndex = body.indexOf(")", startOfParamsSearchIndex + 1);
        if (startOfParamsSearchIndex > 0 && endOfParamsSearchIndex > 0 && endOfParamsSearchIndex > startOfParamsSearchIndex) {
            String trimmedParams = body.substring(startOfParamsSearchIndex + 1, endOfParamsSearchIndex).trim();
            if (!trimmedParams.isEmpty()) {
                String[] params = trimmedParams.split(getParameterSeparatorRegex());
                unit.setNumberOfParameters(params.length);
            }
        }
    }
}
