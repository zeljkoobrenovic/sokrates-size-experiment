package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.data.LandscapeDataExport;
import nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.repositories.RepositoryTableCells.getRepositoryDisplayHtml;

class LandscapeRepositoriesSummaryGraphs {
    private LandscapeAnalysisResults landscapeAnalysisResults;

    LandscapeRepositoriesSummaryGraphs(LandscapeAnalysisResults landscapeAnalysisResults) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
    }

    void addSummaryGraphCommits(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startDiv("white-space: nowrap; overflow-x: scroll; width: 100%");
        int max = Math.max(repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days())
                .max().orElse(1), 1);
        int sum = repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days())
                .sum();
        int maxHeight = 64;
        int cummulative[] = {0};
        int index[] = {0};
        boolean breakPointReached[] = {false};
        int repositoriesCount = repositoryAnalysisResults.size();
        repositoryAnalysisResults.stream().limit(landscapeAnalysisResults.getConfiguration().getRepositoriesListLimit()).forEach(repositoryAnalysis -> {
            int commits30d = repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days();
            cummulative[0] += commits30d;
            index[0] += 1;
            int height = (int) (1 + maxHeight * (double) commits30d / max);
            String name = getRepositoryDisplayHtml(repositoryAnalysis.getAnalysisResults().getMetadata().getName());
            double percentage = RichTextRenderingUtils.getPercentage(sum, cummulative[0]);
            double percentageRepositories = RichTextRenderingUtils.getPercentage(repositoriesCount, index[0]);
            String color = commits30d > 0 ? (!breakPointReached[0] && percentage >= 50 ? "blue" : "blue; opacity: 0.5") : "lightgrey; opacity: 0.5;";
            if (percentage >= 50) {
                breakPointReached[0] = true;
            }
            report.addContentInDivWithTooltip("",
                    name + ": " + commits30d + " lines of code (main)\n" +
                            "cumulative: top " + index[0] + " repositories (" + percentageRepositories + "%) = "
                            + cummulative[0] + " commits in past 30 days (" + percentage + "%)",
                    "margin: 0; padding: 0; margin-right: 1px; background-color: " + color + "; display: inline-block; width: 8px; height: " + height + "px");
        });
        report.endDiv();
        report.startDiv("font-size: 80%; margin-bottom: 6px;");
        report.addNewTabLink("bubble chart", "visuals/bubble_chart_repositories_commits.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("tree map", "visuals/tree_map_repositories_commits.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("animated history (all time)", "visuals/racing_charts_commits_repositories.html?tickDuration=600");
        report.addHtmlContent(" | ");
        report.addNewTabLink("animated history (12 months window)", "visuals/racing_charts_commits_window_repositories.html?tickDuration=600");
        report.addHtmlContent(" | ");
        report.addNewTabLink("data", "data/" + LandscapeDataExport.REPOSITORIES_DATA_FILE_NAME);
        report.endDiv();
    }

    void addSummaryGraphHistory(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startDiv("white-space: nowrap; overflow-x: scroll; width: 100%");
        int max = Math.max(repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays())
                .max().orElse(1), 1);
        int maxHeight = 64;
        repositoryAnalysisResults.stream().limit(landscapeAnalysisResults.getConfiguration().getRepositoriesListLimit()).forEach(repositoryAnalysis -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysis.getAnalysisResults().getFilesHistoryAnalysisResults();
            int ageInDays = filesHistoryAnalysisResults.getAgeInDays();
            int height = (int) (1 + maxHeight * (double) ageInDays / max);
            String color = ageInDays > 0 ? "darkgreen" : "lightgrey";
            int repositoryAgeYears = (int) Math.round(filesHistoryAnalysisResults.getAgeInDays() / 365.0);
            String age = repositoryAgeYears == 0 ? "<1y" : repositoryAgeYears + "y";
            report.addContentInDivWithTooltip("",
                    repositoryAnalysis.getAnalysisResults().getMetadata().getName() + ": " + ageInDays + " days (" + age + ")",
                    "margin: 0; padding: 0; opacity: 0.5; margin-right: 1px; background-color: " + color + "; display: inline-block; width: 8px; height: " + height + "px");
        });
        report.endDiv();
        report.startDiv("font-size: 80%; margin-bottom: 6px;");
        report.addNewTabLink("bubble chart", "visuals/bubble_chart_repositories_age.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("tree map", "visuals/tree_map_repositories_age.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("data", "data/" + LandscapeDataExport.REPOSITORIES_DATA_FILE_NAME);
        report.endDiv();
    }

    void addSummaryGraphNewReposPerYear(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startDiv("white-space: nowrap; overflow-x: scroll; width: 100%");
        report.addContentInDiv("repository creation per year:", "font-size: 80%; color: grey; margin-bottom: 5px;");

        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        List<List<RepositoryAnalysisResults>> perYear = new ArrayList<>();

        int goingBack = 20;
        for (int i = 0; i < goingBack; i++) {
            int year = yearNow - i;
            ArrayList<RepositoryAnalysisResults> yearRepos = new ArrayList<>();
            perYear.add(yearRepos);

            repositoryAnalysisResults.forEach(analysis -> {
                String firstDate = analysis.getAnalysisResults().getFilesHistoryAnalysisResults().getFirstDate();
                if (firstDate != null && firstDate.startsWith(year + "")) {
                    yearRepos.add(analysis);
                }
            });
        }

        int max = Math.max(perYear.stream().mapToInt(p -> p.size()).max().orElse(1), 1);

        int maxHeight = 64;

        for (int i = 0; i < goingBack; i++) {
            int year = yearNow - i;
            List<RepositoryAnalysisResults> yearRepos = perYear.get(i);

            int height = (int) (1 + maxHeight * (double) yearRepos.size() / max);
            String color = "skyblue";
            String info = year + ": " + yearRepos.size() + " repos";

            info += "\n\n";

            info += yearRepos.stream().map(r -> r.getAnalysisResults().getMetadata().getName()).collect(Collectors.joining("\n"));

            report.startDiv("display: inline-block");

            report.addContentInDivWithTooltip(yearRepos.size() > 0 ? "+" + yearRepos.size() + "" : "", "",
                    "font-size: 80%; text-align: center");
            report.addContentInDivWithTooltip("", info,
                    "margin: 0; padding: 0; opacity: 0.5; margin-right: 1px; background-color: " + color + "; display: inline-block; width: 44px; height: " + height + "px");
            report.addContentInDivWithTooltip(year + "", "",
                    "font-size: 70%; text-align: center; color: grey");

            report.endDiv();
        }
        report.endDiv();
    }

    void addSummaryGraphContributors(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startDiv("white-space: nowrap; overflow-x: scroll; width: 100%");
        int max = Math.max(repositoryAnalysisResults.stream()
                .mapToInt(p -> (int) p.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count())
                .max().orElse(1), 1);
        int maxHeight = 64;
        repositoryAnalysisResults.stream().limit(landscapeAnalysisResults.getConfiguration().getRepositoriesListLimit()).forEach(repositoryAnalysis -> {
            int contributors30d = (int) repositoryAnalysis.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(LandscapeReportGenerator.RECENT_THRESHOLD_DAYS)).count();
            int height = (int) (1 + maxHeight * (double) contributors30d / max);
            String color = contributors30d > 0 ? "darkred" : "lightgrey";
            report.addContentInDivWithTooltip("",
                    repositoryAnalysis.getAnalysisResults().getMetadata().getName() + ": " + contributors30d + " contributors (30 days)",
                    "margin: 0; padding: 0; opacity: 0.5; margin-right: 1px; background-color: " + color + "; display: inline-block; width: 8px; height: " + height + "px");
        });
        report.endDiv();
        report.startDiv("font-size: 80%; margin-bottom: 6px;");
        report.addNewTabLink("bubble chart", "visuals/bubble_chart_repositories_contributors.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("tree map", "visuals/tree_map_repositories_contributors.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("data", "data/" + LandscapeDataExport.REPOSITORIES_DATA_FILE_NAME);
        report.endDiv();
    }

    void addSummaryGraphMainLoc(RichTextReport report, List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        report.startDiv("white-space: nowrap; overflow-x: scroll; width: 100%");
        int max = Math.max(repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode())
                .max().orElse(1), 1);
        int sum = repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode())
                .sum();
        int cumulative[] = {0};
        int index[] = {0};
        int maxHeight = 64;
        boolean breakPointReached[] = {false};
        int repositoriesCount = repositoryAnalysisResults.size();
        repositoryAnalysisResults.stream().limit(landscapeAnalysisResults.getConfiguration().getRepositoriesListLimit()).forEach(repositoryAnalysis -> {
            int mainLoc = repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode();
            cumulative[0] += mainLoc;
            index[0] += 1;
            int height = (int) (1 + maxHeight * (double) mainLoc / max);
            String name = getRepositoryDisplayHtml(repositoryAnalysis.getAnalysisResults().getMetadata().getName());
            double percentage = RichTextRenderingUtils.getPercentage(sum, cumulative[0]);
            double percentageRepositories = RichTextRenderingUtils.getPercentage(repositoriesCount, index[0]);
            String color = mainLoc > 0 ? (!breakPointReached[0] && percentage >= 50 ? "blue" : "skyblue") : "lightgrey";
            if (percentage >= 50) {
                breakPointReached[0] = true;
            }
            report.addContentInDivWithTooltip("",
                    name + ": " + mainLoc + " lines of code (main)\n" +
                            "cumulative: top " + index[0] + " repositories (" + percentageRepositories + "%) = "
                            + cumulative[0] + " LOC (" + percentage + "%)",
                    "margin: 0; padding: 0; opacity: 0.9; margin-right: 1px; background-color: " + color + "; display: inline-block; width: 8px; height: " + height + "px");
        });
        report.endDiv();
        report.startDiv("font-size: 80%; margin-bottom: 6px;");
        report.addNewTabLink("bubble chart", "visuals/bubble_chart_repositories_loc.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("tree map", "visuals/tree_map_repositories_loc.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("data", "data/" + LandscapeDataExport.REPOSITORIES_DATA_FILE_NAME);
        report.endDiv();
    }
}
