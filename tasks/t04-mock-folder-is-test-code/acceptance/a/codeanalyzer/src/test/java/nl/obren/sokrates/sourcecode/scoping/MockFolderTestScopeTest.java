package nl.obren.sokrates.sourcecode.scoping;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t04: files under a folder whose name starts with "mock" are test code,
 * including a folder named plainly "mock", one with punctuation ("mock.data") and one with
 * non-ASCII characters ("mock-größe"); the folders that were already classified as test keep
 * being, and "mock" inside a file name or in the middle of a folder name does not count.
 */
public class MockFolderTestScopeTest {
    private final ScopingConventions conventions = new ScopingConventions();

    @Test
    public void aFolderNamedPlainlyMockIsTestCode() {
        assertTestScope("/repo/x-pack/plugin/inference/qa/test-service-plugin/src/main/java/org/elasticsearch/xpack/inference/mock/TestInferenceServicePlugin.java");
        assertTestScope("/repo/src/mock/M.java");
        assertTestScope("../src/mock/deep/nested/M.java");
        assertTestScope("/repo/src/__mock/M.java");
        assertTestScope("C:\\repo\\src\\mock\\M.java");
    }

    @Test
    public void mockFoldersWithPunctuationOrNonAsciiNamesAreTestCode() {
        assertTestScope("/repo/src/mock.data/M.java");
        assertTestScope("/repo/src/mock(old)/M.java");
        assertTestScope("/repo/src/mock-größe/M.java");
        assertTestScope("/repo/src/mock模拟/M.java");
        assertTestScope("/repo/src/__mock.data/M.java");
    }

    @Test
    public void previouslyMatchingMockFoldersStayTestCode() {
        assertTestScope("/repo/src/mocks/M.java");
        assertTestScope("/repo/src/mock-data/M.java");
        assertTestScope("/repo/src/mock_data/M.java");
        assertTestScope("/repo/src/mockStore/M.java");
        assertTestScope("/repo/src/__mocks__/M.java");
    }

    @Test
    public void mockMustStartAFolderName() {
        assertNotTestScope("/repo/src/remock/M.java");
        assertNotTestScope("/repo/src/demo/M.java");
        assertNotTestScope("/repo/src/mockery.java");
        assertNotTestScope("/repo/src/mock.java");
        assertNotTestScope("../src/main/java/App.java");
    }

    private boolean matchesAny(List<Convention> rules, String path) {
        return rules.stream().anyMatch(c -> c.pathMatches(path));
    }

    private void assertTestScope(String path) {
        assertTrue(matchesAny(conventions.getTestFilesConventions(), path), "expected test scope: " + path);
    }

    private void assertNotTestScope(String path) {
        assertFalse(matchesAny(conventions.getTestFilesConventions(), path), "expected not test scope: " + path);
    }
}
