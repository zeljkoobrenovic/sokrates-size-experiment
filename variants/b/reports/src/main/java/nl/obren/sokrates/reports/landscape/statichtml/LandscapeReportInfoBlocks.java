/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import org.apache.commons.lang3.StringUtils;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.PEOPLE_COLOR;

/**
 * The info block cards of the landscape report (big summary blocks, language cards, small blocks).
 */
class LandscapeReportInfoBlocks {
    private final RichTextReport landscapeReport;

    LandscapeReportInfoBlocks(RichTextReport landscapeReport) {
        this.landscapeReport = landscapeReport;
    }

    void addInfoBlock(String mainValue, String subtitle, String description, String tooltip, String color, String icon) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: grey; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color + "; opacity: 0.8", tooltip, icon);
    }

    void addFreshInfoBlock(String mainValue, String subtitle, String description, String tooltip, String color, String icon) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: grey; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, icon);
    }

    void addPeopleInfoBlock(String mainValue, String subtitle, String description, String tooltip) {
        addPeopleInfoBlockWithColor(mainValue, subtitle, description, tooltip, PEOPLE_COLOR);
    }

    private void addPeopleInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: #707070; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, "contributors");
    }

    private void addWorkloadInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: grey; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, "workload");
    }

    void addRepositoriesInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: grey; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, "repository");
    }

    private void addInfoBlockWithColor(String mainValue, String subtitle, String color, String tooltip, String icon) {
        String style = "border-radius: 12px;";

        style += "margin: 12px 12px 12px 0px;";
        style += "display: inline-block; width: 160px; height: 120px;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 36px;";
        style += "box-shadow: rgb(0 0 0 / 12%) 0px 1px 3px, rgb(0 0 0 / 24%) 0px 1px 2px;";

        landscapeReport.startDiv("display: inline-block; text-align: center", tooltip);
        landscapeReport.addContentInDiv(ReportFileExporter.getIconSvg(icon, 48), "margin-top: 18px; margin-bottom: -12px");
        landscapeReport.startDiv(style, tooltip);
        String specialColor = mainValue.equals("<b>0</b>") ? " color: grey;" : "";
        landscapeReport.addHtmlContent("<div style='font-size: 50px; margin-top: 20px;" + specialColor + "'>" + mainValue + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 15px;" + specialColor + "'>" + subtitle + "</div>");
        landscapeReport.endDiv();
        landscapeReport.endDiv();
    }

    void addLangInfoBlock(String value, String lang, String description) {
        String style = "border-radius: 8px; margin: 4px 4px 4px 0px; display: inline-block; " +
                "width: 80px; height: 114px;background-color: #dedede; " +
                "text-align: center; vertical-align: middle; margin-bottom: 16px;";

        landscapeReport.startDivWithLabel(description, style);

        landscapeReport.addContentInDiv("", "margin-top: 8px");
        landscapeReport.addHtmlContent(DataImageUtils.getLangDataImageDiv42(lang));
        landscapeReport.addHtmlContent("<div style='font-size: 24px; margin-top: 8px;'>" + value + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 13px'>" + lang + "</div>");
        landscapeReport.endDiv();
    }

    void addLangInfoBlockExtra(String value, String lang, String description, String extra) {
        String style = "border-radius: 8px; margin: 4px 4px 4px 0px; display: inline-block; " +
                "width: 80px; height: 114px;background-color: #dedede; " +
                "text-align: center; vertical-align: middle; margin-bottom: 16px;";

        landscapeReport.startDivWithLabel(description, style);

        landscapeReport.addContentInDiv("", "margin-top: 8px");
        landscapeReport.addHtmlContent(DataImageUtils.getLangDataImageDiv42(lang));
        landscapeReport.addHtmlContent("<div style='font-size: 24px; margin-top: 8px;'>" + value + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 13px'>" + lang + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #767676; font-size: 9px; margin-top: 1px;'>" + extra + "</div>");
        landscapeReport.endDiv();
    }

    void addSmallInfoBlock(String value, String subtitle, String color, String link) {
        String style = "border-radius: 8px;";

        style += "margin: 4px 4px 4px 0px;";
        style += "display: inline-block; width: 80px; height: 76px;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 16px;";

        landscapeReport.startDiv(style);
        if (StringUtils.isNotBlank(link)) {
            landscapeReport.startNewTabLink(link, "text-decoration: none");
        }
        landscapeReport.addHtmlContent("<div style='font-size: 24px; margin-top: 8px;'>" + value + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 13px'>" + subtitle + "</div>");
        if (StringUtils.isNotBlank(link)) {
            landscapeReport.endNewTabLink();
        }
        landscapeReport.endDiv();
    }
}
