/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.generators.statichtml.ContributorsReportUtils;
import nl.obren.sokrates.reports.generators.statichtml.HistoryPerLanguageGenerator;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.HistoryPerExtension;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;

import java.util.List;

import static nl.obren.sokrates.reports.core.ReportFileExporter.getIconSvg;

class ReportIndexActivityTab {
    static void addActivityTab(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        ContributorsAnalysisResults contributorsAnalysisResults = analysisResults.getContributorsAnalysisResults();
        indexReport.startTabContentSection("commits", false);

        if (contributorsAnalysisResults.getCommitsCount() > 0) {
            indexReport.startDiv("margin: 32px; font-size: 110%");
            indexReport.addLevel2Header("Overall Activity Per Year", "");

            indexReport.addParagraph("Latest commit date: " + contributorsAnalysisResults.getLatestCommitDate() + "",
                    "color: grey; font-size: 80%; margin-bottom: 2px;");
            indexReport.addParagraph("Reference analysis date: " + DateUtils.getAnalysisDate() + "",
                    "color: grey; font-size: 80%;");

            addActivityCards(indexReport, contributorsAnalysisResults);
            addActivityPerExtension(indexReport, analysisResults);
            indexReport.endTabContentSection();
            indexReport.endDiv();
            indexReport.endDiv();
        } else {
            indexReport.addParagraph("No commit history found.", "color: grey; margin-left: 10px; margin: 15px");
        }
        indexReport.endTabContentSection();
    }

    private static void addActivityCards(RichTextReport indexReport, ContributorsAnalysisResults contributorsAnalysisResults) {
        long contributorsCount = contributorsAnalysisResults.getContributors().stream().filter(c -> !c.isBot() && c.isActive(Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS)).count();
        int commitsCount30Days = contributorsAnalysisResults.getCommitsCount30Days();

        indexReport.startTable();
        indexReport.startTableRow();

        indexReport.startTableCell("border: none; vertical-align: top;");

        indexReport.startDiv("margin-top: 8px; width: 80px; height: 81px; background-color: white; border-radius: 5px; vertical-align: middle; text-align: center");
        indexReport.startNewTabLink("Commits.html", "");
        indexReport.addContentInDiv(FormattingUtils.getSmallTextForNumber(commitsCount30Days),
                "padding-top: 12px; font-size: 36px;");
        indexReport.addContentInDiv((commitsCount30Days == 1 ? "commit" : "commits") + "<br>(30 days)", "color: black; font-size: 80%");
        indexReport.endNewTabLink();
        indexReport.endDiv();

        indexReport.startDiv("margin-top: 32px; width: 80px; height: 81px; background-color: white; border-radius: 5px; vertical-align: middle; text-align: center");
        indexReport.startNewTabLink("Contributors.html", "");
        indexReport.addContentInDiv(FormattingUtils.getSmallTextForNumber((int) contributorsCount),
                "padding-top: 12px; font-size: 36px;");
        indexReport.addContentInDiv((contributorsCount == 1 ? "contributor" : "contributors") + "<br>(30 days)", "color: black; font-size: 80%");
        indexReport.endNewTabLink();
        indexReport.endDiv();

        indexReport.endTableCell();

        indexReport.startTableCell("border: none");
        List<ContributionTimeSlot> contributorsPerYear = contributorsAnalysisResults.getContributorsPerYear();
        ContributorsReportUtils.addContributorsPerTimeSlot(indexReport, contributorsPerYear, 20, true, true, 8, commitsCount30Days == 0);
        indexReport.endTableCell();
        indexReport.endTableRow();
        indexReport.endTable();
    }

    private static void addActivityPerExtension(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        indexReport.startDiv("font-size: 110%");
        indexReport.addLineBreak();
        indexReport.addLevel3Header("Activity Per File Extension");
        indexReport.startTable();
        indexReport.startTableRow();
        indexReport.addTableCell(getIconSvg("commits") + "<div style='font-size: 80%'>commits</div>", "border: none; text-align: center");
        indexReport.startTableCell("border: none");
        List<HistoryPerExtension> historyPerExtensionPerYear = analysisResults.getFilesHistoryAnalysisResults().getHistoryPerExtensionPerYear();
        List<String> extensions = analysisResults.getMainAspectAnalysisResults().getExtensions();
        HistoryPerLanguageGenerator.getInstanceCommits(historyPerExtensionPerYear, extensions).addHistoryPerLanguage(indexReport);
        indexReport.endTableCell();
        indexReport.endTableRow();
        indexReport.startTableRow();
        indexReport.addTableCell("&nbsp;", "border: none");
        indexReport.addTableCell("&nbsp;", "border: none");
        indexReport.endTableRow();
        indexReport.startTableRow();
        indexReport.addTableCell(getIconSvg("contributors") + "<div style='font-size: 80%'>contributors</div>", "border: none; text-align: center");
        indexReport.startTableCell("border: none");
        HistoryPerLanguageGenerator.getInstanceContributors(historyPerExtensionPerYear, extensions).addHistoryPerLanguage(indexReport);
        indexReport.endTableCell();
        indexReport.endTableRow();
        indexReport.endTable();
    }
}
