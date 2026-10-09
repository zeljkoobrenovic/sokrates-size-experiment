/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

/**
 * The big summary blocks of the landscape report (overview and statistics tabs): repositories, lines of code,
 * active code, people and the commits/contributors per year chart.
 */
class LandscapeReportOverviewSection {
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final LandscapeReportInfoBlocks infoBlocks;
    private final SourceFileAgeDistribution overallFileLastModifiedDistribution;
    private Map<String, List<String>> contributorsPerWeekMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerWeekMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerDayMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerDayMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerMonthMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerMonthMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerYearMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerYearMap = new HashMap<>();

    LandscapeReportOverviewSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults,
                                   LandscapeReportInfoBlocks infoBlocks, SourceFileAgeDistribution overallFileLastModifiedDistribution) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.infoBlocks = infoBlocks;
        this.overallFileLastModifiedDistribution = overallFileLastModifiedDistribution;
        populateTimeSlotMaps();
    }

    private List<RepositoryAnalysisResults> getRepositories() {
        return landscapeAnalysisResults.getFilteredRepositoryAnalysisResults();
    }

    void addBigSummary(LandscapeAnalysisResults landscapeAnalysisResults) {
        landscapeReport.startDiv("margin-top: 0px;");
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        int size = getRepositories().size();
        infoBlocks.addFreshInfoBlock(FormattingUtils.getSmallTextForNumber(size), (size == 1 ? "repository" : "repositories"),
                "", "all repositories updated after " + configuration.getIgnoreRepositoriesLastUpdatedBefore() + " with at least " + FormattingUtils.formatCountPlural(configuration.getRepositoryThresholdContributors(), "contributor", "contributors"), REPOSITORIES_COLOR, "repository");
        addLocInfoBlock(landscapeAnalysisResults);
        int mainLoc1YearActive = landscapeAnalysisResults.getMainLoc1YearActive();
        int totalValue = LandscapeReportStatisticsSection.getSumOfValues(overallFileLastModifiedDistribution);
        addActiveCodeBlock(landscapeAnalysisResults, totalValue);

        List<ContributorRepositories> contributors = landscapeAnalysisResults.getContributors();
        long contributorsCount = contributors.size();
        if (contributorsCount > 0) {
            int recentContributorsCount = landscapeAnalysisResults.getRecentContributorsCount(contributors);
            int locPerRecentContributor = 0;
            if (recentContributorsCount > 0) {
                locPerRecentContributor = (int) Math.round((double) mainLoc1YearActive / recentContributorsCount);
            }
            infoBlocks.addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(recentContributorsCount), "recent contributors",
                    "(past 30 days)", getExtraPeopleInfo(contributors, contributorsCount) + "\n" + FormattingUtils.formatCount(locPerRecentContributor) + " active lines of code per recent contributor");
            int rookiesContributorsCount = landscapeAnalysisResults.getRookiesContributorsCount(contributors);
            infoBlocks.addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(rookiesContributorsCount),
                    rookiesContributorsCount == 1 ? "active rookie" : "active rookies",
                    "(started in past year)", "active contributors with the first commit in past year");
        }

        addContributorsPerYear(configuration.isShowContributorsTrendsOnFirstTab());

        landscapeReport.endDiv();
        landscapeReport.addLineBreak();
    }

    void addBigRepositoriesSummary(LandscapeAnalysisResults landscapeAnalysisResults) {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        landscapeAnalysisResults.getRecentContributorsCount(landscapeAnalysisResults.getContributors());
        List<RepositoryAnalysisResults> repositories = getRepositories();
        int recentSize = (int) repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days() > 0).count();
        int recentLoc = repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days() > 0).map(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()).reduce(0, (a, b) -> a + b);

        String style = "border-top: 2px solid lightgrey; border-right: 2px solid lightgrey; display: inline-block; margin-right: 8px";
        landscapeReport.startDiv(style);
        landscapeReport.addContentInDiv("active repositories", "text-align: center; margin-bottom: -7px; margin-top: 2px; margin-left: 4px; color: grey; font-size: 70%;");

        int size = repositories.size();
        int locAll = landscapeAnalysisResults.getMainLoc();
        int size90Days = (int) repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount90Days() > 0).count();
        int loc90Days = repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount90Days() > 0).map(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()).reduce(0, (a, b) -> a + b);
        int size180Days = (int) repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount180Days() > 0).count();
        int loc180Days = repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount180Days() > 0).map(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()).reduce(0, (a, b) -> a + b);
        int size365Days = (int) repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount365Days() > 0).count();
        int loc365Days = repositories.stream().filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount365Days() > 0).map(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()).reduce(0, (a, b) -> a + b);
        String contributorConstraint = " with at least " + FormattingUtils.formatCountPlural(configuration.getRepositoryThresholdContributors(), "contributor", "repository");
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(size), "all time",
                FormattingUtils.getSmallTextForNumber(locAll) + " LOC",
                "all repositories updated after " + configuration.getIgnoreRepositoriesLastUpdatedBefore() + " with at least " + contributorConstraint, REPOSITORIES_COLOR, "repository");
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(size365Days), "past 365d",
                FormattingUtils.getSmallTextForNumber(loc365Days) + " LOC (" + FormattingUtils.getFormattedPercentage(100.0 * loc365Days / Math.max(1, locAll)) + "%)",
                "all repositories updated in the past 365 days with at least " + contributorConstraint, REPOSITORIES_COLOR, "repository");
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(size180Days), "past 180d",
                FormattingUtils.getSmallTextForNumber(loc180Days) + " LOC (" + FormattingUtils.getFormattedPercentage(100.0 * loc180Days / Math.max(1, locAll)) + "%)",
                "all repositories updated in the past 180 days with at least " + contributorConstraint, REPOSITORIES_COLOR, "repository");
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(size90Days), "past 90d",
                FormattingUtils.getSmallTextForNumber(loc180Days) + " LOC (" + FormattingUtils.getFormattedPercentage(100.0 * loc90Days / Math.max(1, locAll)) + "%)",
                "all repositories updated in the past 90 days with at least " + contributorConstraint, REPOSITORIES_COLOR, "repository");
        infoBlocks.addFreshInfoBlock(FormattingUtils.getSmallTextForNumber(recentSize), "past 30d",
                FormattingUtils.getSmallTextForNumber(recentLoc) + " LOC (" + FormattingUtils.getFormattedPercentage(100.0 * recentLoc / Math.max(1, locAll)) + "%)",
                "all repositories updated in the past 30 days with at least " + contributorConstraint, REPOSITORIES_COLOR, "repository");
        landscapeReport.endDiv();
        landscapeReport.startDiv(style);
        landscapeReport.addContentInDiv("size (LOC)", "text-align: center; margin-bottom: -7px; margin-top: 2px; margin-left: 4px; color: grey; font-size: 70%;");
        addLocInfoBlock(landscapeAnalysisResults);
        landscapeReport.endDiv();
        landscapeReport.startDiv(style);
        landscapeReport.addContentInDiv("1y code activity", "text-align: center; margin-bottom: -7px; margin-top: 2px; margin-left: 4px; color: grey; font-size: 70%;");
        int totalValue = LandscapeReportStatisticsSection.getSumOfValues(overallFileLastModifiedDistribution);
        addActiveCodeBlock(landscapeAnalysisResults, totalValue);

        landscapeReport.endDiv();
    }

    private void addActiveCodeBlock(LandscapeAnalysisResults landscapeAnalysisResults, int locAll) {
        int mainLocActive = landscapeAnalysisResults.getMainLoc1YearActive();
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(mainLocActive), "main code touched", "1 year (" + FormattingUtils.getFormattedPercentage(100.0 * mainLocActive / Math.max(1, locAll)) + "%)",
                "files updated in past year", MAIN_LOC_FRESH_COLOR, "touch");
        int mainLocNew = landscapeAnalysisResults.getMainLocNew();
        infoBlocks.addInfoBlock(FormattingUtils.getSmallTextForNumber(mainLocNew),
                "new main code", "1 year (+" + FormattingUtils.getFormattedPercentage(100.0 * mainLocNew / Math.max(1, locAll)) + "%)",
                "files created in past year", MAIN_LOC_FRESH_COLOR, "new");
    }

    private void addLocInfoBlock(LandscapeAnalysisResults landscapeAnalysisResults) {
        int mainLoc = landscapeAnalysisResults.getMainLoc();
        int secondaryLoc = landscapeAnalysisResults.getSecondaryLoc();
        int mainFilesCount = landscapeAnalysisResults.getMainFilesCount();
        int secondaryFilesCount = landscapeAnalysisResults.getSecondaryFilesCount();
        infoBlocks.addFreshInfoBlock(FormattingUtils.getSmallTextForNumber(mainLoc), "lines of main code", FormattingUtils.getSmallTextForNumber(mainFilesCount) + " files", "main lines of code", MAIN_LOC_COLOR, "main");
        infoBlocks.addFreshInfoBlock(FormattingUtils.getSmallTextForNumber(secondaryLoc), "lines of other code", FormattingUtils.getSmallTextForNumber(secondaryFilesCount) + " files", "test, build & deployment, generated, all other code in scope", TEST_LOC_COLOR, "build");
    }

    private String getExtraPeopleInfo(List<ContributorRepositories> contributors, long contributorsCount) {
        String info = "";

        int recentContributorsCount6Months = landscapeAnalysisResults.getRecentContributorsCount6Months(contributors);
        int recentContributorsCount3Months = landscapeAnalysisResults.getRecentContributorsCount3Months(contributors);
        info += FormattingUtils.getPlainTextForNumber(landscapeAnalysisResults.getRecentContributorsCount(landscapeAnalysisResults.getContributors())) + " contributors (30 days)\n";
        info += FormattingUtils.getPlainTextForNumber(recentContributorsCount3Months) + " contributors (3 months)\n";
        info += FormattingUtils.getPlainTextForNumber(recentContributorsCount6Months) + " contributors (6 months)\n";

        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        int thresholdCommits = configuration.getContributorThresholdCommits();
        info += FormattingUtils.getPlainTextForNumber((int) contributorsCount) + " contributors (all time)\n";
        info += "\nOnly the contributors with " + (thresholdCommits > 1 ? "(" + thresholdCommits + "+&nbsp;commits)" : "") + " included";

        return info;
    }

    private void addContributorsPerYear(boolean showContributorsCount) {
        List<ContributionTimeSlot> contributorsPerYear = landscapeAnalysisResults.getContributorsPerYear();
        if (contributorsPerYear.size() > 0) {
            int limit = landscapeAnalysisResults.getConfiguration().getCommitsMaxYears();
            if (contributorsPerYear.size() > limit) {
                contributorsPerYear = contributorsPerYear.subList(0, limit);
            }

            int maxCommits = contributorsPerYear.stream().mapToInt(c -> c.getCommitsCount()).max().orElse(1);

            landscapeReport.startDiv("overflow-y: none;");
            landscapeReport.startTable();

            String style = "border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 100px";
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            addCommitsPerYearRow(contributorsPerYear, maxCommits, style, thisYear);

            if (showContributorsCount) {
                addContributorsPerYearRow(contributorsPerYear, style, thisYear);
            }

            addYearLabelsRow(contributorsPerYear, thisYear);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    private void addCommitsPerYearRow(List<ContributionTimeSlot> contributorsPerYear, int maxCommits, String style, int thisYear) {
        landscapeReport.startTableRow();
        landscapeReport.startTableCell("border: none; height: 100px");
        int commitsCount = landscapeAnalysisResults.getCommitsCount();
        if (commitsCount > 0) {
            landscapeReport.startDiv("max-height: 105px");
            infoBlocks.addSmallInfoBlock(FormattingUtils.getSmallTextForNumber(commitsCount), "commits", "white", "");
            landscapeReport.endDiv();
        }
        landscapeReport.endTableCell();
        contributorsPerYear.forEach(year -> {
            landscapeReport.startTableCell(style);
            int count = year.getCommitsCount();
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.addParagraph(count + "", "margin: 2px; color: " + color);
            int height = 1 + (int) (64.0 * count / maxCommits);
            String bgColor = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "lightgrey";
            landscapeReport.addHtmlContent("<div style='width: 100%; background-color: " + bgColor + "; height:" + height + "px'></div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addContributorsPerYearRow(List<ContributionTimeSlot> contributorsPerYear, String style, int thisYear) {
        int maxContributors[] = {1};
        contributorsPerYear.forEach(year -> {
            int count = getContributorsCountPerYear(year.getTimeSlot());
            maxContributors[0] = Math.max(maxContributors[0], count);
        });
        landscapeReport.startTableRow();
        landscapeReport.startTableCell("border: none; height: 100px");
        int contributorsCount = landscapeAnalysisResults.getContributors().size();
        if (contributorsCount > 0) {
            landscapeReport.startDiv("max-height: 105px");
            infoBlocks.addSmallInfoBlock(FormattingUtils.getSmallTextForNumber(contributorsCount), "contributors", "white", "");
            landscapeReport.endDiv();
        }
        landscapeReport.endTableCell();
        contributorsPerYear.forEach(year -> {
            landscapeReport.startTableCell(style);
            int count = getContributorsCountPerYear(year.getTimeSlot());
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.addParagraph(count + "", "margin: 2px; color: " + color + ";");
            int height = 1 + (int) (64.0 * count / maxContributors[0]);
            landscapeReport.addHtmlContent("<div style='width: 100%; background-color: skyblue; height:" + height + "px'></div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addYearLabelsRow(List<ContributionTimeSlot> contributorsPerYear, int thisYear) {
        landscapeReport.startTableRow();
        landscapeReport.addTableCell("", "border: none; ");
        var ref = new Object() {
            String latestCommitDate = landscapeAnalysisResults.getLatestCommitDate();
        };
        if (ref.latestCommitDate.length() > 5) {
            ref.latestCommitDate = ref.latestCommitDate.substring(5);
        }
        contributorsPerYear.forEach(year -> {
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.startTableCell("vertical-align: top; border: none; text-align: center; font-size: 90%; color: " + color);
            landscapeReport.addHtmlContent(year.getTimeSlot());
            if (landscapeAnalysisResults.getLatestCommitDate().startsWith(year.getTimeSlot() + "-")) {
                landscapeReport.addContentInDiv(ref.latestCommitDate, "text-align: center; color: grey; font-size: 9px");
            }
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private int getContributorsCountPerYear(String year) {
        return this.contributorsPerYearMap.containsKey(year) ? contributorsPerYearMap.get(year).size() : 0;
    }

    private void populateTimeSlotMaps() {
        landscapeAnalysisResults.getContributors().forEach(contributorRepositories -> {
            List<String> commitDates = contributorRepositories.getContributor().getCommitDates();
            commitDates.forEach(day -> {
                String week = DateUtils.getWeekMonday(day);
                String month = DateUtils.getMonth(day);
                String year = DateUtils.getYear(day);

                updateTimeSlotMap(contributorRepositories, contributorsPerDayMap, rookiesPerDayMap, day, day);
                updateTimeSlotMap(contributorRepositories, contributorsPerWeekMap, rookiesPerWeekMap, week, week);
                updateTimeSlotMap(contributorRepositories, contributorsPerMonthMap, rookiesPerMonthMap, month, month + "-01");
                updateTimeSlotMap(contributorRepositories, contributorsPerYearMap, rookiesPerYearMap, year, year + "-01-01");
            });
        });

    }

    private void updateTimeSlotMap(ContributorRepositories contributorRepositories,
                                   Map<String, List<String>> map, Map<String, List<String>> rookiesMap, String key, String rookieDate) {
        boolean rookie = contributorRepositories.getContributor().isRookieAtDate(rookieDate);

        String email = contributorRepositories.getContributor().getEmail();
        if (map.containsKey(key)) {
            if (!map.get(key).contains(email)) {
                map.get(key).add(email);
            }
        } else {
            map.put(key, new ArrayList<>(Arrays.asList(email)));
        }
        if (rookie) {
            if (rookiesMap.containsKey(key)) {
                if (!rookiesMap.get(key).contains(email)) {
                    rookiesMap.get(key).add(email);
                }
            } else {
                rookiesMap.put(key, new ArrayList<>(Arrays.asList(email)));
            }
        }
    }
}
