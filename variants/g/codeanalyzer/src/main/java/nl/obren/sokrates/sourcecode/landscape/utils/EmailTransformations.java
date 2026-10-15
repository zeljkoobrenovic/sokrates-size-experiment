package nl.obren.sokrates.sourcecode.landscape.utils;

import nl.obren.sokrates.sourcecode.contributors.ContributorIdentity;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.operations.OperationStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Kept for compatibility; the rule (and its static cache) lives in {@link ContributorIdentity}.
 */
public class EmailTransformations {
    private static final Log LOG = LogFactory.getLog(EmailTransformations.class);

    public static String transformEmail(final String email, List<OperationStatement> operationStatements, PeopleConfig peopleConfig) {
        return ContributorIdentity.transformEmail(email, operationStatements, peopleConfig);
    }
}
