package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.generators.statichtml.ControlsReportGenerator;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.MetricRangeControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.*;

class LandscapeRepositoriesMetricsTable {
    private LandscapeRepositoriesReport owner;
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private RepositoryReportUrls urls;
    private int limit;

    LandscapeRepositoriesMetricsTable(LandscapeRepositoriesReport owner, LandscapeAnalysisResults landscapeAnalysisResults, RepositoryReportUrls urls, int limit) {
        this.owner = owner;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.urls = urls;
        this.limit = limit;
    }

    void addMetricsTable(RichTextReport report, List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        report.startTable();
        List<String> headers = new ArrayList<>();
        headers.addAll(Arrays.asList(new String[]{"Main<br>Lang", "Repository", "Duplication", "File Size",
                "Unit Size", "Conditional<br>Complexity", "Newness", "Freshness", "Update<br>Frequency"}));
        if (showControls()) {
            headers.add("Controls");
        }

        report.addTableHeader(headers.toArray(new String[headers.size()]));

        boolean startedInactiveSection90Days[] = {false};
        boolean startedInactiveSection180Days[] = {false};

        repositoriesAnalysisResults.stream().filter(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode() > 0).limit(limit).forEach(repositoryAnalysis -> {
            int commits90Days = repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount90Days();
            int commits180Days = repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount180Days();
            addInactiveSectionHeaders(report, commits90Days, commits180Days, startedInactiveSection90Days, startedInactiveSection180Days);
            addMetricsRow(report, repositoryAnalysis, commits90Days);
        });

        report.endTable();

        if (limit < repositoriesAnalysisResults.size()) {
            owner.addShowMoreFooter(report, repositoriesAnalysisResults.size());
        }

    }

    private void addInactiveSectionHeaders(RichTextReport report, int commits90Days, int commits180Days, boolean[] startedInactiveSection90Days, boolean[] startedInactiveSection180Days) {
        if (commits90Days == 0 && commits180Days > 0 && !startedInactiveSection90Days[0]) {
            startedInactiveSection90Days[0] = true;
            report.startTableRow("white-space: nowrap");
            report.addMultiColumnTableCell("<h3 style='margin: 0; margin-top: 14px; margin-bottom: 6px;'>Repository not active in past 90 days</h3>", 11);
            report.endTableRow();
        }
        if (commits180Days == 0 && !startedInactiveSection180Days[0]) {
            startedInactiveSection180Days[0] = true;
            report.startTableRow("white-space: nowrap");
            report.addMultiColumnTableCell("<h3 style='margin: 0; margin-top: 14px; margin-bottom: 6px;'>Repository not active in past 180 days</h3>", 11);
            report.endTableRow();
        }
    }

    private void addMetricsRow(RichTextReport report, RepositoryAnalysisResults repositoryAnalysis, int commits90Days) {
        report.startTableRow("white-space: nowrap" + (commits90Days > 0 ? "" : "; opacity: 0.7"));
        CodeAnalysisResults repositoryAnalysisResults = repositoryAnalysis.getAnalysisResults();
        String name = getRepositoryDisplayHtml(repositoryAnalysis.getAnalysisResults().getMetadata().getName());

        AspectAnalysisResults main = repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults();

        addLangTableCell(report, main);

        String locText = FormattingUtils.formatCount(main.getLinesOfCode());
        String commits90DaysText = commits90Days > 0 ? ", <b>" + FormattingUtils.formatCount(commits90Days) + "</b> commits (90d)" : "";
        report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                        + "<div>" + name + "</div><div style='color: black; font-size: 80%'><b>" + locText + "</b> LOC (main)" + commits90DaysText + "</div></a>",
                "overflow: hidden; white-space: nowrap; vertical-align: middle; max-width: 400px");

        report.addTableCell("<a href='" + urls.getDuplicationReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getDuplicationVisual(repositoryAnalysisResults.skipDuplicationAnalysis(), repositoryAnalysisResults.getDuplicationAnalysisResults().getOverallDuplication().getDuplicationPercentage()) +
                "</a>", "text-align: center");
        report.addTableCell("<a href='" + urls.getFileSizeReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getFilesAnalysisResults().getOverallFileSizeDistribution(), Palette.getRiskPalette()) +
                "</a>", "text-align: center");

        report.addTableCell("<a href='" + urls.getUnitSizeReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getUnitsAnalysisResults().getUnitSizeRiskDistribution(), Palette.getRiskPalette()) +
                "</a>", "text-align: center");
        report.addTableCell("<a href='" + urls.getConditionalComplexityReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getUnitsAnalysisResults().getConditionalComplexityRiskDistribution(), Palette.getRiskPalette()) +
                "</a>", "text-align: center");
        report.addTableCell("<a href='" + urls.getFileAgeReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileFirstModifiedDistribution(), Palette.getAgePalette()) +
                "</a>", "text-align: center");
        report.addTableCell("<a href='" + urls.getFileAgeReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileLastModifiedDistribution(), Palette.getFreshnessPalette()) +
                "</a>", "text-align: center");
        report.addTableCell("<a href='" + urls.getFileChangeFrequencyReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                getRiskProfileVisual(repositoryAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileChangeDistribution(), Palette.getHeatPalette()) +
                "</a>", "text-align: center");

        if (showControls()) {
            report.startTableCell("text-align: center; font-size: 90%");
            report.addHtmlContent("<a target='_blank' href='" + urls.getControlsReportUrl(repositoryAnalysis) + "'>");
            addControls(report, repositoryAnalysisResults);
            report.addHtmlContent("</a>");
            report.endTableCell();
        }
        report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                + "<div style='height: 40px'>" + ReportFileExporter.getIconSvg("report", 38) + "</div></a>", "text-align: center");
        report.endTableRow();
    }

    private boolean showControls() {
        return landscapeAnalysisResults.getConfiguration().isShowRepositoryControls();
    }

    private void addControls(RichTextReport report, CodeAnalysisResults analysisResults) {
        analysisResults.getControlResults().getGoalsAnalysisResults().forEach(goalsAnalysisResults -> {
            goalsAnalysisResults.getControlStatuses().forEach(status -> {
                String style = "display: inline-block; border: 2px; border-radius: 50%; height: 12px; width: 12px; background-color: " + ControlsReportGenerator.getColor(status.getStatus());
                MetricRangeControl control = status.getControl();
                String tooltip = control.getDescription() + "\n"
                        + control.getDesiredRange().getTextDescription() + "\n\n"
                        + "" + status.getMetric().getValue();
                report.addContentInDivWithTooltip(" ", tooltip, style);
            });
        });
    }
}
