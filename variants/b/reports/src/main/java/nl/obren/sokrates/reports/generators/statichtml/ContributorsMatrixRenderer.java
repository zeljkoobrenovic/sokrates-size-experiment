/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

class ContributorsMatrixRenderer {
    private RichTextReport report;

    ContributorsMatrixRenderer(RichTextReport report) {
        this.report = report;
    }

    void addMatrix(List<Contributor> contributors, String type) {
        report.addContentInDiv("&nbsp;", "height: 20px");
        report.startSubSection(type + " Matrix (Per Month)", "");
        report.startDiv("width: 100%; overflow-x: scroll; overflow-y: scroll; max-height: 600px");
        report.startTable();

        final List<String> pastMonths = DateUtils.getPastMonths(24, DateUtils.getAnalysisDate());
        addMatrixHeaderRow(contributors, pastMonths);

        Collections.sort(contributors, (a, b) -> {
            if (b.getLatestCommitDate().equals(a.getLatestCommitDate())) {
                if (b.getCommitsCount30Days() == a.getCommitsCount30Days()) {
                    return b.getCommitsCount() - a.getCommitsCount();
                } else {
                    return b.getCommitsCount30Days() - a.getCommitsCount30Days();
                }
            } else {
                return b.getLatestCommitDate().compareTo(a.getLatestCommitDate());
            }
        });

        int listLimit = 500;
        contributors.subList(0, contributors.size() > listLimit ? listLimit : contributors.size()).forEach(contributor -> {
            addMatrixRow(contributor, pastMonths);
        });
        report.endTable();
        if (contributors.size() > 500) {
            report.addParagraph("Top 500 (out of " + contributors.size() + ") items shows.");
        }
        report.endDiv();
        report.endSection();
    }

    private void addMatrixHeaderRow(List<Contributor> contributors, List<String> pastMonths) {
        report.startTableRow();
        report.addTableCell("", "min-width: 200px; border: none; border: none");
        report.addTableCell("Commits<br>(3m)", "max-width: 100px; text-align: center; border: none");
        report.addTableCell("Commit<br>Days", "max-width: 100px; text-align: center; border: none");
        pastMonths.forEach(pastMonth -> {
            report.startTableCell("font-size: 70%; border: none; color: lightgrey; text-align: center");
            int count[] = {0};
            contributors.forEach(contributor -> {
                boolean active[] = {false};
                contributor.getCommitDates().forEach(date -> {
                    String month = DateUtils.getMonth(date);
                    if (month.equals(pastMonth)) {
                        active[0] = true;
                        return;
                    }
                });
                if (active[0]) {
                    count[0] += 1;
                }
            });
            String tooltip = "Month " + pastMonth + ": " + (count[0] + (count[0] == 1 ? " contributor" : " contributors "));
            report.addContentInDivWithTooltip(count[0] == 0 ? "-" : (count[0] + ""), tooltip, "text-align: center");
            report.endTableCell();
        });
        report.endTableRow();
    }

    private void addMatrixRow(Contributor contributor, List<String> pastMonths) {
        report.startTableRow();
        String textOpacity = contributor.getCommitsCount90Days() > 0 ? "font-weight: bold;" : "opacity: 0.4";
        report.startTableCell("border: none; " + textOpacity);
        if (StringUtils.isNotBlank(contributor.getEmail()) && StringUtils.isNotBlank(contributor.getUserName())) {
            report.addHtmlContent(contributor.getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + contributor.getEmail() + "&gt;</div>");
        } else {
            report.addHtmlContent((contributor.getUserName() + contributor.getEmail()).trim());
        }
        report.endTableCell();
        report.addTableCell(contributor.getCommitsCount90Days() > 0 ? contributor.getCommitsCount90Days() + "" : "-", "text-align: center; border: none; " + textOpacity);
        report.addTableCell(contributor.getCommitDates().size() + "", "text-align: center; border: none; " + textOpacity);
        int index[] = {0};
        pastMonths.forEach(pastMonth -> {
            int count[] = {0};
            contributor.getCommitDates().forEach(date -> {
                String month = DateUtils.getMonth(date);
                if (month.equals(pastMonth)) {
                    count[0] += 1;
                }
            });
            index[0] += 1;
            report.startTableCell("text-align: center; padding: 0; border: none; vertical-align: middle;");
            if (count[0] > 0) {
                int size = 10 + (count[0] / 4) * 4;
                String tooltip = "Month " + pastMonth + ": " + count[0] + (count[0] == 1 ? " commit day" : " commit days");
                String opacity = "" + Math.max(0.9 - (index[0] - 1) * 0.2, 0.2);
                report.addContentInDivWithTooltip("", tooltip,
                        "padding: 0; margin: 0; display: inline-block; background-color: #483D8B; opacity: " + opacity + "; border-radius: 50%; width: " + size + "px; height: " + size + "px;");
            } else {
                report.addContentInDiv("-", "color: lightgrey; font-size: 80%");
            }
            report.endTableCell();
        });
        report.endTableRow();
    }
}
