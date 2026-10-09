/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.data.LandscapeDataExport;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.*;
import nl.obren.sokrates.reports.landscape.utils.*;
import nl.obren.sokrates.reports.utils.PromptsUtils;
import nl.obren.sokrates.sourcecode.Link;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.*;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LandscapeReportGenerator {
    public static final String DEPENDENCIES_ICON = "\n" +
            "<svg height='100px' width='100px'  fill=\"#000000\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" x=\"0px\" y=\"0px\" viewBox=\"0 0 48 48\" enable-background=\"new 0 0 48 48\" xml:space=\"preserve\"><path d=\"M12,19.666v6.254l1.357-1.357c0.391-0.391,1.023-0.391,1.414,0s0.391,1.023,0,1.414l-3.064,3.064  c-0.195,0.195-0.451,0.293-0.707,0.293s-0.512-0.098-0.707-0.293l-3.064-3.064c-0.391-0.391-0.391-1.023,0-1.414  s1.023-0.391,1.414,0L10,25.92v-6.254c0-0.552,0.448-1,1-1S12,19.114,12,19.666z M28.334,36H22.08l1.357-1.357  c0.391-0.391,0.391-1.023,0-1.414s-1.023-0.391-1.414,0l-3.064,3.064c-0.391,0.391-0.391,1.023,0,1.414l3.064,3.064  c0.195,0.195,0.451,0.293,0.707,0.293s0.512-0.098,0.707-0.293c0.391-0.391,0.391-1.023,0-1.414L22.08,38h6.254c0.553,0,1-0.447,1-1  S28.887,36,28.334,36z M37,18.666c-0.553,0-1,0.448-1,1v6.254l-1.357-1.357c-0.391-0.391-1.023-0.391-1.414,0s-0.391,1.023,0,1.414  l3.064,3.064c0.195,0.195,0.451,0.293,0.707,0.293s0.512-0.098,0.707-0.293l3.064-3.064c0.391-0.391,0.391-1.023,0-1.414  s-1.023-0.391-1.414,0L38,25.92v-6.254C38,19.114,37.553,18.666,37,18.666z M31.58,16.421c-0.391-0.391-1.023-0.391-1.414,0  L18.127,28.458v-1.92c0-0.553-0.448-1-1-1s-1,0.447-1,1v4.334c0,0.13,0.027,0.26,0.077,0.382c0.101,0.245,0.296,0.439,0.541,0.541  c0.122,0.051,0.251,0.077,0.382,0.077h4.333c0.552,0,1-0.447,1-1s-0.448-1-1-1h-1.919L31.58,17.835  C31.971,17.444,31.971,16.812,31.58,16.421z M16.334,37c0,2.941-2.393,5.334-5.334,5.334S5.666,39.941,5.666,37  S8.059,31.666,11,31.666S16.334,34.059,16.334,37z M14.334,37c0-1.838-1.496-3.334-3.334-3.334S7.666,35.162,7.666,37  S9.162,40.334,11,40.334S14.334,38.838,14.334,37z M42.334,37c0,2.941-2.393,5.334-5.334,5.334S31.666,39.941,31.666,37  s2.393-5.334,5.334-5.334S42.334,34.059,42.334,37z M40.334,37c0-1.838-1.496-3.334-3.334-3.334S33.666,35.162,33.666,37  s1.496,3.334,3.334,3.334S40.334,38.838,40.334,37z M5.666,11c0-2.941,2.393-5.334,5.334-5.334S16.334,8.059,16.334,11  S13.941,16.334,11,16.334S5.666,13.941,5.666,11z M7.666,11c0,1.838,1.496,3.334,3.334,3.334s3.334-1.496,3.334-3.334  S12.838,7.666,11,7.666S7.666,9.162,7.666,11z M31.666,11c0-2.941,2.393-5.334,5.334-5.334S42.334,8.059,42.334,11  S39.941,16.334,37,16.334S31.666,13.941,31.666,11z M33.666,11c0,1.838,1.496,3.334,3.334,3.334s3.334-1.496,3.334-3.334  S38.838,7.666,37,7.666S33.666,9.162,33.666,11z\"></path></svg>";

    public static final int RECENT_THRESHOLD_DAYS = 30;
    public static final String OVERVIEW_TAB_ID = "overview";
    public static final String SUB_LANDSCAPES_TAB_ID = "sub-landscapes";
    public static final String REPOSITORIES_TAB_ID = "repositories";
    public static final String STATS_TAB_ID = "stats";

    public static final String TAGS_TAB_ID = "tags";
    public static final String CONTRIBUTORS_TAB_ID = "contributors";
    public static final String TOPOLOGIES_TAB_ID = "topologies";
    public static final String PROMPTS_TAB_ID = "prompts";
    public static final String TEAMS_TAB_ID = "teams";
    public static final String CUSTOM_TAB_ID_PREFIX = "custom_tab_";
    public static final String CONTRIBUTORS_30_D = "contributors_30d_";
    public static final String COMMITS_30_D = "commits_30d_";
    public static final String MAIN_LOC = "main_loc_";
    private static final Log LOG = LogFactory.getLog(LandscapeReportGenerator.class);
    public static final String DEVELOPER_SVG_ICON = "<svg width=\"16pt\" height=\"16pt\" version=\"1.1\" viewBox=\"0 0 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            " <g>\n" +
            "  <path d=\"m82 61.801-14-14c-2.1016-2.1016-4.8008-3.1992-7.8008-3.1992h-20.398c-2.8984 0-5.6992 1.1016-7.8008 3.1992l-14 14c-1.3008 1.3008-2 3.1016-2 4.8984 0 1.8984 0.69922 3.6016 2 5l3.8984 3.8984c1.3008 1.3008 3.1016 2.1016 5 2.1016 1.8984 0 3.6016-0.69922 4.8984-2.1016l1.6016-1.6016v10c0 3.8984 3.1016 7 7 7h19.102c3.8984 0 7-3.1016 7-7v-9.9961l1.6016 1.6016c1.3008 1.3008 3.1016 2.1016 5 2.1016 1.8984 0 3.6016-0.69922 4.8984-2.1016l3.8984-3.8984c2.8008-2.8047 2.8008-7.2031 0.10156-9.9023zm-4.3008 5.5977-3.8984 3.8984c-0.39844 0.39844-1 0.39844-1.3984 0l-6.6992-6.6992c-0.89844-0.89844-2.1016-1.1016-3.3008-0.69922-1.1016 0.5-1.8984 1.6016-1.8984 2.8008l-0.003906 17.301c0 0.60156-0.39844 1-1 1h-19c-0.60156 0-1-0.39844-1-1v-17.301c0-1.1992-0.69922-2.3008-1.8984-2.8008-0.39844-0.19922-0.80078-0.19922-1.1016-0.19922-0.80078 0-1.6016 0.30078-2.1016 0.89844l-6.6992 6.6992c-0.39844 0.39844-1 0.39844-1.3984 0l-3.8984-3.8984c-0.39844-0.39844-0.39844-1 0-1.3984l14-14c0.89844-0.89844 2.1992-1.5 3.5-1.5h20.5c1.3008 0 2.6016 0.5 3.5 1.5l14 14c0.19922 0.19922 0.30078 0.39844 0.30078 0.69922-0.003906 0.30078-0.30469 0.5-0.50391 0.69922z\"></path>\n" +
            "  <path d=\"m50 42.102c9.1016 0 16.5-7.3984 16.5-16.5 0-9.2031-7.3984-16.602-16.5-16.602s-16.5 7.3984-16.5 16.5c0 9.1992 7.3984 16.602 16.5 16.602zm0-27.102c5.8008 0 10.5 4.6992 10.5 10.5s-4.6992 10.602-10.5 10.602-10.5-4.6992-10.5-10.5c0-5.8008 4.6992-10.602 10.5-10.602z\"></path>\n" +
            " </g>\n" +
            "</svg>";
    public static final String TEAM_SVG_ICON = "<svg width=\"14pt\" height=\"14pt\" version=\"1.1\" viewBox=\"0 0 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            " <path d=\"m27.75 13.996c-0.33203-10.332-15.305-10.332-15.637 0 0.33594 10.324 15.293 10.328 15.637 0z\"/>\n" +
            " <path d=\"m28.844 21.113-0.48438-0.15625c-2.0703 2.5234-5.1641 3.9883-8.4297 3.9883-3.2656 0-6.3594-1.4648-8.4297-3.9883l-0.48438 0.15625c-2.7812 0.93359-4.6602 3.5391-4.6602 6.4727v2.7227c0.003906 1.6914 1.375 3.0625 3.0664 3.0625h21.016c1.6914 0 3.0625-1.3711 3.0664-3.0625v-2.7227c0-2.9336-1.8789-5.5391-4.6602-6.4727z\"/>\n" +
            " <path d=\"m27.75 74.141c-0.33203-10.332-15.305-10.332-15.637 0l-0.003906-0.003906c0.027344 4.3008 3.5195 7.7734 7.8203 7.7734 4.3008 0 7.793-3.4727 7.8203-7.7695z\"/>\n" +
            " <path d=\"m28.844 81.254-0.48438-0.15625c-2.082 2.5078-5.1719 3.9609-8.4297 3.9609-3.2578 0-6.3477-1.4531-8.4297-3.9609-3.0195 0.78516-5.1328 3.5078-5.1445 6.6289v2.7227c0.003906 1.6914 1.375 3.0625 3.0664 3.0625h21.016c1.6914 0 3.0625-1.3711 3.0664-3.0625v-2.7227c0-2.9336-1.8789-5.5391-4.6602-6.4727z\"/>\n" +
            " <path d=\"m72.25 74.137c0.027344 4.3008 3.5195 7.7734 7.8203 7.7734 4.3008 0 7.793-3.4727 7.8203-7.7695-0.33203-10.332-15.309-10.332-15.641-0.003906z\"/>\n" +
            " <path d=\"m88.984 81.254-0.48438-0.15625c-2.082 2.5078-5.1719 3.9609-8.4297 3.9609-3.2578 0-6.3477-1.4531-8.4297-3.9609-3.0195 0.78516-5.1328 3.5078-5.1445 6.6289v2.7227c0.003906 1.6914 1.375 3.0625 3.0664 3.0625h21.016c1.6914 0 3.0625-1.3711 3.0664-3.0625v-2.7227c0-2.9336-1.8789-5.5391-4.6602-6.4727z\"/>\n" +
            " <path d=\"m87.891 13.996c-0.33203-10.332-15.305-10.332-15.637 0 0.33594 10.324 15.293 10.328 15.637 0z\"/>\n" +
            " <path d=\"m88.984 21.113-0.48438-0.15625c-2.0703 2.5234-5.1641 3.9883-8.4297 3.9883-3.2656 0-6.3594-1.4648-8.4297-3.9883l-0.48438 0.15625c-2.7812 0.93359-4.6602 3.5391-4.6602 6.4727v2.7227c0.003906 1.6914 1.375 3.0625 3.0664 3.0625h21.016c1.6914 0 3.0625-1.3711 3.0664-3.0625v-2.7227c0-2.9336-1.8789-5.5391-4.6602-6.4727z\"/>\n" +
            " <path d=\"m16.973 37.285c-4.4219 7.8867-5.8906 17.094-4.1445 25.965 0.16797 0.84766 0.98828 1.3984 1.8359 1.2305 0.84766-0.16797 1.4023-0.98828 1.2344-1.8359-1.6055-8.1406-0.25781-16.586 3.7969-23.824 0.40625-0.75 0.13672-1.6875-0.60547-2.1055-0.74219-0.41797-1.6836-0.16406-2.1172 0.57031z\"/>\n" +
            " <path d=\"m61.293 88.742c-7.3203 2.5-15.266 2.5-22.586 0-0.80859-0.26563-1.6797 0.16797-1.9609 0.97266-0.27734 0.80469 0.13672 1.6836 0.93359 1.9805 7.9844 2.7383 16.656 2.7383 24.641 0 0.79688-0.29687 1.2109-1.1758 0.93359-1.9805-0.28125-0.80469-1.1523-1.2383-1.9609-0.97266z\"/>\n" +
            " <path d=\"m85.641 64.512c0.74609-0.003907 1.3867-0.53125 1.5312-1.2617 1.7461-8.8711 0.27734-18.078-4.1445-25.965-0.43359-0.73438-1.375-0.98828-2.1172-0.57031-0.74219 0.41797-1.0117 1.3555-0.60547 2.1055 4.0547 7.2383 5.4023 15.684 3.7969 23.824-0.085937 0.45703 0.035157 0.93359 0.33203 1.293 0.29688 0.36328 0.73828 0.57031 1.207 0.57422z\"/>\n" +
            " <path d=\"m63.82 20.586c-8.8867-3.4648-18.754-3.4648-27.641 0-0.78516 0.33203-1.1602 1.2266-0.84766 2.0195s1.2031 1.1875 2 0.88672c8.3516-3.1797 17.59-3.1406 25.914 0.11328 0.74219-0.015625 1.3711-0.54688 1.5117-1.2773 0.14063-0.72656-0.25-1.457-0.9375-1.7422z\"/>\n" +
            " <path d=\"m57.82 47.32c-0.32031-10.332-15.316-10.332-15.637 0 0.32422 10.328 15.305 10.332 15.637 0z\"/>\n" +
            " <path d=\"m58.914 54.438-0.48437-0.15625c-2.0859 2.5-5.1719 3.9453-8.4297 3.9453s-6.3438-1.4453-8.4297-3.9492c-3.0234 0.78516-5.1367 3.5078-5.1445 6.6328v2.707-0.003907c0 0.81641 0.32422 1.5938 0.89844 2.1719 0.57422 0.57422 1.3555 0.89453 2.168 0.89453h21.016c0.8125 0 1.5938-0.32031 2.168-0.89453 0.57812-0.57812 0.89844-1.3555 0.89844-2.1719v-2.7031c0.003906-2.9375-1.875-5.5469-4.6602-6.4727z\"/>\n" +
            "</svg>";
    public static final String OPEN_IN_NEW_TAB_SVG_ICON = "<svg width=\"14pt\" height=\"14pt\" version=\"1.1\" viewBox=\"0 0 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            " <path d=\"m87.5 16.918-35.289 35.289c-1.2266 1.1836-3.1719 1.168-4.3789-0.039062s-1.2227-3.1523-0.039062-4.3789l35.289-35.289h-23.707c-1.7266 0-3.125-1.3984-3.125-3.125s1.3984-3.125 3.125-3.125h31.25c0.82812 0 1.625 0.32812 2.2109 0.91406 0.58594 0.58594 0.91406 1.3828 0.91406 2.2109v31.25c0 1.7266-1.3984 3.125-3.125 3.125s-3.125-1.3984-3.125-3.125zm-56.25 1.832h-15.633c-5.1719 0-9.3672 4.1797-9.3672 9.3516v56.305c0 5.1562 4.2422 9.3516 9.3867 9.3516h56.219c2.4922 0 4.8828-0.98437 6.6406-2.7461 1.7617-1.7617 2.75-4.1523 2.7461-6.6445v-15.613 0.003906c0-1.7266-1.3984-3.125-3.125-3.125-1.7227 0-3.125 1.3984-3.125 3.125v15.613-0.003906c0.003906 0.83594-0.32422 1.6328-0.91406 2.2227s-1.3906 0.91797-2.2227 0.91797h-56.219c-1.7148-0.007812-3.1094-1.3867-3.1367-3.1016v-56.305c0-1.7148 1.3945-3.1016 3.1172-3.1016h15.633c1.7266 0 3.125-1.3984 3.125-3.125s-1.3984-3.125-3.125-3.125z\"/>\n" +
            "</svg>";
    public static final String OPEN_IN_NEW_TAB_SVG_ICON_SMALL = "<svg width=\"14pt\" height=\"10pt\" version=\"1.1\" viewBox=\"0 0 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            " <path d=\"m87.5 16.918-35.289 35.289c-1.2266 1.1836-3.1719 1.168-4.3789-0.039062s-1.2227-3.1523-0.039062-4.3789l35.289-35.289h-23.707c-1.7266 0-3.125-1.3984-3.125-3.125s1.3984-3.125 3.125-3.125h31.25c0.82812 0 1.625 0.32812 2.2109 0.91406 0.58594 0.58594 0.91406 1.3828 0.91406 2.2109v31.25c0 1.7266-1.3984 3.125-3.125 3.125s-3.125-1.3984-3.125-3.125zm-56.25 1.832h-15.633c-5.1719 0-9.3672 4.1797-9.3672 9.3516v56.305c0 5.1562 4.2422 9.3516 9.3867 9.3516h56.219c2.4922 0 4.8828-0.98437 6.6406-2.7461 1.7617-1.7617 2.75-4.1523 2.7461-6.6445v-15.613 0.003906c0-1.7266-1.3984-3.125-3.125-3.125-1.7227 0-3.125 1.3984-3.125 3.125v15.613-0.003906c0.003906 0.83594-0.32422 1.6328-0.91406 2.2227s-1.3906 0.91797-2.2227 0.91797h-56.219c-1.7148-0.007812-3.1094-1.3867-3.1367-3.1016v-56.305c0-1.7148 1.3945-3.1016 3.1172-3.1016h15.633c1.7266 0 3.125-1.3984 3.125-3.125s-1.3984-3.125-3.125-3.125z\"/>\n" +
            "</svg>";
    public static final String REPOSITORIES_COLOR = "#EADDCA";
    public static final String MAIN_LOC_FRESH_COLOR = "#E0FFFF";
    public static final String MAIN_LOC_COLOR = "#D6E4E1";
    public static final String TEST_LOC_COLOR = "#f0f0f0";
    public static final String PEOPLE_COLOR = "#ADD8E6";
    private final TagMap customTagsMap;
    private final TeamsConfig teamsConfig;
    private final LandscapeReportContributorsTab landscapeReportContributorsTab;
    private final LandscapeReportPeopleTopologyTab contributorsTopologyTab;
    private final LandscapeReportPeopleTopologyTab teamsTopologyTab;
    private final LandscapeReportContributorsTab landscapeReportTeamsTab;
    private RichTextReport landscapeReport = new RichTextReport("Landscape Report", "index.html");
    private RichTextReport landscapeRepositoriesReportShort = new RichTextReport("", "repositories-short.html");

    private RichTextReport landscapeRepositoriesTags = new RichTextReport("", "repositories-tags.html");
    private RichTextReport landscapeRepositoriesTagsMatrix = new RichTextReport("", "repositories-tags-matrix.html");

    private RichTextReport landscapeRepositoriesExtensionTags = new RichTextReport("", "repositories-extensions.html");
    private RichTextReport
            landscapeRepositoriesExtensionTagsMatrix = new RichTextReport("", "repositories-extensions-matrix.html");
    private RichTextReport landscapeRepositoriesReportLong = new RichTextReport("", "repositories.html");
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private List<TagGroup> tagGroups;
    private File folder;
    private File reportsFolder;
    private SourceFileAgeDistribution overallFileLastModifiedDistribution;
    private SourceFileAgeDistribution overallFileFirstModifiedDistribution;
    private final LandscapeReportChrome chrome;
    private final LandscapeReportInfoBlocks infoBlocks;
    private final LandscapeReportOverviewSection overviewSection;
    private final LandscapeReportStatisticsSection statisticsSection;
    private final LandscapeReportSubLandscapesSection subLandscapesSection;
    private final LandscapeReportExtensionsSection extensionsSection;
    private final LandscapeReportRepositoriesSection repositoriesSection;
    private final LandscapeReportTagsSection tagsSection;

    public LandscapeReportGenerator(LandscapeAnalysisResults analysisResults, List<TagGroup> tagGroups, File folder, File reportsFolder) {
        this.tagGroups = tagGroups;
        this.teamsConfig = analysisResults.getTeamsConfig();
        this.folder = folder;
        this.reportsFolder = reportsFolder;

        this.landscapeAnalysisResults = analysisResults;

        overallFileFirstModifiedDistribution = analysisResults.getOverallFileFirstModifiedDistribution();
        overallFileLastModifiedDistribution = analysisResults.getOverallFileLastModifiedDistribution();
        chrome = new LandscapeReportChrome(landscapeReport, analysisResults, teamsConfig);
        infoBlocks = new LandscapeReportInfoBlocks(landscapeReport);
        overviewSection = new LandscapeReportOverviewSection(landscapeReport, analysisResults, infoBlocks, overallFileLastModifiedDistribution);
        statisticsSection = new LandscapeReportStatisticsSection(landscapeReport, analysisResults, overallFileFirstModifiedDistribution, overallFileLastModifiedDistribution);
        subLandscapesSection = new LandscapeReportSubLandscapesSection(landscapeReport, analysisResults, folder, reportsFolder, chrome);

        landscapeReportContributorsTab = new LandscapeReportContributorsTab(analysisResults, analysisResults.getContributors(), landscapeReport, folder, reportsFolder, LandscapeReportContributorsTab.Type.CONTRIBUTORS, teamsConfig);
        landscapeReportTeamsTab = new LandscapeReportContributorsTab(analysisResults, analysisResults.getTeams(), landscapeReport, folder, reportsFolder, LandscapeReportContributorsTab.Type.TEAMS, teamsConfig);

        contributorsTopologyTab = new LandscapeReportPeopleTopologyTab(analysisResults, analysisResults.getContributors(), landscapeReport, folder, reportsFolder, LandscapeReportContributorsTab.Type.CONTRIBUTORS, teamsConfig);
        teamsTopologyTab = new LandscapeReportPeopleTopologyTab(analysisResults, analysisResults.getTeams(), landscapeReport, folder, reportsFolder, LandscapeReportContributorsTab.Type.TEAMS, teamsConfig);

        landscapeRepositoriesReportShort.setEmbedded(true);
        landscapeRepositoriesReportLong.setEmbedded(true);

        LOG.info("Exporting repositories...");
        List<RepositoryAnalysisResults> repositories = getRepositories();

        tagsSection = new LandscapeReportTagsSection(landscapeReport, analysisResults, reportsFolder, tagGroups, repositories,
                landscapeRepositoriesTags, landscapeRepositoriesTagsMatrix, landscapeRepositoriesExtensionTags, landscapeRepositoriesExtensionTagsMatrix);
        customTagsMap = tagsSection.getCustomTagsMap();
        extensionsSection = new LandscapeReportExtensionsSection(landscapeReport, analysisResults, reportsFolder, tagGroups, customTagsMap, infoBlocks, chrome);
        repositoriesSection = new LandscapeReportRepositoriesSection(landscapeReport, analysisResults, reportsFolder, tagGroups, customTagsMap, landscapeRepositoriesReportShort, landscapeRepositoriesReportLong);

        exportData(analysisResults, folder);

        chrome.addReportHead();
        chrome.addDescription();
        chrome.addLinks();

        landscapeReport.addLineBreak();

        chrome.addTabsLine();

        addTabs(repositories);

        String generationDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        landscapeReport.addContentInDiv("generated by <a target='_blank' href='https://sokrates.dev/'>sokrates.dev</a> " +
                        " (<a href='config.json' target='_blank'>configuration</a> | <a href='config-tags.json' target='_blank'>tag definitions</a> | <a href='config-teams.json' target='_blank'>team definitions</a>) " +
                        " on " + generationDate,
                "color: grey; font-size: 80%; margin: 10px");
        LOG.info("Done report generation.");
    }

    private void addTabs(List<RepositoryAnalysisResults> repositories) {
        addOverviewTab();
        addSublandscapesTab();
        addRepositoriesTab(repositories);
        addStatsTab();
        addRepositoryTab(repositories);
        addTagsTab(repositories);
        addPromptsTab();

        landscapeReportContributorsTab.addContributorsTabs(CONTRIBUTORS_TAB_ID);
        if (teamsConfig.getTeams().size() > 0) {
            landscapeReportTeamsTab.addContributorsTabs(TEAMS_TAB_ID);
        }

        addTopologyTag(TOPOLOGIES_TAB_ID);

        chrome.addCustomTabs();
    }

    void addTopologyTag(String tabId) {
        landscapeReport.startTabContentSection(tabId, false);

        ProcessingStopwatch.start("reporting/team topologies");
        LOG.info("Adding Contributor Dependencies...");

        if (teamsConfig.getTeams().size() > 0) {
            teamsTopologyTab.addPeopleInfoBlock();
        }
        contributorsTopologyTab.addPeopleInfoBlock();

        int repositoriesCount = landscapeAnalysisResults.getRepositoriesCount();
        infoBlocks.addRepositoriesInfoBlockWithColor(FormattingUtils.getSmallTextForNumber(repositoriesCount), repositoriesCount == 1 ? "repository" : "repositories", "", "", REPOSITORIES_COLOR);

        contributorsTopologyTab.render30DaysTopology();
        if (teamsConfig.getTeams().size() > 0) {
            teamsTopologyTab.render30DaysTopology();
        }
        contributorsTopologyTab.renderRepoAndKnowlegeTopologies();
        contributorsTopologyTab.renderDetails();

        if (teamsConfig.getTeams().size() > 0) {
            // set dummy report to render graphs, but not add section in the HTML report
            // teamsTopologyTab.setLandscapeReport(new RichTextReport("dummy report", ""));

            teamsTopologyTab.renderDetails();

            // teamsTopologyTab.setLandscapeReport(landscapeReport);
        }

        ProcessingStopwatch.end("reporting/team topologies");

        landscapeReport.endTabContentSection();
    }


    private void exportData(LandscapeAnalysisResults landscapeAnalysisResults, File folder) {
        LandscapeDataExport dataExport = new LandscapeDataExport(landscapeAnalysisResults, folder);
        dataExport.exportRepositories(customTagsMap);
        LOG.info("Exporting contributors...");
        dataExport.exportContributors();
        LOG.info("Exporting teams...");
        dataExport.exportTeams(teamsConfig);
        LOG.info("Exporting analysis results...");
        dataExport.exportAnalysisResults();
    }

    private void addTagsTab(List<RepositoryAnalysisResults> repositories) {
        landscapeReport.startTabContentSection(TAGS_TAB_ID, false);

        landscapeReport.addLineBreak();
        landscapeReport.startSubSection("<a href='repositories-extensions.html' target='_blank' style='text-decoration: none'>" +
                "File Extension Stats</a>&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "");
        landscapeReport.startDiv("margin-bottom: 18px;");
        landscapeReport.addNewTabLink("<b>Open expanded view</b> (stats per sub-folder)&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "repositories-extensions-matrix.html");
        landscapeReport.endDiv();
        landscapeReport.addHtmlContent("<iframe src='repositories-extensions.html' frameborder=0 style='height: 600px; width: 100%; margin-bottom: 0px; padding: 0;'></iframe>");
        landscapeReport.endSection();

        ProcessingStopwatch.start("reporting/tags");
        landscapeReport.addLineBreak();
        tagsSection.addTagsSection(repositories);


        ProcessingStopwatch.end("reporting/tags");
        landscapeReport.endTabContentSection();
    }

    private void addPromptsTab() {
        ProcessingStopwatch.start("reporting/prompts");

        landscapeReport.startTabContentSection(PROMPTS_TAB_ID, false);
        landscapeReport.startDiv("margin: 20px;");
        landscapeReport.addParagraph("Generative AI tools, like ChatGPT or Gemini, can help you explore and discuss various aspects of source code repositories using simple prompts and file uploads. Sokrates provides you with curated data that you can use to analyze your source code further.", "");

        PromptsUtils.addLandscapePromptSection("landscape-repository-insights", landscapeReport, landscapeAnalysisResults, "Prompt 1: Simple Repository Insights (based on repository names and basic stats)", "", Arrays.asList(new Link("repositories.txt", "data/repositories.txt"), new Link("repositories.json", "data/repositories.json")));

        PromptsUtils.addLandscapePromptSection("landscape-commits-analyzer", landscapeReport, landscapeAnalysisResults, "Prompt 2: Simple Commits & Contributor Insights", "", Arrays.asList(new Link("contributors.txt", "data/contributors.txt"), new Link("contributors.json", "data/contributors.json")));

        ProcessingStopwatch.end("reporting/prompts");

        landscapeReport.endDiv();
        landscapeReport.endTabContentSection();
    }

    private void addRepositoryTab(List<RepositoryAnalysisResults> repositories) {
        LOG.info("Adding repository section...");
        ProcessingStopwatch.start("reporting/repositories");
        addRepositoriesStatisticsSection(repositories);
        chrome.addIFrames(landscapeAnalysisResults.getConfiguration().getiFramesRepositories());
        ProcessingStopwatch.end("reporting/repositories");
        landscapeReport.endTabContentSection();
    }

    private void addStatsTab() {
        landscapeReport.startTabContentSection(STATS_TAB_ID, false);
        LOG.info("Adding stats...");
        overviewSection.addBigRepositoriesSummary(landscapeAnalysisResults);
        chrome.addIFrames(landscapeAnalysisResults.getConfiguration().getiFramesRepositoriesAtStart());
        if (!landscapeAnalysisResults.getConfiguration().isShowExtensionsOnFirstTab()) {
            LOG.info("Adding extensions...");
            extensionsSection.addExtensions();
        }
    }

    private void addRepositoriesTab(List<RepositoryAnalysisResults> repositories) {
        landscapeReport.startTabContentSection(REPOSITORIES_TAB_ID, false);
        LOG.info("Adding repository section...");
        ProcessingStopwatch.start("reporting/repositories");
        repositoriesSection.addRepositoriesSection(repositories);
        ProcessingStopwatch.end("reporting/repositories");
        landscapeReport.endTabContentSection();
    }

    private void addSublandscapesTab() {
        List<SubLandscapeLink> subLandscapes = landscapeAnalysisResults.getConfiguration().getSubLandscapes();
        if (subLandscapes.size() > 0) {
            landscapeReport.startTabContentSection(SUB_LANDSCAPES_TAB_ID, false);
            ProcessingStopwatch.start("reporting/sub-landscapes");
            LOG.info("Adding sub landscape section...");
            subLandscapesSection.addSubLandscapeSection(subLandscapes);
            WebFrameLink iframe = new WebFrameLink();
            iframe.setSrc("visuals/sub_landscapes_zoomable_circles_main_loc_.html");
            iframe.setMoreInfoLink("visuals/sub_landscapes_zoomable_circles_main_loc_.html");
            iframe.setTitle("Sub-Landscape repositories (by size)");
            iframe.setStyle("width: 100%; height: 970px;");
            iframe.setScrolling(false);
            chrome.addIFrame(iframe);
            ProcessingStopwatch.end("reporting/sub-landscapes");

            landscapeReport.startSubSection("Level 1 Sub-Landscape Dependencies", "");
            landscapeReport.startSubSection("Via Recent Contributors (30 days)", "");
            subLandscapesSection.renderSubLandscapeDependenciesViaContributors();
            landscapeReport.endSection();
            landscapeReport.startSubSection("Via Same Repository Names", "");
            subLandscapesSection.renderSubLandscapeDependenciesViaRepoName();
            landscapeReport.endSection();
            landscapeReport.endSection();

            landscapeReport.endTabContentSection();
        }
    }

    private void addOverviewTab() {
        ProcessingStopwatch.start("reporting/big summary");
        landscapeReport.startTabContentSection(OVERVIEW_TAB_ID, true);
        ProcessingStopwatch.start("reporting/overview");
        overviewSection.addBigSummary(landscapeAnalysisResults);
        if (landscapeAnalysisResults.getConfiguration().isShowExtensionsOnFirstTab()) {
            extensionsSection.addExtensions();
        }
        chrome.addIFrames(landscapeAnalysisResults.getConfiguration().getiFrames());
        ProcessingStopwatch.end("reporting/overview");
        landscapeReport.endTabContentSection();
        ProcessingStopwatch.end("reporting/big summary");
    }

    public static List<ContributionTimeSlot> getContributionDays(List<ContributionTimeSlot> contributorsPerDayOriginal, int pastDays, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerDay = new ArrayList<>(contributorsPerDayOriginal);
        List<String> slots = contributorsPerDay.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastDays(pastDays, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerDay.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerDay;
    }

    public static List<ContributionTimeSlot> getContributionWeeks(List<ContributionTimeSlot> contributorsPerWeekOriginal, int pastWeeks, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerWeek = new ArrayList<>(contributorsPerWeekOriginal);
        List<String> slots = contributorsPerWeek.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastWeeks(pastWeeks, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerWeek.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerWeek;
    }

    public static List<ContributionTimeSlot> getContributionYears(List<ContributionTimeSlot> contributorsPerWeekOriginal, int pastYears, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerWeek = new ArrayList<>(contributorsPerWeekOriginal);
        List<String> slots = contributorsPerWeek.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastYears(pastYears, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerWeek.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerWeek;
    }

    private List<RepositoryAnalysisResults> getRepositories() {
        return landscapeAnalysisResults.getFilteredRepositoryAnalysisResults();
    }

    private void addRepositoriesStatisticsSection(List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        Collections.sort(repositoryAnalysisResults, (a, b) -> b.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode() - a.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode());
        ProcessingStopwatch.end("reporting/repositories/preparing");

        ProcessingStopwatch.start("reporting/repositories/export visuals");
        repositoriesSection.exportZoomableCircles(CONTRIBUTORS_30_D, repositoryAnalysisResults, new ZommableCircleCountExtractors() {
            @Override
            public int getCount(RepositoryAnalysisResults repositoryAnalysisResults) {
                List<ContributionTimeSlot> contributorsPerMonth = repositoryAnalysisResults.getAnalysisResults().getContributorsAnalysisResults().getContributorsPerMonth();
                if (contributorsPerMonth.size() > 0) {
                    return contributorsPerMonth.get(0).getContributorsCount();
                }
                return 0;
            }
        });
        repositoriesSection.exportZoomableCircles(COMMITS_30_D, repositoryAnalysisResults, new ZommableCircleCountExtractors() {
            @Override
            public int getCount(RepositoryAnalysisResults repositoryAnalysisResults) {
                return repositoryAnalysisResults.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days();
            }
        });
        repositoriesSection.exportZoomableCircles(MAIN_LOC, repositoryAnalysisResults, new ZommableCircleCountExtractors() {
            @Override
            public int getCount(RepositoryAnalysisResults repositoryAnalysisResults) {
                return repositoryAnalysisResults.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode();
            }
        });
        ProcessingStopwatch.end("reporting/repositories/export visuals");

        ProcessingStopwatch.start("reporting/repositories/file age & freshness");
        statisticsSection.addFileAgeAndFreshnessSection();
        addZooSection();
        ProcessingStopwatch.end("reporting/repositories/file age & freshness");

        landscapeReport.startSubSection("Correlations", "");
        repositoriesSection.addCorrelations();
        landscapeReport.endSection();

    }

    public void addZooSection() {
        statisticsSection.addZooSection();
    }

    public List<RichTextReport> report() {
        List<RichTextReport> reports = new ArrayList<>();

        reports.add(this.landscapeReport);
        reports.add(this.landscapeRepositoriesReportShort);
        reports.add(this.landscapeRepositoriesTags);
        reports.add(this.landscapeRepositoriesExtensionTags);
        reports.add(this.landscapeRepositoriesTagsMatrix);
        reports.add(this.landscapeRepositoriesExtensionTagsMatrix);
        if (landscapeAnalysisResults.getRepositoryAnalysisResults().size() > landscapeAnalysisResults.getConfiguration().getRepositoriesShortListLimit()) {
            reports.add(this.landscapeRepositoriesReportLong);
        }
        reports.add(landscapeReportContributorsTab.getLandscapeContributorsReport());
        reports.add(landscapeReportContributorsTab.getLandscapeBotsReport());
        reports.add(landscapeReportContributorsTab.getLandscapeRecentContributorsReport());
        if (teamsConfig.getTeams().size() > 0) {
            reports.add(landscapeReportTeamsTab.getLandscapeContributorsReport());
            reports.add(landscapeReportTeamsTab.getLandscapeRecentContributorsReport());
        }

        return reports;
    }

    abstract class ZommableCircleCountExtractors {
        public abstract int getCount(RepositoryAnalysisResults repositoryAnalysisResults);
    }

    public List<RichTextReport> getIndividualContributorReports() {
        return landscapeReportContributorsTab.getIndividualReports();
    }

    public List<RichTextReport> getIndividualTeamReports() {
        return landscapeReportTeamsTab.getIndividualReports();
    }

    public List<RichTextReport> getIndividualBotReports() {
        return landscapeReportContributorsTab.getBotReports();
    }
}