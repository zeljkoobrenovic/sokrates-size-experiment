/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.reports.utils.HtmlTemplateUtils;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportFileExporter {
    private static String htmlReportsSubFolder = "html";

    public static void exportHtml(File folder, String subFolder, RichTextReport report, String customHeaderFragment) {
        htmlReportsSubFolder = subFolder;
        File htmlReportsFolder = getHtmlReportsFolder(folder);
        String reportFileName = getReportFileName(report);
        export(htmlReportsFolder, report, reportFileName, customHeaderFragment);
    }

    private static void export(File folder, RichTextReport report, String reportFileName, String customHeaderFragment) {
        File reportFile = new File(folder, reportFileName);
        try {
            PrintWriter out = new PrintWriter(reportFile);
            String titleText = extractTitle(report.getDisplayName());
            String reportsHtmlHeader = ReportConstants.REPORTS_HTML_HEADER.replace(
                    "<title></title>",
                    "<title>" + titleText + "</title>"
            );
            reportsHtmlHeader = reportsHtmlHeader.replace("<!-- CUSTOM HEADER FRAGMENT -->", customHeaderFragment);
            if (report.isEmbedded()) {
                reportsHtmlHeader = reportsHtmlHeader.replace(" ${margin-left}", "0");
                reportsHtmlHeader = reportsHtmlHeader.replace(" ${margin-right}", "0");
            } else {
                reportsHtmlHeader = reportsHtmlHeader.replace(" ${margin-left}", "5%");
                reportsHtmlHeader = reportsHtmlHeader.replace(" ${margin-right}", "5%");
            }
            reportsHtmlHeader = minimize(reportsHtmlHeader);
            out.println(reportsHtmlHeader + "\n<body><div id=\"report\">\n" + "\n");
            new ReportRenderer().render(report, getReportRenderingClient(out, folder));
            out.println("</div>\n</body>\n</html>");
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static String extractTitle(String displayName) {
        return displayName.replaceAll("<.*?>", " ").replaceAll("  ", " ").trim();
    }

    private static String minimize(String html) {
        html = StringUtils.replace(html, "  ", " ");
        html = StringUtils.replace(html, "\n\n", "\n");
        return html;
    }

    private static ReportRenderingClient getReportRenderingClient(PrintWriter out, File reportsFolder) {
        return new ReportRenderingClient() {
            @Override
            public void append(String text) {
                out.println(text);
            }

            @Override
            public File getVisualsExportFolder() {
                File visualsFolder = new File(reportsFolder, "visuals");
                visualsFolder.mkdirs();
                return visualsFolder;
            }
        };
    }

    private static String getReportFileName(RichTextReport report) {
        return report.getFileName();
    }

    public static void exportReportsIndexFile(File reportsFolder, CodeAnalysisResults analysisResults, File sokratesConfigFolder) {
        List<String[]> reportList = ReportIndexReportsList.getReportsList(analysisResults, sokratesConfigFolder);

        File htmlExportFolder = getHtmlReportsFolder(reportsFolder);

        Metadata metadata = analysisResults.getCodeConfiguration().getMetadata();
        String title = metadata.getName();
        RichTextReport indexReport = new RichTextReport(title, "", metadata.getLogoLink());
        if (StringUtils.isNotBlank(metadata.getDescription())) {
            indexReport.addContentInDiv(metadata.getDescription(), "white-space: nowrap; overflow: hidden; margin-left: 2px; margin-top: 0px; margin-bottom: 14px; color: grey; font-size: 90%");
        }

        ReportIndexOverviewTab.appendLinks(indexReport, analysisResults);

        boolean hasLinks = metadata.getLinks().size() > 0;
        indexReport.addContentInDiv("", "height; 10px; margin-top: " + (hasLinks ? 6 : 0) + "px; margin-bottom: 6px;");

        addTabGroup(indexReport);

        ReportIndexOverviewTab.addOverviewTab(indexReport, analysisResults);

        addQualityTab(indexReport, analysisResults, htmlExportFolder, reportList);

        addFilesTab(indexReport);

        ReportIndexActivityTab.addActivityTab(indexReport, analysisResults);

        indexReport.startTabContentSection("visuals", false);
        indexReport.startDiv("margin: 24px");
        ReportVisualsTab.addVisuals(indexReport, analysisResults, htmlExportFolder);
        indexReport.endDiv();
        indexReport.endTabContentSection();

        indexReport.startTabContentSection("data", false);
        indexReport.startDiv("margin: 24px");
        ReportDataTab.addData(indexReport, analysisResults);
        indexReport.endDiv();
        indexReport.endTabContentSection();

        indexReport.startTabContentSection("prompts", false);
        indexReport.startDiv("margin: 24px");
        ReportDataTab.addPrompts(indexReport, analysisResults);
        indexReport.endDiv();
        indexReport.endTabContentSection();

        addFooter(indexReport);
        export(htmlExportFolder, indexReport, "index.html", analysisResults.getCodeConfiguration().getAnalysis().getCustomHtmlReportHeaderFragment());
    }

    private static void addTabGroup(RichTextReport indexReport) {
        indexReport.startTabGroup();
        indexReport.addTab("overview", "Overview", true);
        indexReport.addTab("quality", "Analyses", false);
        indexReport.addTab("commits", "Activity", false);
        indexReport.addTab("files", "Files", false);
        indexReport.addTab("visuals", "Visuals", false);
        indexReport.addTab("data", "Data", false);
        indexReport.addTab("prompts", "AI Prompts", false);
        indexReport.endDiv();
    }

    private static void addQualityTab(RichTextReport indexReport, CodeAnalysisResults analysisResults, File htmlExportFolder, List<String[]> reportList) {
        indexReport.startTabContentSection("quality", false);
        indexReport.addLineBreak();
        indexReport.startDiv("margin: 10px");
        summarize(indexReport, analysisResults);
        indexReport.addLineBreak();
        indexReport.endDiv();
        indexReport.startDiv("margin: 24px");
        indexReport.addLevel2Header("All Analysis Reports");
        for (String[] report : reportList) {
            ReportIndexReportsList.addReportFragment(htmlExportFolder, indexReport, report);
        }
        indexReport.endDiv();

        indexReport.endTabContentSection();
    }

    private static void addFilesTab(RichTextReport indexReport) {
        indexReport.startTabContentSection("files", false);
        indexReport.addLineBreak();
        indexReport.addHtmlContent("<iframe src='../explorers/files-explorer.html' style='width: 100%; border: none; height: calc(100vh - 220px); overflow: hidden; margin-top: -12px'></iframe>");

        indexReport.endTabContentSection();
    }

    private static void addFooter(RichTextReport indexReport) {
        String dateOfUpdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String referenceDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getCalendar().getTime());
        indexReport.addParagraph("generated by <a target='_blank' href='https://sokrates.dev/'>sokrates.dev</a> " +
                        " (<a href='../data/config.json' target='_blank'>configuration</a>)" +
                        " on " + dateOfUpdate + (!referenceDate.equals(dateOfUpdate) ? "; reference date: " + referenceDate : ""),
                "color: grey; font-size: 80%; margin-left: 10px; margin-bottom: 30px");
    }

    public static String getDetailsIcon() {
        return getIconSvg("details", 22);
    }


    private static void summarize(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        new SummaryUtils().summarize(analysisResults, indexReport);
    }


    private static File getHtmlReportsFolder(File reportsFolder) {
        File htmlExportFolder = new File(reportsFolder, htmlReportsSubFolder);
        htmlExportFolder.mkdirs();
        return htmlExportFolder;
    }

    public static String getIconSvg(String icon) {
        return getIconSvg(icon, 80);
    }

    public static String getIconSvg(String icon, int size) {
        String svg = HtmlTemplateUtils.getResource("/icons/" + icon + ".svg");
        svg = svg.replaceAll("height='.*?'", "height='" + size + "px'");
        svg = svg.replaceAll("width='.*?'", "width='" + size + "px'");
        return svg;
    }
}
