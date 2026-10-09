package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator;
import nl.obren.sokrates.reports.landscape.utils.Counter;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;

import java.util.Comparator;
import java.util.List;

import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.*;

class LandscapeRepositoriesHistoryTables {
    private LandscapeRepositoriesReport owner;
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private RepositoryReportUrls urls;
    private int limit;

    LandscapeRepositoriesHistoryTables(LandscapeRepositoriesReport owner, LandscapeAnalysisResults landscapeAnalysisResults, RepositoryReportUrls urls, int limit) {
        this.owner = owner;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.urls = urls;
        this.limit = limit;
    }

    void addCommitsTrend(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults, String label, String color, Counter counter) {
        report.startTable();
        report.addTableHeader("", "Repository", "Commits<br>(30d)" + (label.equalsIgnoreCase("Commits") ? "*" : ""),
                "Contributors<br>(30d)" + (label.equalsIgnoreCase("Contributors") ? "*" : ""), "Rookies<br>(30d)", label + " per Week (past year)", "Details");
        int maxCommits[] = {1};
        int pastWeeks = 52;
        repositoryAnalysisResults.forEach(repositoryAnalysis -> {
            List<ContributionTimeSlot> contributorsPerWeek = LandscapeReportGenerator.getContributionWeeks(repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getContributorsPerWeek(), pastWeeks, landscapeAnalysisResults.getLatestCommitDate());
            contributorsPerWeek.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());
            if (contributorsPerWeek.size() > pastWeeks) {
                contributorsPerWeek = contributorsPerWeek.subList(0, pastWeeks);
            }
            contributorsPerWeek.forEach(c -> maxCommits[0] = Math.max(counter.getCount(c), maxCommits[0]));
        });
        repositoryAnalysisResults.stream().limit(limit).forEach(repositoryAnalysis -> {
            report.startTableRow("white-space: nowrap");
            String name = getRepositoryDisplayHtml(repositoryAnalysis.getAnalysisResults().getMetadata().getName());
            addLangTableCell(report, repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults());
            report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                    + "<div>" + name + "</div></a>", "overflow: hidden; white-space: nowrap; vertical-align: middle; min-width: 400px; max-width: 400px");
            ContributorsAnalysisResults contributorsAnalysisResults = repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults();
            report.addTableCell(contributorsAnalysisResults.getCommitsCount30Days() + "", "text-align: center");
            List<Contributor> contributors = contributorsAnalysisResults.getContributors();
            int recentContributorsCount = (int) contributors.stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count();
            int rookiesCount = (int) contributors.stream().filter(c -> c.isRookie(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count();
            report.addTableCell(recentContributorsCount + "", "text-align: center");
            report.addTableCell(rookiesCount + "", "text-align: center");
            List<ContributionTimeSlot> contributorsPerWeek = LandscapeReportGenerator.getContributionWeeks(contributorsAnalysisResults.getContributorsPerWeek(), pastWeeks, landscapeAnalysisResults.getLatestCommitDate());
            contributorsPerWeek.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());
            if (contributorsPerWeek.size() > pastWeeks) {
                contributorsPerWeek = contributorsPerWeek.subList(0, pastWeeks);
            }
            addPerWeekCell(report, contributorsPerWeek, color, counter, maxCommits);
            report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                    + "<div style='height: 40px'>" + ReportFileExporter.getIconSvg("report", 38) + "</div></a>", "text-align: center");
            report.endTableRow();
        });

        report.endTable();

        if (limit < repositoryAnalysisResults.size()) {
            owner.addShowMoreFooter(report, repositoryAnalysisResults.size());
        }

    }

    private void addPerWeekCell(RichTextReport report, List<ContributionTimeSlot> contributorsPerWeek, String color, Counter counter, int[] maxCommits) {
        report.startTableCell("white-space: nowrap; overflow-x: hidden;");
        contributorsPerWeek.forEach(contributionTimeSlot -> {
            int h = (int) (2 + 24.0 * counter.getCount(contributionTimeSlot) / maxCommits[0]);
            report.addContentInDivWithTooltip("", +counter.getCount(contributionTimeSlot) + " in the week of " + contributionTimeSlot.getTimeSlot(),
                    "margin: 0; margin-right: -4px; padding: 0; display: inline-block; vertical-align: bottom; width: 12px; " +
                            "background-color: " + (counter.getCount(contributionTimeSlot) == 0 ? "grey" : color) + "; " +
                            "opacity: 0.5; height: " + (h + "px"));
        });
        report.endTableCell();
    }

    void addHistory(RichTextReport report, List<RepositoryAnalysisResults> repositoriesAnalysisResults, String label, String color, Counter counter) {
        report.startTable();
        report.addTableHeader("", "Repository",
                "Age", label + " per Year", "Contributors", "Commits", "Freshness", "Details");
        int pastYears = landscapeAnalysisResults.getConfiguration().getRepositoriesHistoryLimit();
        int maxCommits[] = {1};
        repositoriesAnalysisResults.forEach(repositoryAnalysis -> {
            List<ContributionTimeSlot> contributorsPerYear = LandscapeReportGenerator.getContributionYears(repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getContributorsPerYear(), pastYears, landscapeAnalysisResults.getLatestCommitDate());
            contributorsPerYear.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());
            if (contributorsPerYear.size() > pastYears) {
                contributorsPerYear = contributorsPerYear.subList(0, pastYears);
            }
            contributorsPerYear.forEach(c -> maxCommits[0] = Math.max(counter.getCount(c), maxCommits[0]));
        });

        int listCount[] = {0};
        repositoriesAnalysisResults.forEach(repositoryAnalysis -> {
            if (repositoryAnalysis.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays() == 0) {
                return;
            }
            listCount[0]++;
            if (listCount[0] > limit) return;
            addHistoryRow(report, repositoryAnalysis, color, counter, pastYears, maxCommits);
        });

        report.endTable();

        if (limit < repositoriesAnalysisResults.size()) {
            owner.addShowMoreFooter(report, repositoriesAnalysisResults.size());
        }

    }

    private void addHistoryRow(RichTextReport report, RepositoryAnalysisResults repositoryAnalysis, String color, Counter counter, int pastYears, int[] maxCommits) {
        report.startTableRow("white-space: nowrap");
        CodeAnalysisResults repositoryAnalysisAnalysisResults = repositoryAnalysis.getAnalysisResults();
        String name = getRepositoryDisplayHtml(repositoryAnalysisAnalysisResults.getMetadata().getName());

        addLangTableCell(report, repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults());

        report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                + "<div>" + name + "</div></a>", "overflow: hidden; white-space: nowrap; vertical-align: middle; min-width: 400px; max-width: 400px");

        ContributorsAnalysisResults contributorsAnalysisResults = repositoryAnalysisAnalysisResults.getContributorsAnalysisResults();
        FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisAnalysisResults.getFilesHistoryAnalysisResults();
        int ageInDays = filesHistoryAnalysisResults.getAgeInDays();
        int repositoryAgeYears = (int) Math.round(ageInDays / 365.0);
        String age = repositoryAgeYears == 0 ? ageInDays + "d" : repositoryAgeYears + "y";
        report.addTableCell(age, "text-align: center; font-size: 90%");

        List<ContributionTimeSlot> contributorsPerYear = LandscapeReportGenerator.getContributionYears(contributorsAnalysisResults.getContributorsPerYear(), pastYears, landscapeAnalysisResults.getLatestCommitDate());
        contributorsPerYear.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());
        if (contributorsPerYear.size() > pastYears) {
            contributorsPerYear = contributorsPerYear.subList(0, pastYears);
        }

        addPerYearCell(report, contributorsPerYear, filesHistoryAnalysisResults.getFirstDate(), color, counter, maxCommits);

        report.addTableCell(contributorsAnalysisResults.getContributors().size() + "", "text-align: center; font-size: 90%");
        report.addTableCell(contributorsAnalysisResults.getCommitsCount() + "", "text-align: center; font-size: 90%");
        report.addTableCell(getRiskProfileVisual(repositoryAnalysisAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileLastModifiedDistribution(), Palette.getFreshnessPalette()));
        report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                + "<div style='height: 40px'>" + ReportFileExporter.getIconSvg("report", 38) + "</div></a>", "text-align: center");
        report.endTableRow();
    }

    private void addPerYearCell(RichTextReport report, List<ContributionTimeSlot> contributorsPerYear, String firstDate, String color, Counter counter, int[] maxCommits) {
        report.startTableCell("white-space: nowrap; overflow-x: hidden;");
        String firstYear = firstDate.length() >= 4 ? firstDate.substring(0, 4) : "";
        boolean reachedFirstYear[] = {false};
        contributorsPerYear.forEach(contributionTimeSlot -> {
            int count = counter.getCount(contributionTimeSlot);
            if (count > 0) {
                int h = (int) (6 + 24.0 * count / maxCommits[0]);
                report.addContentInDivWithTooltip("", +count + " in " + contributionTimeSlot.getTimeSlot(),
                        "margin: 0; margin-right: -4px; padding: 0; display: inline-block; vertical-align: bottom; width: 16px; " +
                                "background-color: " + color + "; " +
                                "opacity: 0.5; height: " + (h + "px"));
            } else {
                if (contributionTimeSlot.getTimeSlot().substring(0, 4).compareTo(firstYear) >= 0) {
                    report.addContentInDivWithTooltip("", +count + " in the week of " + contributionTimeSlot.getTimeSlot(),
                            "margin: 0; margin-right: -4px; padding: 0; display: inline-block; vertical-align: bottom; width: 16px; " +
                                    "background-color: lightgrey; " +
                                    "opacity: 0.5; height: 5px;");
                } else if (!reachedFirstYear[0]) {
                    reachedFirstYear[0] = true;
                    report.addContentInDiv(firstDate, "color: grey; vertical-align: top; margin-left: 6px; display: inline-block; font-size: 70%");
                }
            }
        });
        report.endTableCell();
    }
}
