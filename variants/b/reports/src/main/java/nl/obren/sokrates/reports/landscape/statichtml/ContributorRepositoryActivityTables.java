package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositoryInfo;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ContributorRepositoryActivityTables {
    private LandscapeAnalysisResults landscapeAnalysisResults;

    ContributorRepositoryActivityTables(LandscapeAnalysisResults landscapeAnalysisResults) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
    }

    void addPerWeek(ContributorRepositories contributorRepositories, RichTextReport report) {

        report.startDiv("width: 100%; overflow-x: scroll;");
        report.startTable();

        final List<String> pastWeeks = DateUtils.getPastWeeks(104, landscapeAnalysisResults.getLatestCommitDate());
        List<ContributorRepositoryInfo> repositories = new ArrayList<>(contributorRepositories.getRepositories());
        addPerWeekHeaderRow(report, pastWeeks, repositories);

        List<ContributorRepositoryInfo> activeRepositories = getActiveRepositories(pastWeeks, repositories);

        Collections.sort(activeRepositories, (a, b) -> b.getLatestCommitDate().compareTo(a.getLatestCommitDate()));

        activeRepositories.forEach(repository -> {
            addPerWeekRepositoryRow(report, pastWeeks, repository);
        });
        report.endTable();
        report.endDiv();
    }

    private void addPerWeekHeaderRow(RichTextReport report, List<String> pastWeeks, List<ContributorRepositoryInfo> repositories) {
        report.startTableRow();
        report.addTableCell("", "border: none");
        report.addTableCell("", "min-width: 300px; border: none");
        report.addTableCell("Commits<br>(3m)", "max-width: 100px; text-align: center; border: none");
        report.addTableCell("Commit<br>Days", "max-width: 100px; text-align: center; border: none");
        pastWeeks.forEach(pastWeek -> {
            int repositoryCount[] = {0};
            repositories.forEach(repository -> {
                boolean found[] = {false};
                repository.getCommitDates().forEach(date -> {
                    String weekMonday = DateUtils.getWeekMonday(date);
                    if (weekMonday.equals(pastWeek)) {
                        found[0] = true;
                        return;
                    }
                });
                if (found[0]) {
                    repositoryCount[0] += 1;
                    return;
                }
            });
            String tooltip = "Week of " + pastWeek + ": " + repositoryCount[0] + (repositoryCount[0] == 1 ? " repository" : " repositories");
            report.startTableCell("font-size: 70%; border: none; color: lightgrey; text-align: center");
            report.addContentInDivWithTooltip(repositoryCount[0] + "", tooltip, "text-align: center");
            report.endTableCell();
        });
        report.endTableRow();
    }

    private List<ContributorRepositoryInfo> getActiveRepositories(List<String> pastWeeks, List<ContributorRepositoryInfo> repositories) {
        List<ContributorRepositoryInfo> activeRepositories = new ArrayList<>();

        repositories.forEach(repository -> {
            int daysCount[] = {0};
            pastWeeks.forEach(pastWeek -> {
                repository.getCommitDates().forEach(date -> {
                    String weekMonday = DateUtils.getWeekMonday(date);
                    if (weekMonday.equals(pastWeek)) {
                        daysCount[0] += 1;
                    }
                });

            });
            if (daysCount[0] > 0) {
                activeRepositories.add(repository);
            }
        });
        return activeRepositories;
    }

    private void addPerWeekRepositoryRow(RichTextReport report, List<String> pastWeeks, ContributorRepositoryInfo repository) {
        String textOpacity = repository.getCommits90Days() > 0 ? "font-weight: bold;" : "opacity: 0.4";
        report.startTableRow();
        addLangTableCell(report, repository.getRepositoryAnalysisResults().getAnalysisResults().getMainAspectAnalysisResults());
        addRepositoryNameCell(report, repository, "border: none;" + textOpacity);
        report.addTableCell(repository.getCommits90Days() > 0 ? repository.getCommits90Days() + "" : "-", "text-align: center; border: none; " + textOpacity);
        report.addTableCell(repository.getCommitDates().size() + "", "text-align: center; border: none; " + textOpacity);
        addWeekCells(report, pastWeeks, repository);
        report.endTableRow();
    }

    private void addWeekCells(RichTextReport report, List<String> pastWeeks, ContributorRepositoryInfo repository) {
        int index[] = {0};
        pastWeeks.forEach(pastWeek -> {
            int daysCount[] = {0};
            index[0] += 1;
            repository.getCommitDates().forEach(date -> {
                String weekMonday = DateUtils.getWeekMonday(date);
                if (weekMonday.equals(pastWeek)) {
                    daysCount[0] += 1;
                }
            });
            report.startTableCell("text-align: center; padding: 0; border: none; vertical-align: middle");
            if (daysCount[0] > 0) {
                int size = 10 + daysCount[0] * 4;
                String tooltip = "Week of " + pastWeek + ": " + daysCount[0] + (daysCount[0] == 1 ? " commit day" : " commit days");
                String opacity = "" + Math.max(0.9 - (index[0] - 1) * 0.05, 0.2);
                report.addContentInDivWithTooltip("", tooltip,
                        "display: inline-block; padding: 0; margin: 0; " +
                                "background-color: #483D8B; border-radius: 50%; width: " + size + "px; height: " + size + "px; opacity: " + opacity + ";");
            } else {
                report.addContentInDiv("-", "color: lightgrey; font-size: 80%");
            }
            report.endTableCell();
        });
    }

    private static void addRepositoryNameCell(RichTextReport report, ContributorRepositoryInfo repository, String cellStyle) {
        report.startTableCell(cellStyle);
        String fullName = repository.getRepositoryAnalysisResults().getAnalysisResults().getMetadata().getName();
        String nameElements[] = fullName.split("/");
        String parent = nameElements.length == 2 ? nameElements[0] : null;
        String name = nameElements.length == 2 ? nameElements[1] : fullName;
        String nameHtml = "";
        if (parent != null) {
            nameHtml = "<div style='font-size: 90%; color: lightgrey; padding-top: 2px'>" + parent + "</div>";
        }
        nameHtml += "<div style='font-size: 110%;'>" + name + "</div>";
        report.addNewTabLink(nameHtml,
                "../../" + repository.getRepositoryAnalysisResults().getSokratesRepositoryLink().getHtmlReportsRoot() + "/index.html");
        report.endTableCell();
    }

    void addPerMonth(ContributorRepositories contributorRepositories, RichTextReport report) {
        report.startDiv("width: 100%; overflow-x: scroll;");
        report.startTable();

        final List<String> pastMonths = DateUtils.getPastMonths(24, landscapeAnalysisResults.getLatestCommitDate());
        addPerMonthHeaderRow(report, pastMonths, contributorRepositories);
        List<ContributorRepositoryInfo> repositories = new ArrayList<>(contributorRepositories.getRepositories());
        Collections.sort(repositories, (a, b) -> b.getLatestCommitDate().compareTo(a.getLatestCommitDate()));

        repositories.forEach(repository -> {
            addPerMonthRepositoryRow(report, pastMonths, repository);
        });
        report.endTable();
        report.endDiv();
    }

    private void addPerMonthHeaderRow(RichTextReport report, List<String> pastMonths, ContributorRepositories contributorRepositories) {
        report.startTableRow();
        report.addTableCell("", "border: none");
        report.addTableCell("", "min-width: 200px; border: none");
        report.addTableCell("Commits<br>(3m)", "max-width: 100px; text-align: center; border: none");
        report.addTableCell("Commit<br>Days", "max-width: 100px; text-align: center; border: none");
        pastMonths.forEach(pastMonth -> {
            int repositoryCount[] = {0};
            contributorRepositories.getRepositories().forEach(repository -> {
                boolean found[] = {false};
                repository.getCommitDates().forEach(date -> {
                    String weekMonday = DateUtils.getMonth(date);
                    if (weekMonday.equals(pastMonth)) {
                        found[0] = true;
                        return;
                    }
                });
                if (found[0]) {
                    repositoryCount[0] += 1;
                    return;
                }
            });
            String tooltip = "Month " + pastMonth + ": " + repositoryCount[0] + (repositoryCount[0] == 1 ? " repository" : " repositories");
            report.startTableCell("font-size: 70%; border: none; color: lightgrey; text-align: center");
            report.addContentInDivWithTooltip(repositoryCount[0] + "", tooltip, "text-align: center");
            report.endTableCell();
        });
        report.endTableRow();
    }

    private void addPerMonthRepositoryRow(RichTextReport report, List<String> pastMonths, ContributorRepositoryInfo repository) {
        report.startTableRow();
        addLangTableCell(report, repository.getRepositoryAnalysisResults().getAnalysisResults().getMainAspectAnalysisResults());
        String textOpacity = repository.getCommits90Days() > 0 ? "font-weight: bold;" : "opacity: 0.4";
        addRepositoryNameCell(report, repository, "border: none; " + textOpacity);
        report.addTableCell(repository.getCommits90Days() > 0 ? repository.getCommits90Days() + "" : "-", "text-align: center; border: none; " + textOpacity);
        report.addTableCell(repository.getCommitDates().size() + "", "text-align: center; border: none; " + textOpacity);
        addMonthCells(report, pastMonths, repository);
        report.endTableRow();
    }

    private void addMonthCells(RichTextReport report, List<String> pastMonths, ContributorRepositoryInfo repository) {
        int index[] = {0};
        pastMonths.forEach(pastMonth -> {
            int count[] = {0};
            repository.getCommitDates().forEach(date -> {
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
    }

    static void addLangTableCell(RichTextReport report, AspectAnalysisResults main) {
        List<NumericMetric> linesOfCodePerExtension = main.getLinesOfCodePerExtension();
        StringBuilder locSummary = new StringBuilder();
        if (linesOfCodePerExtension.size() > 0) {
            locSummary.append(linesOfCodePerExtension.get(0).getName().replace("*.", "").trim().toUpperCase());
        } else {
            locSummary.append("-");
        }
        String lang = locSummary.toString().replace("> = ", ">");
        report.startTableCell("text-align: left; max-width: 32px; border: none");
        report.startDiv("white-space: nowrap; overflow: hidden;");
        report.addHtmlContent(DataImageUtils.getLangDataImageDiv28(lang));
        report.endDiv();
        report.endTableCell();
    }


    void addPerYear(ContributorRepositories contributorRepositories, RichTextReport report) {
        report.startDiv("width: 100%; overflow-x: scroll;");
        report.startTable();

        final List<String> pastYears = DateUtils.getPastYears(landscapeAnalysisResults.getConfiguration().getCommitsMaxYears(), landscapeAnalysisResults.getLatestCommitDate());
        report.startTableRow();
        report.addTableCell("", "border: none");
        report.addTableCell("", "min-width: 200px; border: none; max-width: 500px; white-space: nowrap; overflow: hidden");
        report.addTableCell("Commits<br>(3m)", "max-width: 100px; text-align: center; border: none");
        report.addTableCell("Commit<br>Days", "max-width: 100px; text-align: center; border: none");
        int maxRepositoryDays[] = getMaxRepositoryDays(pastYears, contributorRepositories);
        addPerYearHeaderCells(report, pastYears, contributorRepositories, maxRepositoryDays);
        report.endTableRow();
        List<ContributorRepositoryInfo> repositories = new ArrayList<>(contributorRepositories.getRepositories());
        Collections.sort(repositories, (a, b) -> b.getLatestCommitDate().compareTo(a.getLatestCommitDate()));

        repositories.forEach(repository -> {
            addPerYearRepositoryRow(report, pastYears, repository);
        });
        report.endTable();

        report.endDiv();
    }

    private int[] getMaxRepositoryDays(List<String> pastYears, ContributorRepositories contributorRepositories) {
        int maxRepositoryDays[] = {1};
        pastYears.forEach(pastYear -> {
            int repositoryCount[] = {0};
            int repositoryDays[] = {0};
            contributorRepositories.getRepositories().forEach(repository -> {
                boolean found[] = {false};
                repository.getCommitDates().forEach(date -> {
                    String year = DateUtils.getYear(date);
                    if (year.equals(pastYear)) {
                        found[0] = true;
                        return;
                    }
                });
                if (found[0]) {
                    repositoryCount[0] += 1;
                    repositoryDays[0] += repository.getCommitDates().stream().filter(date -> date.startsWith(pastYear + "-")).count();
                    return;
                }
            });

            maxRepositoryDays[0] = Math.max(repositoryDays[0], maxRepositoryDays[0]);
        });
        return maxRepositoryDays;
    }

    private void addPerYearHeaderCells(RichTextReport report, List<String> pastYears, ContributorRepositories contributorRepositories, int[] maxRepositoryDays) {
        pastYears.forEach(pastYear -> {
            int repositoryCount[] = {0};
            int repositoryDays[] = {0};
            contributorRepositories.getRepositories().forEach(repository -> {
                boolean found[] = {false};
                repository.getCommitDates().forEach(date -> {
                    String year = DateUtils.getYear(date);
                    if (year.equals(pastYear)) {
                        found[0] = true;
                    }
                });
                if (found[0]) {
                    repositoryCount[0] += 1;
                    repositoryDays[0] += repository.getCommitDates().stream().filter(date -> date.startsWith(pastYear + "-")).count();
                }
            });
            String tooltip = "Month " + pastYear + ": " + repositoryCount[0] + (repositoryCount[0] == 1 ? " repository" : " repositories"
                    + ", " + repositoryDays[0] + " commit " + (repositoryDays[0] == 1 ? "day" : "days"));
            report.startTableCell("vertical-align: bottom; font-size: 70%; border: none; color: lightgrey; text-align: center");
            String content = repositoryCount[0] + "&nbsp;r<br>" + repositoryDays[0] + "&nbsp;cd";
            content += "<div style='vertical-align: bottom; text-align: center; margin: auto; background-color: skyblue; width: 32px; height: "
                    + ((int) (1 + 32 * ((double) repositoryDays[0] / maxRepositoryDays[0]))) +
                    "px;'> </div>" + pastYear;
            report.addContentInDivWithTooltip(content, tooltip, "text-align: center");
            report.endTableCell();

        });
    }

    private void addPerYearRepositoryRow(RichTextReport report, List<String> pastYears, ContributorRepositoryInfo repository) {
        report.startTableRow();
        String textOpacity = repository.getCommits90Days() > 0 ? "font-weight: bold;" : "opacity: 0.4";
        addLangTableCell(report, repository.getRepositoryAnalysisResults().getAnalysisResults().getMainAspectAnalysisResults());

        addRepositoryNameCell(report, repository, "padding: 0; border: none; " + textOpacity);
        report.addTableCell(repository.getCommits90Days() > 0 ? repository.getCommits90Days() + "" : "-", "text-align: center; border: none; " + textOpacity);
        report.addTableCell(repository.getCommitDates().size() + "", "text-align: center; border: none; " + textOpacity);
        addYearCells(report, pastYears, repository);
        report.endTableRow();
    }

    private void addYearCells(RichTextReport report, List<String> pastYears, ContributorRepositoryInfo repository) {
        int index[] = {0};
        pastYears.forEach(pastYear -> {
            int count[] = {0};
            repository.getCommitDates().forEach(date -> {
                String year = DateUtils.getYear(date);
                if (year.equals(pastYear)) {
                    count[0] += 1;
                }
            });
            index[0] += 1;
            report.startTableCell("text-align: center; padding: 0; border: none; vertical-align: middle;");
            if (count[0] > 0) {
                int size = (int) (10 + Math.min(1, (count[0] / 366.0)) * 40);
                String tooltip = "Year " + pastYear + ": " + count[0] + (count[0] == 1 ? " commit day" : " commit days");
                String opacity = "" + Math.max(0.9 - (index[0] - 1) * 0.2, 0.2);
                report.addContentInDivWithTooltip("", tooltip,
                        "padding: 0; margin: 0; display: inline-block; background-color: #483D8B; opacity: " + opacity + "; border-radius: 50%; width: " + size + "px; height: " + size + "px;");
            } else {
                report.addContentInDiv("-", "color: lightgrey; font-size: 80%");
            }
            report.endTableCell();
        });
    }

}
