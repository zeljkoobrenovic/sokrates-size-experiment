package nl.obren.sokrates.sourcecode.contributors;

import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.PersonConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.operations.OperationStatement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ContributorIdentityTest {
    // the transformation and people caches are static, so every test uses its own emails and names

    private ContributorIdentity identity() {
        LandscapeConfiguration configuration = new LandscapeConfiguration();
        configuration.setIgnoreContributors(Arrays.asList("dl[-].*", ".*[.]ignored"));
        configuration.setTransformContributorEmails(Collections.singletonList(
                new OperationStatement("append", Collections.singletonList(".ignored"))));
        configuration.setBots(Collections.singletonList(".*[-]bot@.*"));

        PersonConfig carol = new PersonConfig();
        carol.setName("Carol R4");
        carol.setImage("carol.png");
        carol.setEmailPatterns(Collections.singletonList("carol@r4test[.]org.*"));
        PeopleConfig peopleConfig = new PeopleConfig();
        peopleConfig.setPeople(Collections.singletonList(carol));

        TeamConfig team = new TeamConfig();
        team.setName("Team R4");
        team.setEmailPatterns(Collections.singletonList(".*@r4team[.]org"));
        TeamsConfig teamsConfig = new TeamsConfig();
        teamsConfig.setTeams(Collections.singletonList(team));

        return new ContributorIdentity(configuration, peopleConfig, teamsConfig);
    }

    @Test
    void resolveIgnoresBeforeTransforming() {
        assertNull(identity().resolve("dl-r4@r4test.org"));
    }

    @Test
    void resolveIgnoresAfterTransforming() {
        // "x@r4test.org" is not ignored itself; its transformation "x@r4test.org.ignored" is
        assertFalse(identity().isIgnored("x@r4test.org"));
        assertNull(identity().resolve("x@r4test.org"));
    }

    @Test
    void resolveMapsTransformedEmailToPersonAfterTransformation() {
        // transformation first ("carol@r4test.org.ignored"), then the people config maps it to the person name,
        // then the second ignore-check runs on the person name, which no longer matches
        assertEquals("Carol R4", identity().resolve("carol@r4test.org"));
    }

    @Test
    void transformDoesNotCheckIgnore() {
        assertEquals("y@r4test.org.ignored", identity().transform("y@r4test.org"));
    }

    @Test
    void botsAndTeams() {
        assertTrue(identity().isBot("build-bot@r4test.org"));
        assertFalse(identity().isBot("dev@r4test.org"));
        assertEquals("Team R4", identity().team("dev@r4team.org"));
        assertNull(identity().team("dev@r4test.org"));
    }

    @Test
    void person() {
        assertEquals("carol.png", identity().person("Carol R4").map(PersonConfig::getImage).orElse(null));
        assertEquals("Unknown R4", identity().person("Unknown R4").map(PersonConfig::getName).orElse(null));
        assertFalse(new ContributorIdentity(new LandscapeConfiguration(), null, null).person("Carol R4").isPresent());
        assertEquals("carol.png", ContributorIdentity.personImage(identity().person("Carol R4").get()).orElse(null));
        assertFalse(ContributorIdentity.personImage(identity().person("Unknown R4").get()).isPresent());
    }
}
