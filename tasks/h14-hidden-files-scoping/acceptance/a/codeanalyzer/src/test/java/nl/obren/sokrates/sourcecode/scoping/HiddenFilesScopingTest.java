package nl.obren.sokrates.sourcecode.scoping;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HiddenFilesScopingTest {

    private final ScopingConventions conventions = new ScopingConventions();

    private boolean matchesAny(List<Convention> rules, String path) {
        return rules.stream().anyMatch(c -> c.pathMatches(path));
    }

    private void assertIgnored(String path) {
        assertTrue(matchesAny(conventions.getIgnoredFilesConventions(), path), "expected IGNORED but was kept: " + path);
    }

    private void assertNotIgnored(String path) {
        assertFalse(matchesAny(conventions.getIgnoredFilesConventions(), path), "expected KEPT but was ignored: " + path);
    }

    private void assertBuildAndDeployment(String path) {
        assertTrue(matchesAny(conventions.getBuildAndDeploymentFilesConventions(), path),
                "expected BUILD-AND-DEPLOYMENT but was not classified there: " + path);
    }

    @Test
    void ordinarySourceFilesAreNotIgnored() {
        assertNotIgnored("../src/main/java/org/acme/Foo.java");
        assertNotIgnored("..\\src\\Foo.java");
        assertNotIgnored("/Users/dev/project/README.md");
    }

    @Test
    void plainDotfilesAreKept() {
        assertNotIgnored("../.gitignore");
        assertNotIgnored("../clients/.gitignore");
        assertNotIgnored("a/b/c/.env");
        assertNotIgnored("../foo/.eslintrc.json");
        assertNotIgnored("../.editorconfig");
        assertNotIgnored("../.gitlab-ci.yml");
        assertNotIgnored("../sub/.travis.yml");
    }

    @Test
    void sourceUnderIncidentallyDottedFolderIsKept() {
        assertNotIgnored("../src/.config/Foo.java");
        assertNotIgnored("../app/.generated/Bar.kt");
    }

    @Test
    void vcsMetadataDirectoriesAreIgnoredAtEveryDepth() {
        assertIgnored(".git/config");
        assertIgnored("../.git/config");
        assertIgnored("../clients/.svn/entries");
        assertIgnored("a/b/.hg/store/data.i");
        assertIgnored("../.bzr/checkout");
    }

    @Test
    void ideAndToolDirectoriesAreBuildAndDeploymentNotIgnored() {
        for (String path : List.of("../.idea/workspace.xml", ".vscode/settings.json", "../.vs/config.json",
                "../sub/.gradle/cache.properties", "../.mvn/wrapper/maven-wrapper.properties",
                "../.settings/org.eclipse.jdt.core.prefs", "../.metadata/version.ini", "../.circleci/config.yml")) {
            assertNotIgnored(path);
            assertBuildAndDeployment(path);
        }
    }

    @Test
    void aFolderNamedDependenciesIsNotIgnored() {
        assertNotIgnored("../src/dependencies/Resolver.java");
    }

    @Test
    void yarnReleasesAndPluginsAreStillIgnored() {
        assertIgnored("../.yarn/releases/yarn-3.2.0.cjs");
        assertIgnored("../.yarn/plugins/plugin.cjs");
    }
}
