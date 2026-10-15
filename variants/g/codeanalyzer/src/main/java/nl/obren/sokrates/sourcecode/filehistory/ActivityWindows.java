/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.filehistory;

/**
 * The fixed reporting windows for activity (commits, contributors, files changed together, people
 * dependencies): the past 30, 90, 180 and 365 days, counted back from the analysis date
 * ({@link DateUtils#getCalendar()}).
 * <p>
 * Words used for the windows in tasks and in the reports, and the window they mean:
 * <ul>
 *     <li>"recent", "recently active", "active now", "last month", "past 30 days", "_30d" → {@link #DAYS_30}
 *     (also {@code LandscapeAnalysisResults.RECENT_THRESHOLD_DAYS},
 *     {@code LandscapeReportGenerator.RECENT_THRESHOLD_DAYS} and
 *     {@code Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS}, which alias it);</li>
 *     <li>"last quarter", "3 months", "past 90 days" → {@link #DAYS_90};</li>
 *     <li>"active", "half a year", "6 months", "past 180 days" → {@link #DAYS_180}
 *     (also {@code Contributor.ACTIVITY_THRESHOLD_DAYS});</li>
 *     <li>"last year", "12 months", "past 365 days", "rookie" (first commit in the past year) → {@link #DAYS_365}
 *     (also {@code Contributor.ROOKIE_THRESHOLD_DAYS}).</li>
 * </ul>
 * The per-window variants of a metric are named after the window: {@code ...30Days}, {@code ...90Days},
 * {@code ...180Days}, {@code ...365Days} (for example {@code LandscapeAnalysisResults.getCommitsCount30Days()},
 * {@code Contributor.getCommitsCount90Days()}).
 * <p>
 * These windows are fixed. They are not the configurable risk thresholds (file size, unit size, file age,
 * change frequency, ...), which live in {@link nl.obren.sokrates.sourcecode.threshold.Thresholds} and are set
 * per project in the analysis configuration.
 */
public final class ActivityWindows {
    /** The past 30 days: "recent", "recently active", "last month". */
    public static final int DAYS_30 = 30;
    /** The past 90 days: "last quarter", "3 months". */
    public static final int DAYS_90 = 90;
    /** The past 180 days: "active", "6 months". */
    public static final int DAYS_180 = 180;
    /** The past 365 days: "last year", "12 months"; the rookie window. */
    public static final int DAYS_365 = 365;

    /** Report label of the 30-day window. */
    public static final String PAST_30_DAYS = "past 30 days";
    /** Report label of the 90-day window. */
    public static final String PAST_90_DAYS = "past 90 days";
    /** Report label of the 180-day window. */
    public static final String PAST_180_DAYS = "past 180 days";
    /** Short report label of the 90-day window. */
    public static final String LABEL_3_MONTHS = "3 months";
    /** Short report label of the 180-day window. */
    public static final String LABEL_6_MONTHS = "6 months";
    /** Label of the animated (racing chart) history, which shows a sliding one-year window. */
    public static final String LABEL_12_MONTHS_WINDOW = "12 months window";

    /** File-name suffix of the visuals and data files of the 30-day window, e.g. {@code extension_dependencies_30d}. */
    public static final String SUFFIX_30D = "_30d";

    private ActivityWindows() {
    }

    /**
     * @return the report label of a window, e.g. {@code "past 30 days"} for {@code 30}
     */
    public static String pastDays(int days) {
        return "past " + days + " days";
    }
}
