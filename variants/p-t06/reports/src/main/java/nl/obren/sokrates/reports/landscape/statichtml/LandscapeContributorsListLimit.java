package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The single place where the landscape's contributor lists are truncated (cut off) at the
 * contributorsListLimit setting of _sokrates_landscape/config.json (1000 by default).
 * <p>
 * Used by:
 * <ul>
 * <li>{@link LandscapeContributorsReport#saveContributorsTable} - the tables of contributors.html ("All Contributors"),
 * contributors-recent.html ("Recently Active Contributors"), the bots page and the teams pages, and the
 * "The list is limited to N items (out of M)." note under them;</li>
 * <li>{@link LandscapeReportContributorsTab} - the bar chart of recent commits per contributor in the Contributors tab
 * ("contributors sorted by recent commits").</li>
 * </ul>
 * The order of the lists is not changed here (most recent commits first, as sorted by the callers).
 */
public class LandscapeContributorsListLimit {
    private LandscapeContributorsListLimit() {
    }

    /**
     * The maximal number of contributors exported to the contributor tables and to the recent commits bar chart.
     */
    public static int getLimit(LandscapeAnalysisResults landscapeAnalysisResults) {
        return landscapeAnalysisResults.getConfiguration().getContributorsListLimit();
    }

    /**
     * Returns the contributors that are exported (truncated to the limit), keeping their order.
     */
    public static <T> List<T> limit(LandscapeAnalysisResults landscapeAnalysisResults, List<T> contributors) {
        return contributors.stream().limit(getLimit(landscapeAnalysisResults)).collect(Collectors.toList());
    }

    /**
     * Adds the note "The list is limited to N items (out of M)." when the list was truncated.
     */
    public static void addLimitNote(LandscapeAnalysisResults landscapeAnalysisResults, RichTextReport report, int contributorsCount) {
        int limit = getLimit(landscapeAnalysisResults);
        if (limit < contributorsCount) {
            report.addParagraph("The list is limited to " + limit + " items (out of " + contributorsCount + ").",
                    "color:grey; font-size: 90%; margin-top: 16px; margin-left: 11px");
        }
    }
}
