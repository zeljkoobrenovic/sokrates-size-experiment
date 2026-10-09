/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;

import java.util.*;
import java.util.stream.Collectors;

class ContributorTimeSlots {
    private final List<ContributorRepositories> contributors;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private Map<String, List<String>> contributorsPerWeekMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerWeekMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerDayMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerDayMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerMonthMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerMonthMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerYearMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerYearMap = new HashMap<>();

    ContributorTimeSlots(List<ContributorRepositories> contributors, LandscapeAnalysisResults landscapeAnalysisResults) {
        this.contributors = contributors;
        this.landscapeAnalysisResults = landscapeAnalysisResults;

        populateTimeSlotMaps();
    }

    int getContributorsCountPerYear(String year) {
        return this.contributorsPerYearMap.containsKey(year) ? contributorsPerYearMap.get(year).size() : 0;
    }

    private void populateTimeSlotMaps() {
        contributors.forEach(contributorRepositories -> {
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

    List<String> getSignificantContributorsPerYear(List<ContributorRepositories> contributorRepositories, String year, boolean rookiesOnly, int thresholdCommitDays) {
        if (rookiesOnly) {
            return getLastContributorsPerYear(year, true);
        }
        return contributorRepositories.stream()
                .filter(c -> c.getContributor().getCommitDates().stream().filter(d -> d.startsWith(year)).count() >= thresholdCommitDays)
                .map(c -> c.getContributor().getEmail())
                .collect(Collectors.toList());
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

    List<String> getContributorsPerWeek(String week, boolean rookiesOnly) {
        Map<String, List<String>> map = rookiesOnly ? rookiesPerWeekMap : contributorsPerWeekMap;
        return map.containsKey(week) ? map.get(week) : new ArrayList<>();
    }

    List<String> getContributorsPerDay(String day, boolean rookiesOnly) {
        Map<String, List<String>> map = rookiesOnly ? rookiesPerDayMap : contributorsPerDayMap;
        return map.containsKey(day) ? map.get(day) : new ArrayList<>();
    }

    List<String> getLastContributorsPerWeek(String week, boolean first) {
        Map<String, String> emails = new HashMap<>();

        contributors.stream()
                .sorted((a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days())
                .filter(c -> !DateUtils.getWeekMonday(c.getContributor().getFirstCommitDate()).equals(DateUtils.getWeekMonday(c.getContributor().getLatestCommitDate())))
                // .filter(c -> c.getContributor().getCommitDates().size() >= landscapeAnalysisResults.getConfiguration().getSignificantContributorMinCommitDaysPerYear())
                .forEach(contributorRepositories -> {
                    Contributor contributor = contributorRepositories.getContributor();
                    if (DateUtils.getWeekMonday(first ? contributor.getFirstCommitDate() : contributor.getLatestCommitDate()).equals(week)) {
                        String email = contributor.getEmail();
                        emails.put(email, email);
                        return;
                    }
                });

        return new ArrayList<>(emails.values());
    }

    List<String> getLastContributorsPerDay(String day, boolean first) {
        Map<String, String> emails = new HashMap<>();

        contributors.stream()
                .sorted((a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days())
                .filter(c -> !c.getContributor().getFirstCommitDate().equals(c.getContributor().getLatestCommitDate()))
                .forEach(contributorRepositories -> {
                    Contributor contributor = contributorRepositories.getContributor();
                    if ((first ? contributor.getFirstCommitDate() : contributor.getLatestCommitDate()).equals(day)) {
                        String email = contributor.getEmail();
                        emails.put(email, email);
                        return;
                    }
                });

        return new ArrayList<>(emails.values());
    }

    List<String> getContributorsPerMonth(String month, boolean rookiesOnly) {
        Map<String, List<String>> map = rookiesOnly ? rookiesPerMonthMap : contributorsPerMonthMap;
        return map.containsKey(month) ? map.get(month) : new ArrayList<>();
    }

    List<String> getLastContributorsPerYear(String year, boolean first) {
        Map<String, String> emails = new HashMap<>();

        contributors.stream()
                .sorted((a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days())
                .filter(c -> !DateUtils.getYear(c.getContributor().getLatestCommitDate()).equals(DateUtils.getYear(c.getContributor().getFirstCommitDate())))
                .forEach(contributorRepositories -> {
                    Contributor contributor = contributorRepositories.getContributor();
                    if (DateUtils.getYear(first ? contributor.getFirstCommitDate() : contributor.getLatestCommitDate()).equals(year)) {
                        String email = contributor.getEmail();
                        // only look at contributors with at least 10 commits days per year
                        if (contributor.getCommitDates().size() >= landscapeAnalysisResults.getConfiguration().getSignificantContributorMinCommitDaysPerYear()) {
                            emails.put(email, email);
                        }
                        return;
                    }
                });

        return new ArrayList<>(emails.values());
    }

    List<String> getLastContributorsPerMonth(String month, boolean first) {
        Map<String, String> emails = new HashMap<>();

        contributors.stream()
                .sorted((a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days())
                .filter(c -> !DateUtils.getMonth(c.getContributor().getLatestCommitDate()).equals(DateUtils.getMonth(c.getContributor().getFirstCommitDate())))
                .forEach(contributorRepositories -> {
                    Contributor contributor = contributorRepositories.getContributor();
                    if (DateUtils.getMonth(first ? contributor.getFirstCommitDate() : contributor.getLatestCommitDate()).equals(month)) {
                        String email = contributor.getEmail();
                        emails.put(email, email);
                        return;
                    }
                });

        return new ArrayList<>(emails.values());
    }
}
