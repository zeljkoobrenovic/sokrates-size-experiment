/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters;

/**
 * The names of the data-export files and of the visual (explorer) files of a repository report, in one place.
 * <p>
 * Each name is used on two sides: the producer that writes the file ({@link DataExporter} for the data files,
 * {@code CommandLineInterface} for the visuals) and the report pages that link to it ({@code ReportFileExporter}
 * and the page generators). Use these constants and builders on both sides, so that a search for the file name
 * finds the producer and all consumers, and a rename cannot leave a dead link.
 * <p>
 * Folder layout under the reports folder: {@code data/} (JSON), {@code data/text/} (text dumps),
 * {@code data/zips/}, {@code data/extra_analysis/}, {@code history/} and {@code html/visuals/}. The report pages
 * live in {@code html/}, so they link to data files with {@code ../data/...} and to visuals with {@code visuals/...}.
 * <p>
 * Also named elsewhere, without these constants: {@code templates/Structure.html} (static HTML) and
 * {@code LandscapeAnalyzer} in codeanalyzer (reads {@code excluded_files_ignored_*.txt}; that module cannot see
 * this class).
 */
public final class ReportDataFiles {
    // folders
    /** The data folder under the reports folder (same value as {@link DataExporter#DATA_FOLDER_NAME}). */
    public static final String DATA_FOLDER = "data";
    /** {@code data/text}: text dumps. */
    public static final String TEXT_FOLDER = "text";
    /** {@code data/zips}: zipped exports. */
    public static final String ZIPS_FOLDER = "zips";
    /** {@code data/extra_analysis}: derived configuration files. */
    public static final String EXTRA_ANALYSIS_FOLDER = "extra_analysis";
    /** {@code html/visuals}: interactive visuals (zoomable circles, 3D views, graphs). */
    public static final String VISUALS_FOLDER = "visuals";

    // data/text
    /** Metric id and value per line. */
    public static final String METRICS_TXT = "metrics.txt";
    /** Control (goal) statuses. */
    public static final String CONTROLS_TXT = "controls.txt";
    /** Contributors with commit counts (all time, 30/90/180/365 days). */
    public static final String CONTRIBUTORS_TXT = "contributors.txt";
    /** All duplicated blocks. */
    public static final String DUPLICATES_TXT = "duplicates.txt";
    /** Duplicated units. */
    public static final String UNIT_DUPLICATES_TXT = "unit_duplicates.txt";
    /** All units (size, McCabe index, parameters). */
    public static final String UNITS_TXT = "units.txt";
    /** Main files with lines of code. */
    public static final String MAIN_FILES_TXT = "mainFiles.txt";
    /** Main files with their git history (age, change frequency, contributors). */
    public static final String MAIN_FILES_WITH_HISTORY_TXT = "mainFilesWithHistory.txt";
    /** Main files that have no git history. */
    public static final String MAIN_FILES_WITHOUT_HISTORY_TXT = "mainFilesWithoutHistory.txt";
    /** Files excluded because of their extension. */
    public static final String EXCLUDED_FILES_IGNORED_EXTENSIONS_TXT = "excluded_files_ignored_extensions.txt";
    /** Files excluded by the ignore rules. */
    public static final String EXCLUDED_FILES_IGNORED_RULES_TXT = "excluded_files_ignored_rules.txt";
    /** Temporal dependencies (files changed together), all time. */
    public static final String TEMPORAL_DEPENDENCIES_TXT = "temporal_dependencies.txt";
    /** Temporal dependencies between files in different folders, all time. */
    public static final String TEMPORAL_DEPENDENCIES_DIFFERENT_FOLDERS_TXT = "temporal_dependencies_different_folders.txt";
    /** Temporal dependencies, past 30 days. */
    public static final String TEMPORAL_DEPENDENCIES_30_DAYS_TXT = "temporal_dependencies_30_days.txt";
    /** Temporal dependencies between files in different folders, past 30 days. */
    public static final String TEMPORAL_DEPENDENCIES_DIFFERENT_FOLDERS_30_DAYS_TXT = "temporal_dependencies_different_folders_30_days.txt";
    /** Trend of all metrics. */
    public static final String METRICS_TREND_TXT = "metrics_trend.txt";
    /** Trend of lines of code per extension. */
    public static final String METRICS_TREND_LOC_PER_EXTENSION_TXT = "metrics_trend_loc_per_extension.txt";
    /** Trend of duplicated lines. */
    public static final String METRICS_TREND_LOC_DUPLICATION_TXT = "metrics_trend_loc_duplication.txt";
    /** Trend of lines of code per unit size category. */
    public static final String METRICS_TREND_UNIT_SIZE_LOC_TXT = "metrics_trend_unit_size_loc.txt";
    /** Trend of lines of code per conditional complexity category. */
    public static final String METRICS_TREND_CONDITIONAL_COMPLEXITY_LOC_TXT = "metrics_trend_conditional_complexity_loc.txt";
    /** Trend of lines of code per logical component. */
    public static final String METRICS_TREND_LOC_LOGICAL_DECOMPOSITIONS_TXT = "metrics_trend_loc_logical_decompositions.txt";
    /** Trend of lines of code per file size category. */
    public static final String METRICS_TREND_LOC_FILE_SIZE_TXT = "metrics_trend_loc_file_size.txt";

