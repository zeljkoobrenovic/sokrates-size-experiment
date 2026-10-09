/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.SubLandscapeLink;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.WebFrameLink;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

/**
 * The page chrome of the landscape report: head (name, logo, breadcrumbs), description, links, the tabs line,
 * the custom tabs, iframes and graph download links.
 */
class LandscapeReportChrome {
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final TeamsConfig teamsConfig;

    LandscapeReportChrome(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, TeamsConfig teamsConfig) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.teamsConfig = teamsConfig;
    }

    void addReportHead() {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        Metadata metadata = configuration.getMetadata();
        String landscapeName = metadata.getName();
        if (StringUtils.isNotBlank(landscapeName)) {
            landscapeReport.setDisplayName(landscapeName);
        }
        landscapeReport.setParentUrl(configuration.getParentUrl());
        String logoLink = metadata.getLogoLink();
        if (StringUtils.isBlank(logoLink)) {
            logoLink = "https://zeljkoobrenovic.github.io/sokrates-media/icons/landscape.png";
        }
        landscapeReport.setLogoLink(logoLink);
        landscapeReport.setBreadcrumbs(configuration.getBreadcrumbs());
    }

    void addDescription() {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        Metadata metadata = configuration.getMetadata();
        String description = metadata.getDescription();
        String tooltip = metadata.getTooltip();
        if (StringUtils.isNotBlank(description)) {
            if (StringUtils.isBlank(tooltip)) {
                landscapeReport.addParagraph(description, "font-size: 90%; color: #787878; margin-top: 5px; margin-bottom: 12px;");
            }
            if (StringUtils.isNotBlank(tooltip)) {
                landscapeReport.addParagraphWithTooltip(description, tooltip, "font-size: 90%; color: #787878; margin-top: 8px; margin-bottom: 12px;");
            }
        }
    }

    void addLinks() {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        Metadata metadata = configuration.getMetadata();
        if (metadata.getLinks().size() > 0) {
            landscapeReport.startDiv("font-size: 80%; margin-top: 6px;");
            boolean first[] = {true};
            metadata.getLinks().forEach(link -> {
                if (!first[0]) {
                    landscapeReport.addHtmlContent(" | ");
                }
                landscapeReport.addNewTabLink(link.getLabel(), link.getHref());
                first[0] = false;
            });
            landscapeReport.endDiv();
        }
    }

    void addTabsLine() {
        int recentContributorsCount = landscapeAnalysisResults.getRecentContributorsCount(landscapeAnalysisResults.getContributors());
        int recentTeamsCount = landscapeAnalysisResults.getRecentContributorsCount(landscapeAnalysisResults.getTeams());
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        List<SubLandscapeLink> subLandscapes = configuration.getSubLandscapes();

        List<SubLandscapeLink> level1SubLandscapes = configuration.getSubLandscapes().stream().filter(l -> getPathDepth(l.getIndexFilePath()) == 1).collect(Collectors.toList());

        landscapeReport.startTabGroup();
        landscapeReport.addTab(OVERVIEW_TAB_ID, "Overview", true);
        if (subLandscapes.size() > 0) {
            landscapeReport.addTab(SUB_LANDSCAPES_TAB_ID, "Sub-Landscapes (" + (level1SubLandscapes.size() == 0 ? subLandscapes.size() : level1SubLandscapes.size()) + ")", false);
        }
        landscapeReport.addTab(REPOSITORIES_TAB_ID, "Repositories (" + landscapeAnalysisResults.getFilteredRepositoryAnalysisResults().size() + ")", false);
        landscapeReport.addTab(STATS_TAB_ID, "Statistics", false);

        landscapeReport.addTab(TAGS_TAB_ID, "Tech Stats", false);
        landscapeReport.addTab(CONTRIBUTORS_TAB_ID, "Contributors" + (recentContributorsCount > 0 ? " (" + recentContributorsCount + ")" + "" : ""), false);
        if (teamsConfig.getTeams().size() > 0) {
            landscapeReport.addTab(TEAMS_TAB_ID, "Teams" + (recentContributorsCount > 0 ? " (" + recentTeamsCount + ")" + "" : ""), false);
        }
        landscapeReport.addTab(TOPOLOGIES_TAB_ID, "Team Topology", false);
        configuration.getCustomTabs().forEach(tab -> {
            int index = configuration.getCustomTabs().indexOf(tab);
            landscapeReport.addTab(CUSTOM_TAB_ID_PREFIX + index, tab.getName(), false);
        });
        landscapeReport.addTab(PROMPTS_TAB_ID, "AI Prompts", false);
        landscapeReport.endTabGroup();
    }

    void addCustomTabs() {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        configuration.getCustomTabs().forEach(tab -> {
            int index = configuration.getCustomTabs().indexOf(tab);
            landscapeReport.startTabContentSection(CUSTOM_TAB_ID_PREFIX + index, false);
            landscapeReport.addLineBreak();
            addIFrames(tab.getiFrames());
            landscapeReport.endTabContentSection();
        });
    }

    static int getPathDepth(String path) {
        return path.replace("\\", "/")
                .replace("/_sokrates_landscape/index.html", "")
                .split("/").length;
    }

    void addIFrames(List<WebFrameLink> iframes) {
        if (iframes.size() > 0) {
            iframes.forEach(iframe -> {
                addIFrame(iframe);
            });
        }
    }

    void addIFrame(WebFrameLink iframe) {
        if (StringUtils.isNotBlank(iframe.getTitle())) {
            String title;
            if (StringUtils.isNotBlank(iframe.getMoreInfoLink())) {
                title = "<a href='" + iframe.getMoreInfoLink() + "' target='_blank' style='text-decoration: none'>" + iframe.getTitle() + "</a>";
                title += "&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON;
            } else {
                title = iframe.getTitle();
            }
            landscapeReport.startSubSection(title, "");
        }
        String style = StringUtils.defaultIfBlank(iframe.getStyle(), "width: 100%; height: 200px; border: 1px solid lightgrey;");
        landscapeReport.addHtmlContent("<iframe src='" + iframe.getSrc()
                + "' frameborder='0' style='" + style + "'"
                + (iframe.getScrolling() ? "" : " scrolling='no' ")
                + "></iframe>");
        if (StringUtils.isNotBlank(iframe.getTitle())) {
            landscapeReport.endSection();
        }
    }

    void addDownloadLinks(String graphId) {
        landscapeReport.startDiv("");
        landscapeReport.addHtmlContent("Download: ");
        landscapeReport.addNewTabLink("SVG", "visuals/" + graphId + ".svg");
        landscapeReport.addHtmlContent(" ");
        landscapeReport.addNewTabLink("DOT", "visuals/" + graphId + ".dot.txt");
        landscapeReport.addHtmlContent(" ");
        landscapeReport.addNewTabLink("(open online Graphviz editor)", "https://obren.io/tools/graphviz/");
        landscapeReport.endDiv();
    }
}
