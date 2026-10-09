/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.landscape.analysis;

import nl.obren.sokrates.sourcecode.analysis.results.HistoryPerExtension;
import nl.obren.sokrates.sourcecode.githistory.CommitsPerExtension;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.utils.EmailTransformations;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;

import java.util.*;

class LandscapeExtensionAggregator {
    private final LandscapeAnalysisResults results;

    LandscapeExtensionAggregator(LandscapeAnalysisResults results) {
        this.results = results;
    }

    private LandscapeConfiguration configuration() {
        return results.getConfiguration();
    }

    private boolean ignoreExtension(String extension) {
        return configuration().getIgnoreExtensions().contains(extension);
    }

    List<HistoryPerExtension> getYearlyCommitHistoryPerExtension() {
        Map<String, HistoryPerExtension> map = new HashMap<>();
        results.getRepositoryAnalysisResults().forEach(repository -> {
            List<HistoryPerExtension> history = repository.getAnalysisResults().getFilesHistoryAnalysisResults().getHistoryPerExtensionPerYear();
            history.stream().filter(e -> !ignoreExtension(e.getExtension())).forEach(extensionYear -> {
                String extension = extensionYear.getExtension().toLowerCase();
                String key = extension + "::" + extensionYear.getYear();
                if (map.containsKey(key)) {
                    map.get(key).setCommitsCount(map.get(key).getCommitsCount() + extensionYear.getCommitsCount());
                    map.get(key).getContributors().addAll(extensionYear.getContributors());
                } else {
                    HistoryPerExtension newHistoryPerExtension = new HistoryPerExtension(extension,
                            extensionYear.getYear(), extensionYear.getCommitsCount());
                    newHistoryPerExtension.getContributors().addAll(extensionYear.getContributors());
                    map.put(key, newHistoryPerExtension);
                }
            });
        });
        return new ArrayList<>(map.values());
    }

    List<NumericMetric> getLinesOfCodePerExtension(LandscapeAnalysisResults.CodeCategory type) {
        List<NumericMetric> linesOfCodePerExtension = new ArrayList<>();
        results.getFilteredRepositoryAnalysisResults().forEach(repositoryAnalysisResults -> {
            String repositoryName = repositoryAnalysisResults.getAnalysisResults().getMetadata().getName();
            List<NumericMetric> repositoryLinesOfCodePerExtension;
            if (type == LandscapeAnalysisResults.CodeCategory.TEST) {
                repositoryLinesOfCodePerExtension = repositoryAnalysisResults.getAnalysisResults().getTestAspectAnalysisResults().getLinesOfCodePerExtension();
            } else if (type == LandscapeAnalysisResults.CodeCategory.OTHER) {
                List<NumericMetric> build = repositoryAnalysisResults.getAnalysisResults().getBuildAndDeployAspectAnalysisResults().getLinesOfCodePerExtension();
                List<NumericMetric> generated = repositoryAnalysisResults.getAnalysisResults().getGeneratedAspectAnalysisResults().getLinesOfCodePerExtension();
                List<NumericMetric> other = repositoryAnalysisResults.getAnalysisResults().getOtherAspectAnalysisResults().getLinesOfCodePerExtension();
                repositoryLinesOfCodePerExtension = merge(Arrays.asList(build, generated, other));
            } else {
                repositoryLinesOfCodePerExtension = repositoryAnalysisResults.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCodePerExtension();
            }
            repositoryLinesOfCodePerExtension.forEach(metric -> {
                String id = metric.getName().toLowerCase();
                Optional<NumericMetric> existingMetric = linesOfCodePerExtension.stream().filter(c -> c.getName().equalsIgnoreCase(id)).findAny();
                if (existingMetric.isPresent()) {
                    NumericMetric metricObject = existingMetric.get();
                    metricObject.getDescription().add(new NumericMetric(repositoryName, metric.getValue()));
                    metricObject.setValue(metricObject.getValue().intValue() + metric.getValue().intValue());
                } else {
                    NumericMetric metricObject = new NumericMetric(id, metric.getValue());
                    metricObject.getDescription().add(new NumericMetric(repositoryName, metric.getValue()));
                    linesOfCodePerExtension.add(metricObject);
                }
            });
        });

        Collections.sort(linesOfCodePerExtension, (a, b) -> b.getValue().intValue() - a.getValue().intValue());
        return linesOfCodePerExtension;
    }

    private List<NumericMetric> merge(List<List<NumericMetric>> metricLists) {
        List<NumericMetric> merged = new ArrayList<>();
        Map<String, NumericMetric> mergedMap = new HashMap<>();

        metricLists.forEach(list -> {
            list.forEach(metric -> {
                if (mergedMap.containsKey(metric.getName())) {
                    mergedMap.get(metric.getName()).setValue(mergedMap.get(metric.getName()).getValue().doubleValue() + metric.getValue().doubleValue());
                } else {
                    NumericMetric newMetric = new NumericMetric(metric.getName(), metric.getValue());
                    merged.add(newMetric);
                    mergedMap.put(newMetric.getName(), newMetric);
                }
            });
        });

        return merged;
    }

