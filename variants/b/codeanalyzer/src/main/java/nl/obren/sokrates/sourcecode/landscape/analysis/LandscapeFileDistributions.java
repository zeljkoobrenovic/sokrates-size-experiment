/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.landscape.analysis;

import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;

import java.util.List;

class LandscapeFileDistributions {
    static SourceFileAgeDistribution getOverallFileLastModifiedDistribution(List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        SourceFileAgeDistribution distribution = new SourceFileAgeDistribution();
        repositoriesAnalysisResults.forEach(repositoryAnalysisResults -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getFilesHistoryAnalysisResults();
            SourceFileAgeDistribution repositoryDistribution = filesHistoryAnalysisResults.getOverallFileLastModifiedDistribution();
            if (repositoryDistribution == null) {
                return;
            }
            updateDistribution(distribution, repositoryDistribution);
        });
        return distribution;
    }

    static SourceFileAgeDistribution getOverallFileFirstModifiedDistribution(List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        SourceFileAgeDistribution distribution = new SourceFileAgeDistribution();
        repositoriesAnalysisResults.forEach(repositoryAnalysisResults -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getFilesHistoryAnalysisResults();
            SourceFileAgeDistribution repositoryDistribution = filesHistoryAnalysisResults.getOverallFileFirstModifiedDistribution();
            if (repositoryDistribution == null) {
                return;
            }
            updateDistribution(distribution, repositoryDistribution);
        });
        return distribution;
    }

    private static void updateDistribution(SourceFileAgeDistribution distribution, SourceFileAgeDistribution repositoryDistribution) {
        distribution.setNegligibleRiskLabel(repositoryDistribution.getNegligibleRiskLabel());
        distribution.setNegligibleRiskCount(distribution.getNegligibleRiskCount() + repositoryDistribution.getNegligibleRiskCount());
        distribution.setNegligibleRiskValue(distribution.getNegligibleRiskValue() + repositoryDistribution.getNegligibleRiskValue());

        distribution.setLowRiskLabel(repositoryDistribution.getLowRiskLabel());
        distribution.setLowRiskCount(distribution.getLowRiskCount() + repositoryDistribution.getLowRiskCount());
        distribution.setLowRiskValue(distribution.getLowRiskValue() + repositoryDistribution.getLowRiskValue());

        distribution.setMediumRiskLabel(repositoryDistribution.getMediumRiskLabel());
        distribution.setMediumRiskCount(distribution.getMediumRiskCount() + repositoryDistribution.getMediumRiskCount());
        distribution.setMediumRiskValue(distribution.getMediumRiskValue() + repositoryDistribution.getMediumRiskValue());

        distribution.setHighRiskLabel(repositoryDistribution.getHighRiskLabel());
        distribution.setHighRiskCount(distribution.getHighRiskCount() + repositoryDistribution.getHighRiskCount());
        distribution.setHighRiskValue(distribution.getHighRiskValue() + repositoryDistribution.getHighRiskValue());

        distribution.setVeryHighRiskLabel(repositoryDistribution.getVeryHighRiskLabel());
        distribution.setVeryHighRiskCount(distribution.getVeryHighRiskCount() + repositoryDistribution.getVeryHighRiskCount());
        distribution.setVeryHighRiskValue(distribution.getVeryHighRiskValue() + repositoryDistribution.getVeryHighRiskValue());
    }

    static int getLoc1YearActive(List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        int[] count = {0};
        repositoriesAnalysisResults.forEach(repositoryAnalysisResults -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getFilesHistoryAnalysisResults();
            SourceFileAgeDistribution overallFileLastModifiedDistribution = filesHistoryAnalysisResults.getOverallFileLastModifiedDistribution();
            if (overallFileLastModifiedDistribution != null) {
                count[0] += overallFileLastModifiedDistribution.getTotalValue() - overallFileLastModifiedDistribution.getVeryHighRiskValue();
            }
        });
        return count[0];
    }

    static int getLoc30DaysActive(List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        int[] count = {0};
        repositoriesAnalysisResults.forEach(repositoryAnalysisResults -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getFilesHistoryAnalysisResults();
            SourceFileAgeDistribution overallFileLastModifiedDistribution = filesHistoryAnalysisResults.getOverallFileLastModifiedDistribution();
            if (overallFileLastModifiedDistribution != null) {
                count[0] += overallFileLastModifiedDistribution.getNegligibleRiskValue();
            }
        });
        return count[0];
    }

    static int getLocNew(List<RepositoryAnalysisResults> repositoriesAnalysisResults) {
        int[] count = {0};
        repositoriesAnalysisResults.forEach(repositoryAnalysisResults -> {
            FilesHistoryAnalysisResults filesHistoryAnalysisResults = repositoryAnalysisResults.getAnalysisResults().getFilesHistoryAnalysisResults();
            SourceFileAgeDistribution overallFileFirstModifiedDistribution = filesHistoryAnalysisResults.getOverallFileFirstModifiedDistribution();
            if (overallFileFirstModifiedDistribution != null) {
                count[0] += overallFileFirstModifiedDistribution.getTotalValue() - overallFileFirstModifiedDistribution.getVeryHighRiskValue();
            }
        });
        return count[0];
    }
}
