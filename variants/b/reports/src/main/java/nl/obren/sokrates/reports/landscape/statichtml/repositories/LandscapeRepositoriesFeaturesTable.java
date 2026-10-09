package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.FeaturesOfInterestAggregator;
import nl.obren.sokrates.reports.landscape.utils.RepositoryConcernData;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;

import java.util.List;

import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.addLangTableCell;
import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.getRepositoryDisplayHtml;

class LandscapeRepositoriesFeaturesTable {
    private LandscapeRepositoriesReport owner;
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private RepositoryReportUrls urls;
    private int limit;

    LandscapeRepositoriesFeaturesTable(LandscapeRepositoriesReport owner, LandscapeAnalysisResults landscapeAnalysisResults, RepositoryReportUrls urls, int limit) {
        this.owner = owner;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.urls = urls;
        this.limit = limit;
    }

    void addFeaturesOfInterest(RichTextReport report) {
        List<RepositoryAnalysisResults> repositoryAnalysisResults = landscapeAnalysisResults.getRepositoryAnalysisResults();

        FeaturesOfInterestAggregator aggregator = new FeaturesOfInterestAggregator(repositoryAnalysisResults);
        aggregator.aggregateFeaturesOfInterest(limit);

        List<List<RepositoryConcernData>> concerns = aggregator.getConcerns();
        List<List<RepositoryConcernData>> repositories = aggregator.getRepositories();

        if (concerns.size() == 0) {
            report.addParagraph("No features of interest found in repositories.");
            return;
        }

        report.startTable();

        addHeaderRows(report, aggregator, concerns);

        repositories.forEach(repository -> {
            addRepositoryRow(report, aggregator, concerns, repository);
        });

        report.endTable();
        if (limit < aggregator.getRepositoriesMap().size()) {
            owner.addShowMoreFooter(report, aggregator.getRepositoriesMap().size());
        }
    }

    private void addHeaderRows(RichTextReport report, FeaturesOfInterestAggregator aggregator, List<List<RepositoryConcernData>> concerns) {
        report.startTableRow("white-space: nowrap");
        report.addTableCell("", "border: none");
        report.addTableCell("", "border: none");
        concerns.stream().filter(concern -> concern.size() > 0).forEach(concern -> {
            report.startTableCellColSpan(2, "");
            report.addContentInDiv(concern.get(0).getConcern().getName() + " (" + concern.size() + ")", "text-align: center");
            report.endTableCell();
        });
        report.addTableCell("", "border: none");
        report.endTableRow();

        report.startTableRow("white-space: nowrap");
        report.addTableCell("", "border-left: none; border-top: none");
        report.addTableCell("Repositories (" + aggregator.getRepositoriesMap().size() + ")", "");
        concerns.stream().filter(concern -> concern.size() > 0).forEach(concern -> {
            int concernsCount = concern.stream().mapToInt(c -> c.getConcern().getNumberOfRegexLineMatches()).reduce((a, b) -> a + b).orElse(0);
            report.addTableCell(concernsCount + " matches", "font-size: 70%; text-align: center;");
            int filesCount = concern.stream().mapToInt(c -> c.getConcern().getFilesCount()).reduce((a, b) -> a + b).orElse(0);
            report.addTableCell(filesCount + " files", "font-size: 70%; text-align: center;");
        });
        report.addTableCell("Details", "font-size: 70%");
        report.endTableRow();
    }

    private void addRepositoryRow(RichTextReport report, FeaturesOfInterestAggregator aggregator, List<List<RepositoryConcernData>> concerns, List<RepositoryConcernData> repository) {
        report.startTableRow("white-space: nowrap");
        RepositoryConcernData repositoryConcernData = repository.get(0);
        addLangTableCell(report, repository.get(0).getRepository().getAnalysisResults().getMainAspectAnalysisResults());
        String repositoryName = repositoryConcernData.getRepositoryName();
        String name = getRepositoryDisplayHtml(repositoryName);

        report.addTableCell("<a href='" + urls.getFeaturesReportUrl(repositoryConcernData.getRepository()) + "' target='_blank'>"
                + "" + name + "</a>", "");
        concerns.stream().filter(concern -> concern.size() > 0).forEach(concern -> {
            String key = repositoryName + "::" + concern.get(0).getConcern().getName();
            int instancesCount = aggregator.getRepositoriesConcernMap().containsKey(key) ? aggregator.getRepositoriesConcernMap().get(key).getConcern().getNumberOfRegexLineMatches() : 0;
            report.addTableCell("" + (instancesCount > 0 ? instancesCount : "-"), "text-align: center;");
            int filesCount = aggregator.getRepositoriesConcernMap().containsKey(key) ? aggregator.getRepositoriesConcernMap().get(key).getConcern().getFilesCount() : 0;
            report.addTableCell("" + (filesCount > 0 ? filesCount : "-"), "text-align: center;");
        });
        report.addTableCell("<a href='" + urls.getFeaturesReportUrl(repositoryConcernData.getRepository()) + "' target='_blank'>"
                + "<div style='height: 40px'>" + ReportFileExporter.getIconSvg("report", 38) + "</div></a>", "text-align: center");
        report.endTableRow();
    }
}
