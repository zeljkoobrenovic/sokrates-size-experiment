/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.scoping;

import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

// based on:
// - https://github.com/github/linguist/blob/master/lib/linguist/generated.rb
// - https://raw.githubusercontent.com/github/linguist/master/lib/linguist/documentation.yml
// - https://github.com/github/linguist/blob/master/lib/linguist/languages.yml
// - https://github.com/github/linguist/blob/master/lib/linguist/vendor.yml
public class ScopingConventions {
    private static final Log LOG = LogFactory.getLog(ScopingConventions.class);

    private List<Convention> ignoredFilesConventions = new ArrayList<>();
    private List<Convention> testFilesConventions = new ArrayList<>();
    private List<Convention> generatedFilesConventions = new ArrayList<>();
    private List<Convention> buildAndDeploymentFilesConventions = new ArrayList<>();
    private List<Convention> otherFilesConventions = new ArrayList<>();

    public ScopingConventions() {
        IgnoreConventions.addTo(ignoredFilesConventions);
        TestConventions.addTo(testFilesConventions);
        GeneratedConventions.addTo(generatedFilesConventions);
        BuildAndDeploymentConventions.addTo(buildAndDeploymentFilesConventions);
        OtherConventions.addTo(otherFilesConventions);
    }

    public static void main(String args[]) {
        printText("ignore files with:", new ScopingConventions().ignoredFilesConventions, "  - ");
        printText("add to the test scope files with:", new ScopingConventions().testFilesConventions, "   - ");
        printText("add to the generated scope files with:", new ScopingConventions().generatedFilesConventions, "   - ");
        printText("add to the build-and-deploy scope files with:", new ScopingConventions().buildAndDeploymentFilesConventions, "   - ");
        printText("add to the other scope files with:", new ScopingConventions().otherFilesConventions, "   - ");
    }

    private static void printText(String s, List<Convention> ignoredFilesConventions, String s2) {
        LOG.info(s);
        ignoredFilesConventions.forEach(convention -> {
            LOG.info(s2 + convention.toString() + " (" + convention.getNote() + ")");
        });
    }

    public void addConventions(CodeConfiguration codeConfiguration, List<SourceFile> sourceFiles) {
        LOG.info("Adding ignore conventions:");
        ConventionUtils.addConventions(ignoredFilesConventions, codeConfiguration.getIgnore(), sourceFiles);
        LOG.info("Adding test files conventions:");
        ConventionUtils.addConventions(testFilesConventions, codeConfiguration.getTest().getSourceFileFilters(), sourceFiles);
        LOG.info("Adding generated files conventions:");
        ConventionUtils.addConventions(generatedFilesConventions, codeConfiguration.getGenerated().getSourceFileFilters(), sourceFiles);
        LOG.info("Adding build & deployment conventions:");
        ConventionUtils.addConventions(buildAndDeploymentFilesConventions, codeConfiguration.getBuildAndDeployment().getSourceFileFilters(), sourceFiles);
        LOG.info("Adding other files conventions:");
        ConventionUtils.addConventions(otherFilesConventions, codeConfiguration.getOther().getSourceFileFilters(), sourceFiles);
    }

    public List<Convention> getIgnoredFilesConventions() {
        return ignoredFilesConventions;
    }

    public void setIgnoredFilesConventions(List<Convention> ignoredFilesConventions) {
        this.ignoredFilesConventions = ignoredFilesConventions;
    }

    public List<Convention> getTestFilesConventions() {
        return testFilesConventions;
    }

    public void setTestFilesConventions(List<Convention> testFilesConventions) {
        this.testFilesConventions = testFilesConventions;
    }

    public List<Convention> getGeneratedFilesConventions() {
        return generatedFilesConventions;
    }

    public void setGeneratedFilesConventions(List<Convention> generatedFilesConventions) {
        this.generatedFilesConventions = generatedFilesConventions;
    }

    public List<Convention> getBuildAndDeploymentFilesConventions() {
        return buildAndDeploymentFilesConventions;
    }

    public void setBuildAndDeploymentFilesConventions(List<Convention> buildAndDeploymentFilesConventions) {
        this.buildAndDeploymentFilesConventions = buildAndDeploymentFilesConventions;
    }

    public List<Convention> getOtherFilesConventions() {
        return otherFilesConventions;
    }

    public void setOtherFilesConventions(List<Convention> otherFilesConventions) {
        this.otherFilesConventions = otherFilesConventions;
    }
}
