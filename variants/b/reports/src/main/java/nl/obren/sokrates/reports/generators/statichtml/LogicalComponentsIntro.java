/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;

class LogicalComponentsIntro {
    private RichTextReport report;
    private final boolean forceSkipStaticDependencies;

    LogicalComponentsIntro(RichTextReport report, boolean forceSkipStaticDependencies) {
        this.report = report;
        this.forceSkipStaticDependencies = forceSkipStaticDependencies;
    }

    void appendIntroduction() {
        report.addParagraph(getShortIntro());
        report.addHtmlContent(getLongIntro());

        if (!forceSkipStaticDependencies) {
            report.startShowMoreBlock("Learn more...");
            report.startUnorderedList();
            report.addListItem("To learn more about good practices on componentization and dependencies, Sokrates recommends the following resources:");
            report.startUnorderedList();
            report.addListItem("<a target='_blank' href='https://www.martinfowler.com/ieeeSoftware/coupling.pdf'>Reduce Coupling</a>, MartinFlower.com (IEEE Software article)");
            report.addListItem("<a target='_blank' href='https://sourcemaking.com/refactoring/smells/couplers'>Couplers Code Smells</a>, sourcemaking.com");
            report.endUnorderedList();
            report.endUnorderedList();
        }

        report.endShowMoreBlock();
    }

    private String getShortIntro() {
        String shortIntro = "";
        if (forceSkipStaticDependencies) {
            shortIntro += "<b>Logical decomposition</b> is a representation of the organization of the <b>main</b> source code, where every and each file is put in exactly one <b>logical component</b>.";
        } else {
            shortIntro = "<b>Static code dependencies</b> refer to the relationships between different modules, libraries, or components in a software system that are established at compile-time rather than at runtime. These dependencies are determined by the code itself, meaning that one module directly references another through imports, includes, or explicit references in the code. Examples include:";
        }
        return shortIntro;
    }

    private String getLongIntro() {
        String longIntro = "";
        if (forceSkipStaticDependencies) {
            longIntro = "<ul>\n";
            longIntro += "<li>A software system can have <b>one</b> or <b>more</b> logical decompositions.</li>\n";
            longIntro += "<li>A logical decomposition can be defined in two ways in Sokrates.</li>\n";
            longIntro += "<li>First approach is based on the <b>folders structure</b>. " +
                    "Components are mapped to folders at defined <b>folder depth</b> relative to the source code root.</li>\n";
            longIntro += "<li>Second approach is based on <b>explicit</b> definition of each component. In such explicit definitions, components are explicitly <b>named</b> and their files are selected based on explicitly defined path and content <b>filters</b>.</li>\n";
            longIntro += "<li>A logical decomposition is considered <b>invalid</b> if a file is selected into <b>two or more components</b>. " +
                    "This constraint is introduced in order to facilitate measuring of <b>dependencies</b> among components.</li>\n";
            longIntro += "<li>Files not assigned to any component are put into a special \"<b>Unclassified</b>\" component.</li>\n";

            longIntro += "</ul>\n";
        } else {
            longIntro = "<ul>\n";
            longIntro += "<li>Library and Module Imports</li>\n";
            longIntro += "<li>Class Dependencies (Inheritance & Composition)</li>\n";
            longIntro += "<li>Function and Method Calls</li>\n";
            longIntro += "<li>Dependency Injection Without Runtime Resolution</li>\n";

            longIntro += "</ul>\n";
        }

        return longIntro;
    }
}