    // data
    /** The full analysis results (also inside the history zip). */
    public static final String ANALYSIS_RESULTS_JSON = "analysisResults.json";
    /** A copy of the Sokrates configuration (also inside the history zip). */
    public static final String CONFIG_JSON = "config.json";
    public static final String MAIN_FILES_JSON = "mainFiles.json";
    public static final String TEST_FILES_JSON = "testFiles.json";
    public static final String GENERATED_FILES_JSON = "generatedFiles.json";
    public static final String BUILD_AND_DEPLOYMENT_FILES_JSON = "buildAndDeploymentFiles.json";
    public static final String OTHER_FILES_JSON = "otherFiles.json";
    public static final String UNITS_JSON = "units.json";
    /** All files (paths and sizes), used by the prompt sections. */
    public static final String FILES_JSON = "files.json";
    public static final String DUPLICATES_JSON = "duplicates.json";
    public static final String LOGICAL_DECOMPOSITIONS_JSON = "logical_decompositions.json";
    public static final String DEPENDENCIES_JSON = "dependencies.json";
    public static final String CONTRIBUTORS_JSON = "contributors.json";
    public static final String CONCERNS_JSON = "concerns.json";

    // history
    /** The analysis snapshot ({@link #CONFIG_JSON} and {@link #ANALYSIS_RESULTS_JSON}) in the history folders. */
    public static final String ANALYSIS_RESULTS_ZIP = "analysisResults.zip";

    // data/zips
    /** The zipped {@code aspect_*.txt} file lists. */
    public static final String ALL_FILES_ZIP = "all_files.zip";
    /** The zipped {@code git-history.txt}. */
    public static final String GIT_HISTORY_ZIP = "git-history.zip";

    // data/extra_analysis
    public static final String CONFIG_ORIGINAL_JSON = "config_original.json";
    public static final String CONFIG_BY_FILE_CHANGE_FREQUENCY_JSON = "config_by_file_change_frequency.json";
    public static final String CONFIG_BY_FILE_AGE_JSON = "config_by_file_age.json";
    public static final String CONFIG_BY_FILE_FRESHNESS_JSON = "config_by_file_freshness.json";

    // html/visuals
    /** 3D view of all main files, colored by size. */
    public static final String FILES_3D_HTML = "files_3d.html";
    /** 3D view of all units, colored by size. */
    public static final String UNITS_3D_SIZE_HTML = "units_3d_size.html";
    /** 3D view of all units, colored by conditional complexity. */
    public static final String UNITS_3D_COMPLEXITY_HTML = "units_3d_complexity.html";