    List<String> getAllExtensions() {
        List<String> extensions = new ArrayList<>();
        List<String> ignoreExtensions = configuration().getIgnoreExtensions();
        results.getFilteredRepositoryAnalysisResults().forEach(repositoryAnalysisResults -> {
            repositoryAnalysisResults.getAnalysisResults().getContributorsAnalysisResults().getCommitsPerExtensions().forEach(perExtension -> {
                String extension = perExtension.getExtension();
                if (!ignoreExtensions.contains(extension) && !extensions.contains(extension)) {
                    extensions.add(extension);
                }
            });
        });

        return extensions;
    }

    List<CommitsPerExtension> getContributorsPerExtension() {
        Map<String, CommitsPerExtension> commitsPerExtensions = new HashMap<>();

        getAllExtensions().forEach(extension -> {
            commitsPerExtensions.put(extension, new CommitsPerExtension(extension));
        });

        results.getFilteredRepositoryAnalysisResults().forEach(repositoryAnalysisResults -> {
            List<CommitsPerExtension> repositoryData = repositoryAnalysisResults.getAnalysisResults().getContributorsAnalysisResults().getCommitsPerExtensions();

            repositoryData.forEach(repositoryExtData -> {
                String extension = repositoryExtData.getExtension();
                if (commitsPerExtensions.containsKey(extension)) {
                    addRepositoryExtensionData(commitsPerExtensions.get(extension), repositoryExtData);
                }
            });
        });


        configuration().getMergeExtensions().forEach(merge -> {
            CommitsPerExtension primary = commitsPerExtensions.get(merge.getPrimary());
            CommitsPerExtension secondary = commitsPerExtensions.get(merge.getSecondary());

            if (primary != null && secondary != null) {
                mergeExtensionData(primary, secondary);

                commitsPerExtensions.remove(merge.getSecondary());
            }
        });

        ArrayList<CommitsPerExtension> list = new ArrayList<>(commitsPerExtensions.values());
        Collections.sort(list, (a, b) -> b.getCommitters30Days().size() - a.getCommitters30Days().size());

        return list;
    }

    private void addRepositoryExtensionData(CommitsPerExtension commitsPerExtension, CommitsPerExtension repositoryExtData) {
        commitsPerExtension.setCommitsCount(commitsPerExtension.getCommitsCount() + repositoryExtData.getCommitsCount());

        commitsPerExtension.setCommitsCount30Days(commitsPerExtension.getCommitsCount30Days() + repositoryExtData.getCommitsCount30Days());
        commitsPerExtension.setCommitsCount90Days(commitsPerExtension.getCommitsCount90Days() + repositoryExtData.getCommitsCount90Days());

        commitsPerExtension.setFilesCount(commitsPerExtension.getFilesCount() + repositoryExtData.getFilesCount());
        commitsPerExtension.setFilesCount30Days(commitsPerExtension.getFilesCount30Days() + repositoryExtData.getFilesCount30Days());
        commitsPerExtension.setFilesCount90Days(commitsPerExtension.getFilesCount90Days() + repositoryExtData.getFilesCount90Days());

        repositoryExtData.getCommitters().forEach(email -> {
            String contributorId = transformEmail(email);
            if (!commitsPerExtension.getCommitters().contains(contributorId)) {
                commitsPerExtension.getCommitters().add(contributorId);
            }
        });
        repositoryExtData.getCommitters30Days().forEach(email -> {
            String contributorId = transformEmail(email);
            if (!commitsPerExtension.getCommitters30Days().contains(contributorId)) {
                commitsPerExtension.getCommitters30Days().add(contributorId);
            }
        });
        repositoryExtData.getCommitters90Days().forEach(email -> {
            String contributorId = transformEmail(email);
            if (!commitsPerExtension.getCommitters90Days().contains(contributorId)) {
                commitsPerExtension.getCommitters90Days().add(contributorId);
            }
        });
    }

    private String transformEmail(String email) {
        return EmailTransformations.transformEmail(email, configuration().getTransformContributorEmails(), results.getPeopleConfig());
    }

    private void mergeExtensionData(CommitsPerExtension primary, CommitsPerExtension secondary) {
        primary.setCommitsCount(primary.getCommitsCount() + secondary.getCommitsCount());
        primary.setCommitsCount30Days(primary.getCommitsCount30Days() + secondary.getCommitsCount30Days());
        primary.setCommitsCount90Days(primary.getCommitsCount90Days() + secondary.getCommitsCount90Days());
        primary.setFilesCount(primary.getFilesCount() + secondary.getFilesCount());
        primary.setFilesCount30Days(primary.getFilesCount30Days() + secondary.getFilesCount30Days());
        primary.setFilesCount90Days(primary.getFilesCount90Days() + secondary.getFilesCount90Days());
        secondary.getCommitters().stream()
                .filter(c -> !primary.getCommitters().contains(c))
                .forEach(commiter -> primary.getCommitters().add(commiter));
        secondary.getCommitters30Days().stream()
                .filter(c -> !primary.getCommitters30Days().contains(c))
                .forEach(commiter -> primary.getCommitters30Days().add(commiter));
        secondary.getCommitters90Days().stream()
                .filter(c -> !primary.getCommitters90Days().contains(c))
                .forEach(commiter -> primary.getCommitters90Days().add(commiter));
    }
}
