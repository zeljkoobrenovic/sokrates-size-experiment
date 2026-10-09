/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.landscape.analysis;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.githistory.GitHistoryUtils;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.utils.EmailTransformations;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

class LandscapeContributorsAggregator {
    private final LandscapeAnalysisResults results;
    private List<ContributorRepositories> contributorsCache;
    private List<ContributorRepositories> teamsCache;
    private List<ContributorRepositories> botsCache;

    LandscapeContributorsAggregator(LandscapeAnalysisResults results) {
        this.results = results;
    }

    private LandscapeConfiguration configuration() {
        return results.getConfiguration();
    }

    List<ContributorRepositories> getContributors() {
        if (contributorsCache != null) {
            return contributorsCache;
        }
        int thresholdCommits = configuration().getContributorThresholdCommits();
        List<ContributorRepositories> contributorRepositories = getAllContributors().stream()
                .filter(c -> c.getContributor().getCommitsCount() >= thresholdCommits)
                .filter(c -> !isBot(c.getContributor().getEmail()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (configuration().isAnonymizeContributors()) {
            int[] counter = {1};
            contributorRepositories.forEach(contributorRepository -> {
                contributorRepository.getContributor().setEmail("Email " + counter[0]);
                contributorRepository.getContributor().setUserName("User " + counter[0]);
                counter[0] += 1;
            });
        }
        contributorsCache = contributorRepositories;
        return contributorRepositories;
    }

    List<ContributorRepositories> getTeams() {
        if (teamsCache != null) {
            return teamsCache;
        }
        List<ContributorRepositories> teamRepositories = getAllTeams()
                .stream().collect(Collectors.toCollection(ArrayList::new));

        teamsCache = teamRepositories;
        return teamRepositories;
    }

    private boolean isBot(String email) {
        return RegexUtils.matchesAnyPattern(email, configuration().getBots());
    }

    List<ContributorRepositories> getBots() {
        if (botsCache != null) {
            return botsCache;
        }
        int thresholdCommits = configuration().getContributorThresholdCommits();
        List<ContributorRepositories> contributorRepositories = getAllContributors().stream()
                .filter(c -> c.getContributor().getCommitsCount() >= thresholdCommits)
                .filter(c -> isBot(c.getContributor().getEmail()))
                .collect(Collectors.toCollection(ArrayList::new));

        botsCache = contributorRepositories;
        return contributorRepositories;
    }

    private List<ContributorRepositories> getAllTeams() {
        final List<ContributorRepositories> contributors = new ArrayList<>(getAllContributors());
        TeamsConfig teamsConfig = results.getTeamsConfig();
        if (teamsConfig.getTeams() == null || teamsConfig.getTeams().size() == 0) {
            return contributors;
        }

        final List<ContributorRepositories> teams = new ArrayList<>();
        final Map<String, ContributorRepositories> map = new HashMap<>();

        ContributorRepositories remainder = new ContributorRepositories(new Contributor("Undefined Team"));

        while (contributors.size() > 0) {
            ContributorRepositories contributor = contributors.remove(0);
            String email = contributor.getContributor().getEmail();

            if (isBot(email)) continue;

            boolean added = addToTeams(teamsConfig, contributor, email, teams, map);

            if (!added && !remainder.getMembers().contains(email)) {
                remainder.getMembers().add(contributor);
                contributor.getRepositories().forEach(repo -> {
                    addRepoToTeam(repo, remainder);
                });
            }
        }

        if (remainder.getMembers().size() > 0 && remainder.getRepositories().size() > 0) {
            teams.add(remainder);
        }

        return teams;
    }

    private boolean addToTeams(TeamsConfig teamsConfig, ContributorRepositories contributor, String email,
                               List<ContributorRepositories> teams, Map<String, ContributorRepositories> map) {
        final boolean[] added = {false};

        teamsConfig.getTeams().forEach(teamConfig -> {
            if (added[0]) return;

            String name = teamConfig.getName();
            if (RegexUtils.matchesAnyPattern(email, teamConfig.getEmailPatterns())) {
                ContributorRepositories teamTemp = map.get(name);

                if (teamTemp == null) {
                    teamTemp = new ContributorRepositories(new Contributor(name));
                    map.put(name, teamTemp);
                    teams.add(teamTemp);
                }

                final ContributorRepositories team = teamTemp;
                team.getMembers().add(contributor);

                contributor.getRepositories().forEach(repo -> {
                    addRepoToTeam(repo, team);
                });

                added[0] = true;
            }
        });

        return added[0];
    }

    private void addRepoToTeam(ContributorRepositoryInfo repo, ContributorRepositories team) {
        Contributor teamData = team.getContributor();
        teamData.setCommitsCount(teamData.getCommitsCount() + repo.getCommitsCount());
        teamData.setCommitsCount30Days(teamData.getCommitsCount30Days() + repo.getCommits30Days());
        teamData.setCommitsCount90Days(teamData.getCommitsCount90Days() + repo.getCommits90Days());
        teamData.setCommitsCount180Days(teamData.getCommitsCount180Days() + repo.getCommits180Days());
        teamData.setCommitsCount365Days(teamData.getCommitsCount365Days() + repo.getCommits365Days());
        if (StringUtils.isBlank(teamData.getFirstCommitDate()) || repo.getFirstCommitDate().compareTo(teamData.getFirstCommitDate()) < 0) {
            teamData.setFirstCommitDate(repo.getFirstCommitDate());
        }
        if (StringUtils.isBlank(teamData.getLatestCommitDate()) || repo.getLatestCommitDate().compareTo(teamData.getLatestCommitDate()) > 0) {
            teamData.setLatestCommitDate(repo.getLatestCommitDate());
        }
        team.addRepository(repo.getRepositoryAnalysisResults(), repo.getFirstCommitDate(), repo.getLatestCommitDate(),
                repo.getCommitsCount(), repo.getCommits30Days(), repo.getCommits90Days(),
                repo.getCommits180Days(), repo.getCommits365Days(), repo.getCommitDates());
    }

    List<ContributorRepositories> getAllContributors() {
        List<ContributorRepositories> list = new ArrayList<>();
        Map<String, ContributorRepositories> map = new HashMap<>();

        results.getFilteredRepositoryAnalysisResults().forEach(repositoryAnalysisResults -> {
            ContributorsAnalysisResults contributorsAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getContributorsAnalysisResults();
            contributorsAnalysisResults.getContributors().forEach(contributor -> {
                addContributor(list, map, repositoryAnalysisResults, contributor);
            });
        });

        Collections.sort(list, (a, b) -> b.getContributor().getCommitsCount() - a.getContributor().getCommitsCount());

        return list;
    }

    private void addContributor(List<ContributorRepositories> list, Map<String, ContributorRepositories> map,
                                RepositoryAnalysisResults repositoryAnalysisResults, Contributor contributor) {
        String contributorId = contributor.getEmail().toLowerCase();
        if (GitHistoryUtils.shouldIgnore(contributorId, configuration().getIgnoreContributors())) {
            return;
        }
        contributorId = EmailTransformations.transformEmail(contributorId, configuration().getTransformContributorEmails(), results.getPeopleConfig());
        if (GitHistoryUtils.shouldIgnore(contributorId, configuration().getIgnoreContributors())) {
            return;
        }

        if (StringUtils.isBlank(contributorId)) {
            return;
        }

        if (map.containsKey(contributorId)) {
            mergeContributor(map.get(contributorId), repositoryAnalysisResults, contributor);
        } else {
            ContributorRepositories newContributorWithRepositories = newContributor(contributorId, repositoryAnalysisResults, contributor);

            map.put(contributorId, newContributorWithRepositories);
            list.add(newContributorWithRepositories);
        }
    }

    private void mergeContributor(ContributorRepositories existingContributor, RepositoryAnalysisResults repositoryAnalysisResults, Contributor contributor) {
        int repositoryCommits = contributor.getCommitsCount();
        List<String> commitDates = contributor.getCommitDates();
        int repositoryCommits30Days = contributor.getCommitsCount30Days();
        int repositoryCommits90Days = contributor.getCommitsCount90Days();
        int repositoryCommits180Days = contributor.getCommitsCount180Days();
        int repositoryCommits365Days = contributor.getCommitsCount365Days();

        String latestCommitDate = contributor.getLatestCommitDate();
        String firstCommitDate = contributor.getFirstCommitDate();

        Contributor contributorInfo = existingContributor.getContributor();

        contributorInfo.setCommitsCount(contributorInfo.getCommitsCount() + repositoryCommits);
        contributorInfo.setCommitsCount30Days(contributorInfo.getCommitsCount30Days() + repositoryCommits30Days);
        contributorInfo.setCommitsCount90Days(contributorInfo.getCommitsCount90Days() + repositoryCommits90Days);
        contributorInfo.setCommitsCount180Days(contributorInfo.getCommitsCount180Days() + repositoryCommits180Days);
        contributorInfo.setCommitsCount365Days(contributorInfo.getCommitsCount365Days() + repositoryCommits365Days);

        contributor.getActiveYears().forEach(activeYear -> {
            if (!contributorInfo.getActiveYears().contains(activeYear)) {
                contributorInfo.getActiveYears().add(activeYear);
            }
        });
        contributor.getCommitDates().forEach(commitDate -> {
            if (!contributorInfo.getCommitDates().contains(commitDate)) {
                contributorInfo.getCommitDates().add(commitDate);
            }
        });

        existingContributor.addRepository(repositoryAnalysisResults, firstCommitDate, latestCommitDate,
                repositoryCommits, repositoryCommits30Days, repositoryCommits90Days,
                repositoryCommits180Days, repositoryCommits365Days,
                new ArrayList<>(commitDates));

        if (firstCommitDate.compareTo(contributorInfo.getFirstCommitDate()) < 0) {
            contributorInfo.setFirstCommitDate(firstCommitDate);
        }
        if (latestCommitDate.compareTo(contributorInfo.getLatestCommitDate()) > 0) {
            contributorInfo.setLatestCommitDate(latestCommitDate);
        }
    }

    private ContributorRepositories newContributor(String contributorId, RepositoryAnalysisResults repositoryAnalysisResults, Contributor contributor) {
        int repositoryCommits = contributor.getCommitsCount();
        List<String> commitDates = contributor.getCommitDates();
        int repositoryCommits30Days = contributor.getCommitsCount30Days();
        int repositoryCommits90Days = contributor.getCommitsCount90Days();
        int repositoryCommits180Days = contributor.getCommitsCount180Days();
        int repositoryCommits365Days = contributor.getCommitsCount365Days();

        Contributor newContributor = new Contributor();

        newContributor.setEmail(contributorId);
        newContributor.setUserName(contributor.getUserName());
        newContributor.setCommitsCount(repositoryCommits);
        newContributor.setCommitsCount30Days(repositoryCommits30Days);
        newContributor.setCommitsCount90Days(repositoryCommits90Days);
        newContributor.setCommitsCount180Days(repositoryCommits180Days);
        newContributor.setCommitsCount365Days(repositoryCommits365Days);
        newContributor.setFirstCommitDate(contributor.getFirstCommitDate());
        newContributor.setLatestCommitDate(contributor.getLatestCommitDate());
        newContributor.setActiveYears(new ArrayList<>(contributor.getActiveYears()));
        newContributor.setCommitDates(new ArrayList<>(contributor.getCommitDates()));

        ContributorRepositories newContributorWithRepositories = new ContributorRepositories(newContributor);

        newContributorWithRepositories.addRepository(repositoryAnalysisResults, newContributor.getFirstCommitDate(),
                newContributor.getLatestCommitDate(),
                repositoryCommits, repositoryCommits30Days, repositoryCommits90Days,
                repositoryCommits180Days, repositoryCommits365Days,
                new ArrayList<>(commitDates));

        return newContributorWithRepositories;
    }
}
