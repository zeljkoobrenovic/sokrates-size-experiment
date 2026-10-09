package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.reports.charts.SimpleOneBarChart;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;

import java.util.Arrays;
import java.util.List;

class RepositoryTableCells {
    static void addLangTableCell(RichTextReport report, AspectAnalysisResults main) {
        List<NumericMetric> linesOfCodePerExtension = main.getLinesOfCodePerExtension();
        StringBuilder locSummary = new StringBuilder();
        if (linesOfCodePerExtension.size() > 0) {
            locSummary.append(linesOfCodePerExtension.get(0).getName().replace("*.", "").trim().toUpperCase());
        } else {
            locSummary.append("-");
        }
        String lang = locSummary.toString().replace("> = ", ">");
        report.startTableCell("text-align: left; max-width: 38px;");
        report.startDiv("white-space: nowrap; overflow: hidden");
        report.addHtmlContent(DataImageUtils.getLangDataImageDiv36(lang));
        report.endDiv();
        report.endTableCell();
    }

    static String getRepositoryDisplayHtml(String name) {
        if (name.contains("/")) {
            int index = name.indexOf("/");
            String context = name.substring(0, index).trim();
            String remainder = name.substring(index + 1).trim();

            name = "<div style='font-size: 90%; color: grey'>" + context + "</div>" + "<div>" + remainder + "</div>";
        }
        return name;
    }

    static String getDuplicationVisual(boolean skipDuplication, Number duplicationPercentage) {
        if (skipDuplication) {
            return "<span style='color: grey; font-size: 70%'>not measured</span>";
        }
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(100);
        chart.setBarHeight(20);
        chart.setMaxBarWidth(100);
        chart.setBarStartXOffset(2);
        chart.setActiveColor("crimson");
        chart.setBackgroundColor("#9DC034");
        chart.setBackgroundStyle("");
        return chart.getPercentageSvg(duplicationPercentage.doubleValue(), "", "");
    }


    static String getRiskProfileVisual(RiskDistributionStats distributionStats, Palette palette) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(100);
        chart.setBarHeight(20);
        chart.setMaxBarWidth(100);
        chart.setBarStartXOffset(0);

        if (distributionStats != null) {
            List<Integer> values = Arrays.asList(
                    distributionStats.getVeryHighRiskValue(),
                    distributionStats.getHighRiskValue(),
                    distributionStats.getMediumRiskValue(),
                    distributionStats.getLowRiskValue(),
                    distributionStats.getNegligibleRiskValue());

            return chart.getStackedBarSvg(values, palette, "", "");
        } else {
            return "";
        }
    }
}
