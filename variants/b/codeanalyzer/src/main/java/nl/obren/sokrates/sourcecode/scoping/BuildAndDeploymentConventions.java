/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.scoping;

import java.util.List;

class BuildAndDeploymentConventions {
    static void addTo(List<Convention> conventions) {
        addPart1(conventions);
        addPart2(conventions);
    }

    private static void addPart1(List<Convention> conventions) {
        conventions.add(new Convention(".*/pom[.]xml", "", "Maven configuration"));
        conventions.add(new Convention(".*[.]nuspec", "", "NuSpec configuration"));
        conventions.add(new Convention(".*/build[.]xml", "", "Build configuration"));
        conventions.add(new Convention(".*/assembly[.]xml", "", "Maven assembly plugin configuration"));
        conventions.add(new Convention(".*/assembly/src[.]xml", "", "Maven assembly plugin configuration"));
        conventions.add(new Convention(".*[.]gradle", "", "Gradle configuration"));
        conventions.add(new Convention(".*[.][-]gradle[.]js", "", "Gradle configuration"));
        conventions.add(new Convention(".*[.]sh", "", "Scripts"));
        conventions.add(new Convention(".*[.]bat", "", "Scripts"));
        conventions.add(new Convention(".*/AndroidManifest[.]xml", "", "Scripts"));
        conventions.add(new Convention(".*/pnpm.*[.]json", "", "pnpm configuration"));
        conventions.add(new Convention(".*/pnpm.*[.]ya?ml", "", "pnpm configuration"));
        conventions.add(new Convention(".*/package[.]json", "", "npm configuration"));
        conventions.add(new Convention(".*/package[-]lock[.]json", "", "npm configuration"));
        conventions.add(new Convention(".*/glide[.]yml", "", "Glide configuration"));
        conventions.add(new Convention(".*/glide[.]yaml", "", "Glide configuration"));
        conventions.add(new Convention(".*/glide[.]lock", "", "Glide configuration"));
        conventions.add(new Convention(".*/docker[-]compose[.]yaml", "", "Docker configuration"));
        conventions.add(new Convention(".*/docker[-]compose[.]yml", "", "Docker configuration"));
        conventions.add(new Convention(".*[.]dockerfile", "", "Docker configuration"));
        conventions.add(new Convention(".*[.]mk", "", "Mk files"));
        conventions.add(new Convention(".*[.]cvsignore", "", "CVS configuration files"));
        conventions.add(new Convention(".*[.]git[a-z]+", "", "Git configuration files"));
        conventions.add(new Convention(".*([.]|/)webpack([.]|/).*", "", "Webpack configuration files"));
        conventions.add(new Convention(".*[.]csproj", "", "C# repository files"));
        conventions.add(new Convention(".*[.]vbproj", "", "VB repository files"));
        conventions.add(new Convention(".*/[.]gitignore", "", "Git ignore files"));
        conventions.add(new Convention(".*/[.]gitattributes", "", "Git attributes"));
        conventions.add(new Convention(".*/[.]gitconfig", "", "Git config"));
        conventions.add(new Convention(".*/[.]gitmodules", "", "Git modules"));
        conventions.add(new Convention(".*[.]manifest", "", "Manifest files"));
    }

    private static void addPart2(List<Convention> conventions) {
        conventions.add(new Convention(".*[.]mak", "", "Make files"));
        conventions.add(new Convention(".*[.]make", "", "Make files"));
        conventions.add(new Convention(".*[.]mk", "", "Make files"));
        conventions.add(new Convention(".*[.]mkfile", "", "Make files"));
        conventions.add(new Convention(".*[.]dotsettings", "", ".Net settings files"));
        conventions.add(new Convention(".*/jenkins/.*[.]groovy", "", "Jenkins files"));
        conventions.add(new Convention(".*/fastlane/.*[.]rb", "", "Fastlane files"));
        conventions.add(new Convention(".*[.]podspec", "", "Podspec files"));


        conventions.add(new Convention(".*/Jenkinsfile", "", "Jenkinsfile"));
        conventions.add(new Convention(".*/Jenkinsfile[.][a-zA-Z0-9]+", "", "Jenkinsfile"));
        conventions.add(new Convention(".*/Makefile", "", "Makefile"));

        conventions.add(new Convention(".*/buildscripts/*", "", "Build scripts"));
    }
}
