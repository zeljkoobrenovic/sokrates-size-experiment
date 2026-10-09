/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.scoping;

import java.util.List;

class OtherConventions {
    static void addTo(List<Convention> conventions) {
        addPart1(conventions);
        addPart2(conventions);
        addPart3(conventions);
        addPart4(conventions);
    }

    private static void addPart1(List<Convention> conventions) {
        // static code analysis configurations
        conventions.add(new Convention(".*/vendor/.*", "", "Vendor files"));
        conventions.add(new Convention(".*/sonatype-settings[.]xml", "", "Sonatype configuration"));
        conventions.add(new Convention(".*/config/checkstyle/.*", "", "Checkstyle configuration"));
        conventions.add(new Convention(".*/checkstyle[.]xml", "", "Checkstyle configuration"));
        conventions.add(new Convention(".*/checkstyle.*", "", "Checkstyle configuration"));

        conventions.add(new Convention(".*[.]md", "", "Markdown files"));
        conventions.add(new Convention(".*[.]markdown", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mdown", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mdwn", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mdx", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mkd", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mkdn", "", "Markdown files"));
        conventions.add(new Convention(".*[.]mkdown", "", "Markdown files"));

        conventions.add(new Convention(".*[.]adoc", "", "AsciiDoc documentation"));

        conventions.add(new Convention(".*[.](rst|rest|resttxt|rsttxt)", "", "reST files"));

        conventions.add(new Convention(".*[.]ronn", "", "Markdown files"));
        conventions.add(new Convention(".*[.]workbook", "", "Markdown files"));
        conventions.add(new Convention(".*[.]plist", "", "Property list files"));

        conventions.add(new Convention(".*[.]json", "", "JSON files"));
        // conventions.add(new Convention(".*[.]yml", "", "YAML files"));
        // conventions.add(new Convention(".*[.]yaml", "", "YAML files"));

        conventions.add(new Convention(".*[.]svg", "", "SVG files"));

        // ignore lists
        conventions.add(new Convention(".*/[.]atomignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]babelignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]bzrignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]coffeelintignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]cvsignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]dockerignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]eslintignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]gitignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]nodemonignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]npmignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]prettierignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]stylelintignore", "", "Ignore list"));
        conventions.add(new Convention(".*/[.]vscodeignore", "", "Ignore list"));
    }

    private static void addPart2(List<Convention> conventions) {
        conventions.add(new Convention(".*/[.]cpplint[.]py", "", "Linter"));

        conventions.add(new Convention(".*[.]storyboard", "", "Storyboard"));
        conventions.add(new Convention(".*[.]xib", "", "XIB files"));

        conventions.add(new Convention(".*[.]bash_[a-z]+", "", "Bash files"));

        // config
        conventions.add(new Convention(".*[.]apacheconf", "", "Configuration"));
        conventions.add(new Convention(".*[.]vhost", "", "Configuration"));
        conventions.add(new Convention(".*/[.]htaccess", "", "Configuration"));
        conventions.add(new Convention(".*[.]csf", "", "Configuration"));
        conventions.add(new Convention(".*[.]diff", "", "Configuration"));
        conventions.add(new Convention(".*[.]patch", "", "Configuration"));

        conventions.add(new Convention(".*[.]editorconfig", "", "NPM Config"));

        conventions.add(new Convention(".*[.]npmrc", "", "Editor configuration"));

        conventions.add(new Convention(".*[.]properties", "", "Properties"));
        conventions.add(new Convention(".*[.]po", "", "Properties"));

        conventions.add(new Convention(".*[.]dsp", "", "Microsoft Developer Studio repository"));

        conventions.add(new Convention(".*[.]txi", "", "Textinfo"));
        conventions.add(new Convention(".*[.]texi", "", "Textinfo"));
        conventions.add(new Convention(".*[.]texinfo", "", "Textinfo"));

        conventions.add(new Convention(".*[.]txt", "", "Text files"));
        conventions.add(new Convention(".*[.]fr", "", "Text files"));
        conventions.add(new Convention(".*[.]nb", "", "Text files"));
        conventions.add(new Convention(".*[.]ncl", "", "Text files"));
        conventions.add(new Convention(".*[.]no", "", "Text files"));

        conventions.add(new Convention(".*/COPYING", "", "Text files"));
        conventions.add(new Convention(".*/COPYING[.][a-z0-9]+", "", "Text files"));
        conventions.add(new Convention(".*/COPYRIGHT", "", "Text files"));
        conventions.add(new Convention(".*/COPYRIGHT[.][a-z0-9]+", "", "Text files"));
        conventions.add(new Convention(".*/FONTLOG", "", "Text files"));
        conventions.add(new Convention(".*/INSTALL", "", "Text files"));
        conventions.add(new Convention(".*/INSTALL[.][a-z0-9]+", "", "Text files"));
        conventions.add(new Convention(".*/LICENSE", "", "Text files"));
        conventions.add(new Convention(".*/LICENSE[.][a-z0-9]+", "", "Text files"));
        conventions.add(new Convention(".*/NEWS", "", "Text files"));
        conventions.add(new Convention(".*/README", "", "Text files"));
        conventions.add(new Convention(".*/README[.][a-z0-9]+", "", "Text files"));
    }

    private static void addPart3(List<Convention> conventions) {
        conventions.add(new Convention(".*/CHANGE(S|LOG)?(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/CONTRIBUTING(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/COPYING(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/INSTALL(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/LICEN[CS]E(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/[Ll]icen[cs]e(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/README(\\.|)", "", "Documentation"));
        conventions.add(new Convention(".*/[Rr]eadme(\\.|)", "", "Documentation"));

        conventions.add(new Convention(".*/click[.]me", "", "Text files"));
        conventions.add(new Convention(".*/delete[.]me", "", "Text files"));
        conventions.add(new Convention(".*/keep[.]me", "", "Text files"));
        conventions.add(new Convention(".*/read[.]me", "", "Text files"));
        conventions.add(new Convention(".*/test[.]me", "", "Text files"));
        conventions.add(new Convention(".*/go[.]mod", "", "Text files"));
        conventions.add(new Convention(".*/go[.]sum", "", "Text files"));
        conventions.add(new Convention(".*/package[.]mask", "", "Text files"));
        conventions.add(new Convention(".*/package[.]use[.]mask", "", "Text files"));
        conventions.add(new Convention(".*/package[.]use[.]stable[.]mask", "", "Text files"));
        conventions.add(new Convention(".*/readme[.]1st", "", "Text files"));
        conventions.add(new Convention(".*/use[.]mask", "", "Text files"));
        conventions.add(new Convention(".*/use[.]stable[.]mask", "", "Text files"));

        conventions.add(new Convention(".*[.]indent[.]pro", "", "Text files"));

        conventions.add(new Convention(".*[.]lock", "", "Locked files"));

        conventions.add(new Convention(".*[.]scm", "", "SCM files"));


        conventions.add(new Convention(".*/[Dd]ocumentation/.*", "", "Documentation"));
        conventions.add(new Convention(".*/asciidoc/.*", "", "Documentation"));
        conventions.add(new Convention(".*/[Mm]an/.*", "", "Documentation"));
        conventions.add(new Convention(".*/[Ee]xamples/.*", "", "Documentation"));
        conventions.add(new Convention(".*/[Ss]amples/.*", "", "Samples"));
        conventions.add(new Convention(".*/[Dd]emos?/.*", "", "Documentation"));
        conventions.add(new Convention(".*[.]3pm", "", "Manual pages"));
        conventions.add(new Convention(".*[.]vim", "", "vim editor config"));
    }

    private static void addPart4(List<Convention> conventions) {
        conventions.add(new Convention(".*[.]_js", "", ""));
        conventions.add(new Convention(".*[.]sublime-project", "", ""));
        conventions.add(new Convention(".*[.]ini", "", "INI files"));
        conventions.add(new Convention(".*[.]libsonnet", "", "Libsonnet files"));
        conventions.add(new Convention(".*[.]tab", "", "Table files"));
        conventions.add(new Convention(".*[.]xmi", "", "XMI files"));

        conventions.add(new Convention(".*changers[.]xml", "", "Changes documentation"));
        conventions.add(new Convention(".*/resources/.*[.]xsd", "", "XSD files"));
        conventions.add(new Convention(".*/wp[-]includes/.*", "", "WordPress includes"));
        conventions.add(new Convention(".*/changes[.]xml", "", "Changes log"));

        conventions.add(new Convention(".*[.]pb", "", "Protocol buffer (protobuf) files"));
        conventions.add(new Convention(".*[.]obj", "", "Geometry definition files"));
        conventions.add(new Convention(".*[.]mtl", "", "Material Template Library files"));
        conventions.add(new Convention(".*[.]urdf", "", "URDF files"));

        conventions.add(new Convention(".*/site[-]packages/.*", "", "3rd party libraries and artifacts"));
    }
}
