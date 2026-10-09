/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.landscape.analysis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.HistoryPerExtension;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.githistory.CommitsPerExtension;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LandscapeAnalysisResults {
    public static final int RECENT_THRESHOLD_DAYS = 30;
    private static final Log LOG = LogFactory.getLog(LandscapeAnalysisResults.class);

    @JsonIgnore
    private TeamsConfig teamsConfig;
    @JsonIgnore
    private PeopleConfig peopleConfig;

    @JsonIgnore
    private Set<String> level1SubLandscapes = new HashSet<>();
    @JsonIgnore
    private List<ComponentDependency> subLandscapeDependenciesViaRepositoriesWithSameContributors = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> subLandscapeIndirectDependenciesViaRepositoriesWithSameContributors = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> subLandscapeDependenciesViaRepositoriesWithSameName = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> subLandscapeIndirectDependenciesViaRepositoriesWithSameName = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> peopleDependencies30Days = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> peopleRepositoryDependencies30Days = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> peopleDependencies90Days = new ArrayList<>();

    @JsonIgnore
    private List<ComponentDependency> peopleDependencies180Days = new ArrayList<>();

    @JsonIgnore
    private List<ContributorConnections> connectionsViaRepositories30Days = new ArrayList<>();

    @JsonIgnore
    private List<Double> connectionsViaRepositories30DaysCountHistory = new ArrayList<>();

    @JsonIgnore
    private List<Double> peopleDependenciesCount30DaysHistory = new ArrayList<>();

    @JsonIgnore
    private List<Double> activeContributors30DaysHistory = new ArrayList<>();

    @JsonIgnore
    private List<ContributorConnections> connectionsViaRepositories90Days = new ArrayList<>();

    @JsonIgnore
    private List<ContributorConnections> connectionsViaRepositories180Days = new ArrayList<>();

    @JsonIgnore
    private LandscapeConfiguration configuration = new LandscapeConfiguration();

    @JsonIgnore
    private final Map<String, List<Contributor>> contributorsPerMonthMap = null;

    @JsonIgnore
    private final Map<String, List<Contributor>> contributorsPerYearMap = null;

    private double c2cConnectionsCount30Days;
    private double c2pConnectionsCount30Days;

    private double cIndex30Days;
    private double cIndex90Days;
    private double cIndex180Days;

    private double cMean30Days;
    private double cMean90Days;
    private double cMean180Days;

    private double cMedian30Days;
    private double cMedian90Days;
    private double cMedian180Days;

    private double pIndex30Days;
    private double pIndex90Days;
    private double pIndex180Days;

    private double pMean30Days;
    private double pMean90Days;
    private double pMean180Days;

    private double pMedian30Days;
    private double pMedian90Days;
    private double pMedian180Days;

    private String firstCommitDate = "";

    private String latestCommitDate = "";

    private List<Double> cIndex30DaysHistory = new ArrayList<>();
    private List<Double> pIndex30DaysHistory = new ArrayList<>();

    private List<Double> cMean30DaysHistory = new ArrayList<>();
    private List<Double> pMean30DaysHistory = new ArrayList<>();

    private List<Double> cMedian30DaysHistory = new ArrayList<>();
    private List<Double> pMedian30DaysHistory = new ArrayList<>();
    @JsonIgnore
    private List<RepositoryAnalysisResults> repositoryAnalysisResults = new ArrayList<>();
    @JsonIgnore
    private final LandscapeExtensionAggregator extensions = new LandscapeExtensionAggregator(this);
    @JsonIgnore
    private final LandscapeContributorsAggregator contributors = new LandscapeContributorsAggregator(this);
    @JsonIgnore
    private final LandscapeContributionTimeSlots timeSlots = new LandscapeContributionTimeSlots(this, contributors);

    public LandscapeAnalysisResults(TeamsConfig teamsConfig, PeopleConfig peopleConfig) {
        this.teamsConfig = teamsConfig;
        this.peopleConfig = peopleConfig;
    }

    public static SourceFileAgeDistribution getOverallFileLastModifiedDistribution(List<RepositoryAnalysisResults> repositoriesAnalysisResults) { return LandscapeFileDistributions.getOverallFileLastModifiedDistribution(repositoriesAnalysisResults); }

    public static SourceFileAgeDistribution getOverallFileFirstModifiedDistribution(List<RepositoryAnalysisResults> repositoriesAnalysisResults) { return LandscapeFileDistributions.getOverallFileFirstModifiedDistribution(repositoriesAnalysisResults); }

    public static int getLoc1YearActive(List<RepositoryAnalysisResults> repositoriesAnalysisResults) { return LandscapeFileDistributions.getLoc1YearActive(repositoriesAnalysisResults); }

    public static int getLoc30DaysActive(List<RepositoryAnalysisResults> repositoriesAnalysisResults) { return LandscapeFileDistributions.getLoc30DaysActive(repositoriesAnalysisResults); }

    public static int getLocNew(List<RepositoryAnalysisResults> repositoriesAnalysisResults) { return LandscapeFileDistributions.getLocNew(repositoriesAnalysisResults); }

    @JsonIgnore
    public Set<String> getLevel1SubLandscapes() { return level1SubLandscapes; }

    @JsonIgnore
    public void setLevel1SubLandscapes(Set<String> level1SubLandscapes) { this.level1SubLandscapes = level1SubLandscapes; }

    @JsonIgnore
    public List<ComponentDependency> getSubLandscapeDependenciesViaRepositoriesWithSameContributors() { return subLandscapeDependenciesViaRepositoriesWithSameContributors; }

    @JsonIgnore
    public void setSubLandscapeDependenciesViaRepositoriesWithSameContributors(List<ComponentDependency> subLandscapeDependenciesViaRepositoriesWithSameContributors) { this.subLandscapeDependenciesViaRepositoriesWithSameContributors = subLandscapeDependenciesViaRepositoriesWithSameContributors; }

    @JsonIgnore
    public List<ComponentDependency> getSubLandscapeIndirectDependenciesViaRepositoriesWithSameContributors() { return subLandscapeIndirectDependenciesViaRepositoriesWithSameContributors; }

    @JsonIgnore
    public void setSubLandscapeIndirectDependenciesViaRepositoriesWithSameContributors(List<ComponentDependency> subLandscapeIndirectDependenciesViaRepositoriesWithSameContributors) { this.subLandscapeIndirectDependenciesViaRepositoriesWithSameContributors = subLandscapeIndirectDependenciesViaRepositoriesWithSameContributors; }

    @JsonIgnore
    public List<ComponentDependency> getSubLandscapeDependenciesViaRepositoriesWithSameName() { return subLandscapeDependenciesViaRepositoriesWithSameName; }

    @JsonIgnore
    public void setSubLandscapeDependenciesViaRepositoriesWithSameName(List<ComponentDependency> subLandscapeDependenciesViaRepositoriesWithSameName) { this.subLandscapeDependenciesViaRepositoriesWithSameName = subLandscapeDependenciesViaRepositoriesWithSameName; }

    @JsonIgnore
    public List<ComponentDependency> getSubLandscapeIndirectDependenciesViaRepositoriesWithSameName() { return subLandscapeIndirectDependenciesViaRepositoriesWithSameName; }

    @JsonIgnore
    public void setSubLandscapeIndirectDependenciesViaRepositoriesWithSameName(List<ComponentDependency> subLandscapeIndirectDependenciesViaRepositoriesWithSameName) { this.subLandscapeIndirectDependenciesViaRepositoriesWithSameName = subLandscapeIndirectDependenciesViaRepositoriesWithSameName; }

    public SourceFileAgeDistribution getOverallFileLastModifiedDistribution() { return getOverallFileLastModifiedDistribution(this.getFilteredRepositoryAnalysisResults()); }

    public SourceFileAgeDistribution getOverallFileFirstModifiedDistribution() { return getOverallFileFirstModifiedDistribution(this.getFilteredRepositoryAnalysisResults()); }

    public LandscapeConfiguration getConfiguration() { return configuration; }

    @JsonIgnore
    public void setConfiguration(LandscapeConfiguration configuration) { this.configuration = configuration; }

    @JsonIgnore
    public List<RepositoryAnalysisResults> getRepositoryAnalysisResults() { return repositoryAnalysisResults; }

    @JsonIgnore
    public void setRepositoryAnalysisResults(List<RepositoryAnalysisResults> repositoryAnalysisResults) { this.repositoryAnalysisResults = repositoryAnalysisResults; }

    @JsonIgnore
    public List<RepositoryAnalysisResults> getFilteredRepositoryAnalysisResults() {
        int thresholdLoc = configuration.getRepositoryThresholdLocMain();
        int thresholdContributors = configuration.getRepositoryThresholdContributors();

        String updatedBefore = configuration.getIgnoreRepositoriesLastUpdatedBefore();

        return repositoryAnalysisResults
                .stream()
                .filter(p -> StringUtils.isBlank(updatedBefore) ||
                        p.getAnalysisResults().getContributorsAnalysisResults().getLatestCommitDate().compareTo(updatedBefore) >= 0)
                .filter(p -> {
                    CodeAnalysisResults results = p.getAnalysisResults();
                    int contributorsCount = results.getContributorsAnalysisResults().getContributors().size();
                    return results.getMainAspectAnalysisResults().getLinesOfCode() >= thresholdLoc
                            && (contributorsCount == 0 || contributorsCount >= thresholdContributors);
                })
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<RepositoryAnalysisResults> getIgnoredRepositoryAnalysisResults() {
        List<RepositoryAnalysisResults> filteredRepositoryAnalysisResults = getFilteredRepositoryAnalysisResults();

        return repositoryAnalysisResults
                .stream()
                .filter(p -> !filteredRepositoryAnalysisResults.contains(p))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<RepositoryAnalysisResults> getAllRepositories() { return this.repositoryAnalysisResults; }

    @JsonIgnore
    public List<HistoryPerExtension> getYearlyCommitHistoryPerExtension() { return extensions.getYearlyCommitHistoryPerExtension(); }

    public int getRepositoriesCount() { return getFilteredRepositoryAnalysisResults().size(); }

    public int getMainLoc() { return LandscapeCodeSize.getMainLoc(getFilteredRepositoryAnalysisResults()); }

    public int getMainFilesCount() { return LandscapeCodeSize.getMainFilesCount(getFilteredRepositoryAnalysisResults()); }

    public int getSecondaryLoc() { return getTestLoc() + getGeneratedLoc() + getBuildAndDeploymentLoc() + getOtherLoc(); }

    public int getSecondaryFilesCount() { return getTestFilesCount() + getGeneratedFilesCount() + getBuildAndDeploymentFilesCount() + getOtherFilesCount(); }

    public int getMainLoc1YearActive() { return getLoc1YearActive(getFilteredRepositoryAnalysisResults()); }

    public int getMainLoc30DaysActive() { return getLoc30DaysActive(getFilteredRepositoryAnalysisResults()); }

    public int getMainLocNew() { return getLocNew(getFilteredRepositoryAnalysisResults()); }

    public int getTestLoc() { return LandscapeCodeSize.getTestLoc(getFilteredRepositoryAnalysisResults()); }

    public int getGeneratedLoc() { return LandscapeCodeSize.getGeneratedLoc(getFilteredRepositoryAnalysisResults()); }

    public int getBuildAndDeploymentLoc() { return LandscapeCodeSize.getBuildAndDeploymentLoc(getFilteredRepositoryAnalysisResults()); }

    public int getOtherLoc() { return LandscapeCodeSize.getOtherLoc(getFilteredRepositoryAnalysisResults()); }

    public int getTestFilesCount() { return LandscapeCodeSize.getTestFilesCount(getFilteredRepositoryAnalysisResults()); }

    public int getGeneratedFilesCount() { return LandscapeCodeSize.getGeneratedFilesCount(getFilteredRepositoryAnalysisResults()); }

    public int getBuildAndDeploymentFilesCount() { return LandscapeCodeSize.getBuildAndDeploymentFilesCount(getFilteredRepositoryAnalysisResults()); }

    public int getOtherFilesCount() { return LandscapeCodeSize.getOtherFilesCount(getFilteredRepositoryAnalysisResults()); }

    public int getAllLoc() { return LandscapeCodeSize.getAllLoc(getFilteredRepositoryAnalysisResults()); }

    @JsonIgnore
    public List<NumericMetric> getMainLinesOfCodePerExtension() { return getLinesOfCodePerExtension(CodeCategory.MAIN); }

    @JsonIgnore
    public List<NumericMetric> getTestLinesOfCodePerExtension() { return getLinesOfCodePerExtension(CodeCategory.TEST); }

    @JsonIgnore
    public List<NumericMetric> getOtherLinesOfCodePerExtension() { return getLinesOfCodePerExtension(CodeCategory.OTHER); }

    @JsonIgnore
    public List<NumericMetric> getLinesOfCodePerExtension(CodeCategory type) { return extensions.getLinesOfCodePerExtension(type); }

    @JsonIgnore
    public List<String> getAllExtensions() { return extensions.getAllExtensions(); }

    @JsonIgnore
    public List<ContributorRepositories> getContributors() { return contributors.getContributors(); }

    @JsonIgnore
    public List<ContributorRepositories> getTeams() { return contributors.getTeams(); }

    @JsonIgnore
    public List<ContributorRepositories> getBots() { return contributors.getBots(); }

    @JsonIgnore
    public List<CommitsPerExtension> getContributorsPerExtension() { return extensions.getContributorsPerExtension(); }

    @JsonIgnore
    public List<ContributionTimeSlot> getContributorsPerYear() { return timeSlots.getContributorsPerYear(); }

    @JsonIgnore
    public List<ContributionTimeSlot> getContributorsPerWeek() { return timeSlots.getContributorsPerWeek(); }

    @JsonIgnore
    public List<ContributionTimeSlot> getContributorsPerDay() { return timeSlots.getContributorsPerDay(); }

    @JsonIgnore
    public List<ContributionTimeSlot> getContributorsPerMonth() { return timeSlots.getContributorsPerMonth(); }

    @JsonIgnore
    public List<Pair<String, List<ContributionTimeSlot>>> getContributorsPerRepositoryAndMonth() { return timeSlots.getContributorsPerRepositoryAndMonth(); }

    @JsonIgnore
    public List<Pair<String, List<ContributionTimeSlot>>> getContributorsCommits() { return timeSlots.getContributorsCommits(); }

    public int getCommitsCount() {
        return this.repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults()
                        .getContributorsAnalysisResults().getCommitsCount()).sum();
    }

    public int getCommitsCount30Days() {
        return this.repositoryAnalysisResults.stream()
                .mapToInt(p -> p.getAnalysisResults()
                        .getContributorsAnalysisResults().getCommitsCount30Days()).sum();
    }

    public int getContributorsCount(List<ContributorRepositories> contributors) { return contributors.size(); }

    @JsonIgnore
    public List<ContributorRepositories> getRecentContributors(List<ContributorRepositories> contributors) { return contributors.stream().filter(c -> c.getContributor().getCommitsCount30Days() > 0).collect(Collectors.toCollection(ArrayList::new)); }

    public int getRecentContributorsCount() {
        Set<String> ids = new HashSet<>();

        for (RepositoryAnalysisResults repository: repositoryAnalysisResults) {
            for (Contributor contributor : repository.getAnalysisResults().getContributorsAnalysisResults().getContributors()) {
                if (contributor.getCommitsCount30Days() > 0) {
                    ids.add(contributor.getEmail());
                }
            }

        }
        return ids.size();
    }

    public int getRecentContributorsCount(List<ContributorRepositories> contributors) { return getRecentContributors(contributors).size(); }

    public int getRecentContributorsCount6Months(List<ContributorRepositories> contributors) { return (int) contributors.stream().filter(c -> c.getContributor().isActive(180)).count(); }

    public int getRecentContributorsCount3Months(List<ContributorRepositories> contributors) { return (int) contributors.stream().filter(c -> c.getContributor().isActive(90)).count(); }

    public int getRookiesContributorsCount(List<ContributorRepositories> contributors) { return (int) contributors.stream().filter(c -> c.getContributor().isRookie(RECENT_THRESHOLD_DAYS)).count(); }

    @JsonIgnore
    public List<ComponentDependency> getPeopleDependencies30Days() { return peopleDependencies30Days; }

    @JsonIgnore
    public void setPeopleDependencies30Days(List<ComponentDependency> peopleDependencies30Days) { this.peopleDependencies30Days = peopleDependencies30Days; }

    public List<ComponentDependency> getPeopleRepositoryDependencies30Days() { return peopleRepositoryDependencies30Days; }

    public void setPeopleRepositoryDependencies30Days(List<ComponentDependency> peopleRepositoryDependencies30Days) { this.peopleRepositoryDependencies30Days = peopleRepositoryDependencies30Days; }

    @JsonIgnore
    public List<ComponentDependency> getPeopleDependencies90Days() { return peopleDependencies90Days; }

    @JsonIgnore
    public void setPeopleDependencies90Days(List<ComponentDependency> peopleDependencies90Days) { this.peopleDependencies90Days = peopleDependencies90Days; }

    @JsonIgnore
    public List<ComponentDependency> getPeopleDependencies180Days() { return peopleDependencies180Days; }

    @JsonIgnore
    public void setPeopleDependencies180Days(List<ComponentDependency> peopleDependencies180Days) { this.peopleDependencies180Days = peopleDependencies180Days; }

    @JsonIgnore
    public List<ContributorConnections> getConnectionsViaRepositories30Days() { return connectionsViaRepositories30Days; }

    @JsonIgnore
    public void setConnectionsViaRepositories30Days(List<ContributorConnections> connectionsViaRepositories30Days) { this.connectionsViaRepositories30Days = connectionsViaRepositories30Days; }

    @JsonIgnore
    public List<ContributorConnections> getConnectionsViaRepositories90Days() { return connectionsViaRepositories90Days; }

    @JsonIgnore
    public void setConnectionsViaRepositories90Days(List<ContributorConnections> connectionsViaRepositories90Days) { this.connectionsViaRepositories90Days = connectionsViaRepositories90Days; }

    @JsonIgnore
    public List<ContributorConnections> getConnectionsViaRepositories180Days() { return connectionsViaRepositories180Days; }

    @JsonIgnore
    public void setConnectionsViaRepositories180Days(List<ContributorConnections> connectionsViaRepositories180Days) { this.connectionsViaRepositories180Days = connectionsViaRepositories180Days; }

    public double getcIndex30Days() { return cIndex30Days; }

    public void setcIndex30Days(double cIndex30Days) { this.cIndex30Days = cIndex30Days; }

    public double getcIndex90Days() { return cIndex90Days; }

    public void setcIndex90Days(double cIndex90Days) { this.cIndex90Days = cIndex90Days; }

    public double getcIndex180Days() { return cIndex180Days; }

    public void setcIndex180Days(double cIndex180Days) { this.cIndex180Days = cIndex180Days; }

    public double getcMean30Days() { return cMean30Days; }

    public void setcMean30Days(double cMean30Days) { this.cMean30Days = cMean30Days; }

    public double getcMean90Days() { return cMean90Days; }

    public void setcMean90Days(double cMean90Days) { this.cMean90Days = cMean90Days; }

    public double getcMean180Days() { return cMean180Days; }

    public void setcMean180Days(double cMean180Days) { this.cMean180Days = cMean180Days; }

    public double getcMedian30Days() { return cMedian30Days; }

    public void setcMedian30Days(double cMedian30Days) { this.cMedian30Days = cMedian30Days; }

    public double getcMedian90Days() { return cMedian90Days; }

    public void setcMedian90Days(double cMedian90Days) { this.cMedian90Days = cMedian90Days; }

    public double getcMedian180Days() { return cMedian180Days; }

    public void setcMedian180Days(double cMedian180Days) { this.cMedian180Days = cMedian180Days; }

    public double getpIndex30Days() { return pIndex30Days; }

    public void setpIndex30Days(double pIndex30Days) { this.pIndex30Days = pIndex30Days; }

    public double getpIndex90Days() { return pIndex90Days; }

    public void setpIndex90Days(double pIndex90Days) { this.pIndex90Days = pIndex90Days; }

    public double getpIndex180Days() { return pIndex180Days; }

    public void setpIndex180Days(double pIndex180Days) { this.pIndex180Days = pIndex180Days; }

    public double getpMean30Days() { return pMean30Days; }

    public void setpMean30Days(double pMean30Days) { this.pMean30Days = pMean30Days; }

    public double getpMean90Days() { return pMean90Days; }

    public void setpMean90Days(double pMean90Days) { this.pMean90Days = pMean90Days; }

    public double getpMean180Days() { return pMean180Days; }

    public void setpMean180Days(double pMean180Days) { this.pMean180Days = pMean180Days; }

    public double getpMedian30Days() { return pMedian30Days; }

    public void setpMedian30Days(double pMedian30Days) { this.pMedian30Days = pMedian30Days; }

    public double getpMedian90Days() { return pMedian90Days; }

    public void setpMedian90Days(double pMedian90Days) { this.pMedian90Days = pMedian90Days; }

    public double getpMedian180Days() { return pMedian180Days; }

    public void setpMedian180Days(double pMedian180Days) { this.pMedian180Days = pMedian180Days; }

    public List<Double> getcIndex30DaysHistory() { return cIndex30DaysHistory; }

    public void setcIndex30DaysHistory(List<Double> cIndex30DaysHistory) { this.cIndex30DaysHistory = cIndex30DaysHistory; }

    public List<Double> getpIndex30DaysHistory() { return pIndex30DaysHistory; }

    public void setpIndex30DaysHistory(List<Double> pIndex30DaysHistory) { this.pIndex30DaysHistory = pIndex30DaysHistory; }

    public List<Double> getcMean30DaysHistory() { return cMean30DaysHistory; }

    public void setcMean30DaysHistory(List<Double> cMean30DaysHistory) { this.cMean30DaysHistory = cMean30DaysHistory; }

    public List<Double> getpMean30DaysHistory() { return pMean30DaysHistory; }

    public void setpMean30DaysHistory(List<Double> pMean30DaysHistory) { this.pMean30DaysHistory = pMean30DaysHistory; }

    public List<Double> getcMedian30DaysHistory() { return cMedian30DaysHistory; }

    public void setcMedian30DaysHistory(List<Double> cMedian30DaysHistory) { this.cMedian30DaysHistory = cMedian30DaysHistory; }

    public List<Double> getpMedian30DaysHistory() { return pMedian30DaysHistory; }

    public void setpMedian30DaysHistory(List<Double> pMedian30DaysHistory) { this.pMedian30DaysHistory = pMedian30DaysHistory; }

    public List<Double> getConnectionsViaRepositories30DaysCountHistory() { return connectionsViaRepositories30DaysCountHistory; }

    public void setConnectionsViaRepositories30DaysCountHistory(List<Double> connectionsViaRepositories30DaysCountHistory) { this.connectionsViaRepositories30DaysCountHistory = connectionsViaRepositories30DaysCountHistory; }

    public List<Double> getPeopleDependenciesCount30DaysHistory() { return peopleDependenciesCount30DaysHistory; }

    public void setPeopleDependenciesCount30DaysHistory(List<Double> peopleDependenciesCount30DaysHistory) { this.peopleDependenciesCount30DaysHistory = peopleDependenciesCount30DaysHistory; }

    public List<Double> getActiveContributors30DaysHistory() { return activeContributors30DaysHistory; }

    public void setActiveContributors30DaysHistory(List<Double> activeContributors30DaysHistory) { this.activeContributors30DaysHistory = activeContributors30DaysHistory; }

    public double getC2cConnectionsCount30Days() { return c2cConnectionsCount30Days; }

    public void setC2cConnectionsCount30Days(double c2cConnectionsCount30Days) { this.c2cConnectionsCount30Days = c2cConnectionsCount30Days; }

    public double getC2pConnectionsCount30Days() { return c2pConnectionsCount30Days; }

    public void setC2pConnectionsCount30Days(double c2pConnectionsCount30Days) { this.c2pConnectionsCount30Days = c2pConnectionsCount30Days; }

    public String getFirstCommitDate() { return firstCommitDate; }

    public void setFirstCommitDate(String firstCommitDate) { this.firstCommitDate = firstCommitDate; }

    public String getLatestCommitDate() { return latestCommitDate; }

    public void setLatestCommitDate(String latestCommitDate) { this.latestCommitDate = latestCommitDate; }

    public TeamsConfig getTeamsConfig() { return teamsConfig; }

    public void setTeamsConfig(TeamsConfig teamsConfig) { this.teamsConfig = teamsConfig; }

    public PeopleConfig getPeopleConfig() { return peopleConfig; }

    public void setPeopleConfig(PeopleConfig peopleConfig) { this.peopleConfig = peopleConfig; }

    enum CodeCategory {
        MAIN, TEST, OTHER
    }

}
