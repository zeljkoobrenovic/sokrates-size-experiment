package nl.obren.sokrates.sourcecode.contributors;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.PersonConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.operations.ComplexOperation;
import nl.obren.sokrates.sourcecode.operations.OperationStatement;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Who a contributor is, at the landscape level: the one place that holds the rules for
 * "alias" / "merge emails" / "same person" (email transformations and the people config),
 * "bot", "ignore contributor", "team" (email patterns in the teams config) and the person's "avatar" and links
 * (people config, looked up by name).
 * <p>
 * {@link #resolve(String)} applies, in this order: ignore-check, email transformation (the landscape
 * {@code transformContributorEmails} operations, then the people-config email patterns), ignore-check again.
 * {@code LandscapeAnalysisResults} uses it to merge the contributors of all repositories.
 * <p>
 * The single-repository analysis ({@code GitHistoryUtils.parseLine}) applies a different variant while reading
 * the git history: ignore, then "anonymize" or transform (the repository's {@code ComplexOperation}, without
 * a people config), ignore again, then bot. It runs before any landscape exists, so it cannot use the people
 * config, and it marks bots on each commit instead of filtering them later. It shares the ignore and bot rules
 * ({@link #isIgnored(String, List)}, {@link #isBot(String, List)}) with this class.
 * <p>
 * The transformation result is cached per email in a static cache (shared by all instances, as it was in
 * {@code EmailTransformations}); {@code PeopleConfig} keeps its own static caches.
 * Old entry points delegate here: {@code EmailTransformations.transformEmail}, {@code GitHistoryUtils.shouldIgnore},
 * {@code GitHistoryUtils.isBot} and {@code TeamsConfig.getTeam}.
 */
public class ContributorIdentity {
    private static final Map<String, String> transformCache = new HashMap<>();

    private final LandscapeConfiguration configuration;
    private final PeopleConfig peopleConfig;
    private final TeamsConfig teamsConfig;

    /**
     * The configuration objects are read on every call, so later changes to them are seen.
     * {@code peopleConfig} may be null (no people mapping); {@code teamsConfig} is only needed by {@link #team(String)}.
     */
    public ContributorIdentity(LandscapeConfiguration configuration, PeopleConfig peopleConfig, TeamsConfig teamsConfig) {
        this.configuration = configuration;
        this.peopleConfig = peopleConfig;
        this.teamsConfig = teamsConfig;
    }

    /**
     * Returns the contributor id for the email: null when the email is ignored, before or after the transformation;
     * otherwise the transformed id (which may be blank).
     */
    public String resolve(String email) {
        if (isIgnored(email)) {
            return null;
        }
        String contributorId = transform(email);
        if (isIgnored(contributorId)) {
            return null;
        }
        return contributorId;
    }

    /**
     * Email transformation only (no ignore-check): the landscape {@code transformContributorEmails} operations,
     * then the people-config mapping from email patterns to a person name.
     */
    public String transform(String email) {
        return transformEmail(email, configuration.getTransformContributorEmails(), peopleConfig);
    }

    public boolean isIgnored(String email) {
        return isIgnored(email, configuration.getIgnoreContributors());
    }

    public boolean isBot(String email) {
        return isBot(email, configuration.getBots());
    }

    /**
     * The people-config entry with this name (an empty entry with just the name if none matches);
     * empty when there is no people config.
     */
    public Optional<PersonConfig> person(String name) {
        return peopleConfig != null ? Optional.of(peopleConfig.getPersonByName(name)) : Optional.empty();
    }

    /**
     * The avatar image configured for the person, if any (a null person or a blank image counts as none).
     */
    public static Optional<String> personImage(PersonConfig personConfig) {
        if (personConfig != null && StringUtils.isNotBlank(personConfig.getImage())) {
            return Optional.of(personConfig.getImage());
        }
        return Optional.empty();
    }

    /**
     * The name of the first team whose email patterns match, or null.
     */
    public String team(String email) {
        return team(email, teamsConfig.getTeams());
    }

    /**
     * The first team whose email patterns match, or null.
     */
    public TeamConfig teamConfig(String email) {
        return teamConfig(email, teamsConfig.getTeams());
    }

    public static String transformEmail(final String email, List<OperationStatement> operationStatements, PeopleConfig peopleConfig) {
        if (transformCache.containsKey(email)) {
            return transformCache.get(email);
        }

        String contributorId = email;
        if (operationStatements.size() > 0) {
            ComplexOperation operation = new ComplexOperation(operationStatements);
            contributorId = operation.exec(contributorId);
        }

        if (peopleConfig != null) {
            contributorId = peopleConfig.getPersonFromEmailPatterns(contributorId).getName();
        }

        transformCache.put(email, contributorId);

        return contributorId;
    }

    /**
     * True if any ignore pattern matches the entire email, both lower-cased.
     */
    public static boolean isIgnored(String email, List<String> ignoreContributors) {
        for (String ignorePattern : ignoreContributors) {
            if (RegexUtils.matchesEntirely(ignorePattern.toLowerCase(), email.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBot(String email, List<String> bots) {
        return RegexUtils.matchesAnyPattern(email, bots);
    }

    public static String team(String email, List<TeamConfig> teams) {
        TeamConfig team = teamConfig(email, teams);
        return team != null ? team.getName() : null;
    }

    public static TeamConfig teamConfig(String email, List<TeamConfig> teams) {
        for (TeamConfig team : teams) {
            if (RegexUtils.matchesAnyPattern(email, team.getEmailPatterns())) {
                return team;
            }
        }
        return null;
    }
}
