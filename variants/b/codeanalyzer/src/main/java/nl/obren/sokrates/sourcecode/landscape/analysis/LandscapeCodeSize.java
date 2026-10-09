/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.landscape.analysis;

import java.util.List;

class LandscapeCodeSize {
    static int getMainLoc(List<RepositoryAnalysisResults> repositories) {
        int[] loc = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            loc[0] += repositoryAnalysisResults.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode();
        });
        return loc[0];
    }

    static int getMainFilesCount(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getMainAspectAnalysisResults().getFilesCount();
        });
        return count[0];
    }

    static int getTestLoc(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getTestAspectAnalysisResults().getLinesOfCode();
        });
        return count[0];
    }

    static int getGeneratedLoc(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getGeneratedAspectAnalysisResults().getLinesOfCode();
        });
        return count[0];
    }

    static int getBuildAndDeploymentLoc(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getBuildAndDeployAspectAnalysisResults().getLinesOfCode();
        });
        return count[0];
    }

    static int getOtherLoc(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getOtherAspectAnalysisResults().getLinesOfCode();
        });
        return count[0];
    }

    static int getTestFilesCount(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getTestAspectAnalysisResults().getFilesCount();
        });
        return count[0];
    }

    static int getGeneratedFilesCount(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getGeneratedAspectAnalysisResults().getFilesCount();
        });
        return count[0];
    }

    static int getBuildAndDeploymentFilesCount(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getBuildAndDeployAspectAnalysisResults().getFilesCount();
        });
        return count[0];
    }

    static int getOtherFilesCount(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getOtherAspectAnalysisResults().getFilesCount();
        });
        return count[0];
    }

    static int getAllLoc(List<RepositoryAnalysisResults> repositories) {
        int[] count = {0};
        repositories.forEach(repositoryAnalysisResults -> {
            count[0] += repositoryAnalysisResults.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode();
            count[0] += repositoryAnalysisResults.getAnalysisResults().getTestAspectAnalysisResults().getLinesOfCode();
            count[0] += repositoryAnalysisResults.getAnalysisResults().getGeneratedAspectAnalysisResults().getLinesOfCode();
            count[0] += repositoryAnalysisResults.getAnalysisResults().getBuildAndDeployAspectAnalysisResults().getLinesOfCode();
            count[0] += repositoryAnalysisResults.getAnalysisResults().getOtherAspectAnalysisResults().getLinesOfCode();
        });
        return count[0];
    }
}
