package nl.obren.sokrates.sourcecode.landscape.analysis;

import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.SokratesRepositoryLink;
import nl.obren.sokrates.sourcecode.landscape.TeamConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Acceptance test of task t01: the "Undefined Team" holds only active contributors (a commit within
 * Contributor.ACTIVITY_THRESHOLD_DAYS = 180 days of the analysis date); configured teams keep every member.
 */
public class UndefinedTeamActiveContributorsTest {
    @BeforeEach
    public void fixAnalysisDate() {
        DateUtils.dateParam = "2025-09-20";
    }

    @AfterEach
    public void resetAnalysisDate() {
        DateUtils.dateParam = null;
    }

    @Test
    public void undefinedTeamKeepsOnlyActiveContributorsWhileConfiguredTeamsKeepEveryone() {
        TeamConfig acme = new TeamConfig();
        acme.setName("Acme");
        acme.setEmailPatterns(Arrays.asList(".*@acme[.]com"));
        TeamsConfig teamsConfig = new TeamsConfig();
        teamsConfig.setTeams(new ArrayList<>(Arrays.asList(acme)));

        LandscapeAnalysisResults results = new LandscapeAnalysisResults(teamsConfig, new PeopleConfig());
        results.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(repository(
                contributor("active@acme.com", "2025-09-01"),
                contributor("dormant@acme.com", "2024-01-15"),
                contributor("active@other.org", "2025-08-15"),
                contributor("dormant@other.org", "2024-01-15"),
                contributor("undated@other.org", "")))));

        Map<String, List<String>> members = new HashMap<>();
        results.getTeams().forEach(team -> members.put(team.getContributor().getEmail(),
                team.getMembers().stream().map(m -> m.getContributor().getEmail()).sorted().collect(Collectors.toList())));

        assertEquals(Arrays.asList("active@acme.com", "dormant@acme.com"), members.get("Acme"),
                "a configured team keeps all its members");
        assertEquals(Arrays.asList("active@other.org", "undated@other.org"), members.get("Undefined Team"),
                "the Undefined Team holds only active unmatched contributors (a blank date counts as active)");
    }

    private RepositoryAnalysisResults repository(Contributor... contributors) {
        CodeAnalysisResults analysisResults = new CodeAnalysisResults();
        ContributorsAnalysisResults contributorsAnalysisResults = new ContributorsAnalysisResults();
        contributorsAnalysisResults.setContributors(new ArrayList<>(Arrays.asList(contributors)));
        analysisResults.setContributorsAnalysisResults(contributorsAnalysisResults);
        return new RepositoryAnalysisResults(new SokratesRepositoryLink("repo/data/analysisResults.json"), analysisResults, new ArrayList<>());
    }

    private Contributor contributor(String email, String latestCommitDate) {
        Contributor contributor = new Contributor(email);
        contributor.setUserName(email.substring(0, email.indexOf('@')));
        contributor.setCommitsCount(5);
        contributor.setFirstCommitDate(latestCommitDate);
        contributor.setLatestCommitDate(latestCommitDate);
        return contributor;
    }
}
