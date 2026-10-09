/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.scoping;

import java.util.List;

class TestConventions {
    static void addTo(List<Convention> conventions) {
        addPart1(conventions);
        addPart2(conventions);
    }

    private static void addPart1(List<Convention> conventions) {
        String defaultNote = "Test files";
        conventions.add(new Convention(".*/[Tt]est/.*", "", defaultNote));
        conventions.add(new Convention(".*/[Tt]est/.*", "", defaultNote));
        conventions.add(new Convention(".*/[Tt]ests/.*", "", defaultNote));
        conventions.add(new Convention(".*[.][Tt]est/.*", "", defaultNote));
        conventions.add(new Convention(".*[.][Tt]ests/.*", "", defaultNote));
        conventions.add(new Convention(".*[.][Tt]est[.].*", "", defaultNote));
        conventions.add(new Convention(".*[.][Tt]ests[.].*", "", defaultNote));
        conventions.add(new Convention(".*/UnitTests?/.*", "", defaultNote));
        conventions.add(new Convention(".*[.]UnitTests/.*", "", defaultNote));
        conventions.add(new Convention(".*UnitTests[.][a-zA-Z0-9_]+", "", defaultNote));
        conventions.add(new Convention(".*/IntegrationTests?/.*", "", defaultNote));
        conventions.add(new Convention(".*/UITests?/.*", "", defaultNote));
        conventions.add(new Convention(".*/src/testPlay/.*", "", defaultNote));
        conventions.add(new Convention(".*/Unit Tests/.*", "", defaultNote));
        conventions.add(new Convention(".*/src/ciTest/.*", "", defaultNote));
        conventions.add(new Convention(".*/src/ciTests/.*", "", defaultNote));
        conventions.add(new Convention(".*/src/androidTest/.*", "", defaultNote));
        conventions.add(new Convention(".*/src/androidTests/.*", "", defaultNote));
        conventions.add(new Convention(".*/[Ss]pecs/.*", "", defaultNote));
        conventions.add(new Convention(".*[-]tests/.*", "", defaultNote));
        conventions.add(new Convention(".*/test[-]data/.*", "", defaultNote));
        conventions.add(new Convention(".*_test[.].*", "", defaultNote));
        conventions.add(new Convention(".*_tests[.].*", "", defaultNote));
        conventions.add(new Convention(".*[.]test[.].*", "", defaultNote));
        conventions.add(new Convention(".*[.]tests[.].*", "", defaultNote));
        conventions.add(new Convention(".*/test_.*", "", defaultNote));
        conventions.add(new Convention(".*/test[.].*", "", defaultNote));
        conventions.add(new Convention(".*/testing[.].*", "", defaultNote));
        conventions.add(new Convention(".*/tests_.*", "", defaultNote));
        conventions.add(new Convention(".*[-]test[-].*", "", defaultNote));
        conventions.add(new Convention(".*[-]tests[-].*", "", defaultNote));
        conventions.add(new Convention(".*__test__.*", "", defaultNote));
        conventions.add(new Convention(".*__tests__.*", "", defaultNote));
        conventions.add(new Convention(".*[.]feature", "", defaultNote));
        conventions.add(new Convention(".*[.]lint[-]test", "", defaultNote));
        conventions.add(new Convention(".*[.]lint[-]tests", "", defaultNote));
    }

    private static void addPart2(List<Convention> conventions) {
        conventions.add(new Convention(".*/vitest[.].*", "", "Vitest configuration files"));
        conventions.add(new Convention(".*/test[-]runner[.].*", "", "Vitest configuration files"));
        conventions.add(new Convention(".*[.]spec[.]ts", "", "TypeScript test files"));
        conventions.add(new Convention(".*[.]spec[.]tsx", "", "TSX (React) files"));
        conventions.add(new Convention(".*[.]spec[.]js", "", "JavaScript test files"));
        conventions.add(new Convention(".*/karma[.]conf[.]js", "", "Karma test files"));
        conventions.add(new Convention(".*/protractor[.]conf[.]js", "", "Protractor test files"));
        conventions.add(new Convention(".*/e2e/.*", "", "Protractor test files"));
        conventions.add(new Convention(".*/cppunittests/.*", "", "CPP unit test files"));
        conventions.add(new Convention(".*/palmtests/.*", "", "Palm test files"));
        conventions.add(new Convention(".*/jstests/.*", "", "JS test files"));

        conventions.add(new Convention(".*/RestAPIClientTests/.*", "", "API test files"));
        conventions.add(new Convention(".*/ViewTests/.*", "", "Test files"));

        conventions.add(new Convention(".*/test[-]resources/.*", "", "Test resources"));
        conventions.add(new Convention(".*/test[-]helpers/.*", "", "Test helpers"));
        conventions.add(new Convention(".*/TestData/.*", "", "Test data"));
        conventions.add(new Convention(".*/mockapi/.*", "", "Mock resources"));
        conventions.add(new Convention(".*/__mock[a-zA-Z0-9_\\- ]+/.*", "", "Mock resources"));
        conventions.add(new Convention(".*/mock[a-zA-Z0-9_\\- ]+/.*", "", "Mock resources"));
        conventions.add(new Convention(".*_mock[.][a-zA-Z0-9_\\-]+", "", "Mock resources"));

        conventions.add(new Convention(".*[.]snap", "", "Jest snapshots"));
        conventions.add(new Convention(".*/jest[.][a-zA-Z0-9\\.]+", "", "Jest files"));
        conventions.add(new Convention(".*/TestUtilities/.*", "", "Test utilities"));
        conventions.add(new Convention(".*/[Mm]ocks/.*", "", "Mocks"));
    }
}