    // coloring types of the zoomable circles of main files, see zoomableCirclesMainColoring(...)
    /** File size. */
    public static final String COLORING_LOC = "loc";
    /** File age (days since first update). */
    public static final String COLORING_AGE = "age";
    /** File freshness (days since last update). */
    public static final String COLORING_FRESHNESS = "freshness";
    /** File change frequency (churn). */
    public static final String COLORING_UPDATE_FREQUENCY = "update_frequency";
    /** Number of contributors per file. */
    public static final String COLORING_CONTRIBUTORS_COUNT = "contributors_count";

    private ReportDataFiles() {
    }

    /** Link from a report page in {@code html/} to a file in {@code data/}. */
    public static String dataLink(String fileName) {
        return "../" + DATA_FOLDER + "/" + fileName;
    }

    /** Link from a report page in {@code html/} to a file in {@code data/text/}. */
    public static String textDataLink(String fileName) {
        return dataLink(TEXT_FOLDER + "/" + fileName);
    }

    /** Link from a report page in {@code html/} to a file in {@code data/zips/}. */
    public static String zipLink(String fileName) {
        return dataLink(ZIPS_FOLDER + "/" + fileName);
    }

    /** Link from a report page in {@code html/} to a file in {@code html/visuals/}. */
    public static String visualLink(String fileName) {
        return VISUALS_FOLDER + "/" + fileName;
    }

    /** {@code zoomable_circles_<nameSuffix>.html}, e.g. per scope ({@code main}, {@code test}, ...). */
    public static String zoomableCircles(String nameSuffix) {
        return "zoomable_circles_" + nameSuffix + ".html";
    }

    /** {@code zoomable_sunburst_<nameSuffix>.html}, e.g. per scope ({@code main}, {@code test}, ...). */
    public static String zoomableSunburst(String nameSuffix) {
        return "zoomable_sunburst_" + nameSuffix + ".html";
    }

    /** {@code zoomable_circles_main_<type>_coloring.html}: main files grouped by folder, colored by {@code type}. */
    public static String zoomableCirclesMainColoring(String type) {
        return "zoomable_circles_main_" + type + "_coloring.html";
    }

    /** {@code zoomable_circles_main_<type>_coloring_categories.html}: main files grouped by the {@code type} category. */
    public static String zoomableCirclesMainColoringCategories(String type) {
        return "zoomable_circles_main_" + type + "_coloring_categories.html";
    }

    /**
     * {@code zoomable_circles_commits_<daysAgo>_<nameSuffix>.html}, or {@code zoomable_circles_commits.html}
     * when {@code daysAgo} is 0 (all time).
     */
    public static String zoomableCirclesCommits(int daysAgo, String nameSuffix) {
        return zoomableCircles("commits" + commitsSuffix(daysAgo, nameSuffix));
    }

    /** The sunburst counterpart of {@link #zoomableCirclesCommits(int, String)}. */
    public static String zoomableSunburstCommits(int daysAgo, String nameSuffix) {
        return zoomableSunburst("commits" + commitsSuffix(daysAgo, nameSuffix));
    }

    /**
     * {@code zoomable_circles_contributors_<daysAgo>_<nameSuffix>.html}, or
     * {@code zoomable_circles_contributors_<nameSuffix>.html} when {@code daysAgo} is 0 (all time).
     */
    public static String zoomableCirclesContributors(int daysAgo, String nameSuffix) {
        return zoomableCircles("contributors" + contributorsSuffix(daysAgo, nameSuffix));
    }

    /** The sunburst counterpart of {@link #zoomableCirclesContributors(int, String)}. */
    public static String zoomableSunburstContributors(int daysAgo, String nameSuffix) {
        return zoomableSunburst("contributors" + contributorsSuffix(daysAgo, nameSuffix));
    }

    // note: the all-time commits file has no name suffix, while the all-time contributors file keeps it
    private static String commitsSuffix(int daysAgo, String nameSuffix) {
        return daysAgo > 0 ? "_" + daysAgo + "_" + nameSuffix : "";
    }

    private static String contributorsSuffix(int daysAgo, String nameSuffix) {
        return (daysAgo > 0 ? ("_" + daysAgo) : "") + ("_" + nameSuffix);
    }
}
