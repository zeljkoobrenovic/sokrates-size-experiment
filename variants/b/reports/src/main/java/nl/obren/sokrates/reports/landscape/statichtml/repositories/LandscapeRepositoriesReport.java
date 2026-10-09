package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.RepositoryTag;
import nl.obren.sokrates.sourcecode.landscape.TagGroup;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.addLangTableCell;
import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.getRepositoryDisplayHtml;

public class LandscapeRepositoriesReport {
    private static final Log LOG = LogFactory.getLog(LandscapeRepositoriesReport.class);

    private LandscapeAnalysisResults landscapeAnalysisResults;
    private int limit = 1000;
    private TagMap customTagsMap;
    private String link;
    private String linkLabel;
    private File reportsFolder;
    private List<TagGroup> tagGroups = new ArrayList<>();

    private String type = "";

    private RepositoryReportUrls urls;
    private LandscapeRepositoriesSummaryGraphs summaryGraphs;
    private LandscapeRepositoriesMetricsTable metricsTable;
    private LandscapeRepositoriesHistoryTables historyTables;
    private LandscapeRepositoriesFeaturesTable featuresTable;

    public LandscapeRepositoriesReport(LandscapeAnalysisResults landscapeAnalysisResults, int limit, TagMap customTagsMap) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.limit = limit;
        this.customTagsMap = customTagsMap;
        this.type = "long report";
        initHelpers();
    }

    public LandscapeRepositoriesReport(LandscapeAnalysisResults landscapeAnalysisResults, int limit, String link, String linkLabel, TagMap customTagsMap) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.limit = limit;
        this.link = link;
        this.linkLabel = linkLabel;
        this.customTagsMap = customTagsMap;
        this.type = "short report";
        initHelpers();
    }

    private void initHelpers() {
        this.urls = new RepositoryReportUrls(landscapeAnalysisResults);
        this.summaryGraphs = new LandscapeRepositoriesSummaryGraphs(landscapeAnalysisResults);
        this.metricsTable = new LandscapeRepositoriesMetricsTable(this, landscapeAnalysisResults, urls, limit);
        this.historyTables = new LandscapeRepositoriesHistoryTables(this, landscapeAnalysisResults, urls, limit);
        this.featuresTable = new LandscapeRepositoriesFeaturesTable(this, landscapeAnalysisResults, urls, limit);
    }

    public void saveRepositoriesReport(RichTextReport report, File reportsFolder, List<RepositoryAnalysisResults> repositoryAnalysisResults, List<TagGroup> tagGroups) {
        ProcessingStopwatch.start("reporting/repositories/" + type + "/preparing");
        this.reportsFolder = reportsFolder;
        this.tagGroups = tagGroups;
        report.startTabGroup();
        boolean showCommits = landscapeAnalysisResults.getCommitsCount() > 0;
        if (showCommits) {
            report.addTab("commitsTrend", "Commits Trend", true);
            report.addTab("contributorsTrend", "Contributors Trend", false);
        }
        report.addTab("repositories", "Size & Details", !showCommits);
        if (showCommits) {
            report.addTab("history", "History", false);
            report.addTab("newest", "Creation", false);
        }
        report.addTab("metrics", "Metrics", false);
        report.addTab("features", "Features of Interest", false);
        report.addLinkInTab("Explorer...", "repositories-explorer.html");
        report.addLinkInTab("Files...", "files-explorer.html");
        report.endTabGroup();
        ProcessingStopwatch.end("reporting/repositories/" + type + "/preparing");

        ProcessingStopwatch.start("reporting/repositories/" + type + "/size");
        addRepositoriesDetails(report, repositoryAnalysisResults);
        ProcessingStopwatch.end("reporting/repositories/" + type + "/size");
        if (showCommits) {
            ProcessingStopwatch.start("reporting/repositories/" + type + "/commits");
            addCommitBasedLists(report, repositoryAnalysisResults);
            ProcessingStopwatch.end("reporting/repositories/" + type + "/commits");
            ProcessingStopwatch.start("reporting/repositories/" + type + "/history");
            addHistory(report, repositoryAnalysisResults);
            addNewest(report, repositoryAnalysisResults);
            ProcessingStopwatch.end("reporting/repositories/" + type + "/history");
        }

        report.startTabContentSection("metrics", false);
        ProcessingStopwatch.start("reporting/repositories/" + type + "/metrics");
        addMetrics(report, repositoryAnalysisResults);
        ProcessingStopwatch.end("reporting/repositories/" + type + "/metrics");
        report.endTabContentSection();

        report.startTabContentSection("features", false);
        ProcessingStopwatch.start("reporting/repositories/" + type + "/features of interest");
        addFeaturesOfInterest(report);
        ProcessingStopwatch.end("reporting/repositories/" + type + "/features of interest");
        report.endTabContentSection();
    }

    private void addFeaturesOfInterest(RichTextReport report) {
        featuresTable.addFeaturesOfInterest(report);
    }

    public void addMetrics(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> b.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount()
                        - a.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount());
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> b.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount180Days()
                        - a.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount180Days());
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> b.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount90Days()
                        - a.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount90Days());
        metricsTable.addMetricsTable(report, repositoryAnalysisResults);
    }

    public void addHistory(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startTabContentSection("history", false);
        List<RepositoryAnalysisResults> sorted = new ArrayList<>(repositoryAnalysisResults);
        Collections.sort(sorted,
                (a, b) -> b.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays()
                        - a.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays());
        addSummaryGraphHistory(report, sorted);
        historyTables.addHistory(report, sorted, "Commits", "blue", (slot) -> slot.getCommitsCount());
        report.endTabContentSection();
    }

    public void addNewest(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startTabContentSection("newest", false);
        List<RepositoryAnalysisResults> sorted = new ArrayList<>(repositoryAnalysisResults);
        Collections.sort(sorted,
                (a, b) -> a.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays()
                        - b.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays());
        addSummaryGraphNewReposPerYear(report, sorted);
        historyTables.addHistory(report, sorted, "Commits", "blue", (slot) -> slot.getCommitsCount());
        report.endTabContentSection();
    }

    public void addCommitBasedLists(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startTabContentSection("commitsTrend", true);
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> b.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days()
                        - a.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days());
        addSummaryGraphCommits(report, repositoryAnalysisResults);
        historyTables.addCommitsTrend(report, repositoryAnalysisResults, "Commits", "blue", (slot) -> slot.getCommitsCount());
        report.endTabContentSection();
        report.startTabContentSection("contributorsTrend", false);
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> (int) b.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count()
                        - (int) a.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count());
        addSummaryGraphContributors(report, repositoryAnalysisResults);
        historyTables.addCommitsTrend(report, repositoryAnalysisResults, "Contributors", "darkred", (slot) -> slot.getContributorsCount());
        report.endTabContentSection();
    }

    public void addSummaryGraphCommits(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        summaryGraphs.addSummaryGraphCommits(report, repositoryAnalysisResults);
    }

    public void addSummaryGraphHistory(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        summaryGraphs.addSummaryGraphHistory(report, repositoryAnalysisResults);
    }

    public void addSummaryGraphNewReposPerYear(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        summaryGraphs.addSummaryGraphNewReposPerYear(report, repositoryAnalysisResults);
    }

    public void addSummaryGraphContributors(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        summaryGraphs.addSummaryGraphContributors(report, repositoryAnalysisResults);
    }

    public void addSummaryGraphMainLoc(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        summaryGraphs.addSummaryGraphMainLoc(report, repositoryAnalysisResults);
    }


    private boolean showTags() {
        for (TagGroup tagGroup : tagGroups) {
            if (tagGroup.getRepositoryTags().size() > 0) return true;
        }
        return false;
    }

    public void addRepositoriesDetails(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        Collections.sort(repositoryAnalysisResults,
                (a, b) -> b.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()
                        - a.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode());

        boolean showCommits = landscapeAnalysisResults.getCommitsCount() > 0;
        report.startTabContentSection("repositories", !showCommits);
        addSummaryGraphMainLoc(report, repositoryAnalysisResults);
        report.startTable("width: 100%");
        int thresholdContributors = landscapeAnalysisResults.getConfiguration().getRepositoryThresholdContributors();
        List<String> headers = new ArrayList<>(Arrays.asList("", "Repository" + (thresholdContributors > 1 ? "<br/>(" + thresholdContributors + "+&nbsp;contributors)" : ""),
                "LOC<br/>(main)*",
                "LOC<br/>(test)", "LOC<br/>(other)",
                "Age", "Latest<br>Commit Date",
                "Contributors<br>(30d)", "Rookies<br>(30d)", "Commits<br>(30d)"));
        headers.add("Report");
        if (showTags()) {
            headers.add("Tags");
        }
        report.addTableHeader(headers.toArray(String[]::new));

        repositoryAnalysisResults.stream().limit(limit).forEach(repositoryAnalysis -> {
            addRepositoryRow(report, repositoryAnalysis);
        });


        report.endTable();
        if (limit < repositoryAnalysisResults.size()) {
            addShowMoreFooter(report, repositoryAnalysisResults.size());
        }
        report.endTabContentSection();
    }

    void addShowMoreFooter(RichTextReport report, int totalSize) {
        report.startDiv("color:grey; font-size: 90%; margin-top: 16px;");
        report.addParagraph("The list is limited to " + limit +
                " items (out of " + totalSize + ").", "margin-left: 11px");
        if (link != null && linkLabel != null) {
            report.startDiv("margin-left: 10px; margin-bottom: 12px;");
            report.addNewTabLink(link, linkLabel);
            report.endDiv();
        }
        report.endDiv();
    }

    private void addRepositoryRow(RichTextReport report, RepositoryAnalysisResults repositoryAnalysis) {
        CodeAnalysisResults analysisResults = repositoryAnalysis.getAnalysisResults();
        Metadata metadata = analysisResults.getMetadata();

        String latestCommitDate = repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getLatestCommitDate();
        report.startTableRow("white-space: nowrap;" + (DateUtils.isCommittedLessThanDaysAgo(latestCommitDate, 90) ? ""
                : (DateUtils.isCommittedLessThanDaysAgo(latestCommitDate, 180) ? "color:#b0b0b0" : "color:#c3c3c3")));
        addLangTableCell(report, repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults());
        String name = getRepositoryDisplayHtml(metadata.getName());
        report.addTableCell("<a href='" + urls.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>"
                + "<div>" + name + "</div></a>", "overflow: hidden; white-space: nowrap; vertical-align: middle; min-width: 400px; max-width: 400px");
        AspectAnalysisResults main = analysisResults.getMainAspectAnalysisResults();
        AspectAnalysisResults test = analysisResults.getTestAspectAnalysisResults();
        AspectAnalysisResults generated = analysisResults.getGeneratedAspectAnalysisResults();
        AspectAnalysisResults build = analysisResults.getBuildAndDeployAspectAnalysisResults();
        AspectAnalysisResults other = analysisResults.getOtherAspectAnalysisResults();

        int thresholdCommits = landscapeAnalysisResults.getConfiguration().getContributorThresholdCommits();
        List<Contributor> contributors = analysisResults.getContributorsAnalysisResults().getContributors()
                .stream().filter(c -> c.getCommitsCount() >= thresholdCommits).collect(Collectors.toCollection(ArrayList::new));

        int recentContributorsCount = (int) contributors.stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count();
        int rookiesCount = (int) contributors.stream().filter(c -> c.isRookie(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count();

        report.addTableCell(FormattingUtils.formatCount(main.getLinesOfCode(), "-"), "text-align: center; font-size: 90%");

        report.addTableCell(FormattingUtils.formatCount(test.getLinesOfCode(), "-"), "text-align: center; font-size: 90%");
        report.addTableCell(FormattingUtils.formatCount(generated.getLinesOfCode() + build.getLinesOfCode() + other.getLinesOfCode(), "-"), "text-align: center; font-size: 90%");
        int repositoryAgeYears = (int) Math.round(analysisResults.getFilesHistoryAnalysisResults().getAgeInDays() / 365.0);
        String age = repositoryAgeYears == 0 ? "<1y" : repositoryAgeYears + "y";
        report.addTableCell(age, "text-align: center; font-size: 90%");
        report.addTableCell(latestCommitDate, "text-align: center; font-size: 90%");
        report.addTableCell(FormattingUtils.formatCount(recentContributorsCount, "-"), "text-align: center; font-size: 90%");
        report.addTableCell(FormattingUtils.formatCount(rookiesCount, "-"), "text-align: center; font-size: 90%");
        report.addTableCell(FormattingUtils.formatCount(analysisResults.getContributorsAnalysisResults().getCommitsCount30Days(), "-"), "text-align: center; font-size: 90%");
        String repositoryReportUrl = urls.getRepositoryReportUrl(repositoryAnalysis);
        report.addTableCell("<a href='" + repositoryReportUrl + "' target='_blank'>"
                + "<div style='height: 40px'>" + ReportFileExporter.getIconSvg("report", 40) + "</div></a>", "text-align: center; font-size: 90%");
        if (showTags()) {
            report.addTableCell(getTags(repositoryAnalysis), "color: black");
        }
        report.endTableRow();
    }

    private String getTags(RepositoryAnalysisResults repository) {
        StringBuilder tagsHtml = new StringBuilder();

        customTagsMap.getRepositoryTags(repository).forEach(tag -> {
            tagsHtml.append("<div style='margin: 2px; padding: 5px; display: inline-block; background-color: " + getTabColor(tag) + "; font-size: 70%; border-radius: 10px'>");
            tagsHtml.append(tag.getTag());
            tagsHtml.append("</div>");
        });

        return tagsHtml.toString();
    }

    private String getTabColor(RepositoryTag tag) {
        return tag.getGroup() != null && StringUtils.isNotBlank(tag.getGroup().getColor()) ? tag.getGroup().getColor() : "#99badd";
    }

}
