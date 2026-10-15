package nl.obren.sokrates.sourcecode.landscape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.obren.sokrates.sourcecode.contributors.ContributorIdentity;

import java.util.ArrayList;
import java.util.List;

public class TeamsConfig {
    private List<TeamConfig> teams = new ArrayList<>();

    public TeamsConfig() {
    }

    public List<TeamConfig> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamConfig> teams) {
        this.teams = teams;
    }

    /**
     * The name of the first team whose email patterns match, or null; the rule is in {@link ContributorIdentity#team(String, List)}.
     */
    @JsonIgnore
    public String getTeam(String email) {
        return ContributorIdentity.team(email, teams);
    }
}
