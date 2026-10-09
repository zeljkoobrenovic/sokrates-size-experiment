/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.charts.SimpleOneBarChart;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class SummaryVisuals {
    private static final int BAR_WIDTH = 260;
    private static final int BAR_HEIGHT = 20;

    static String getControlColor(String status) {
        String upperCaseStatus = status.toUpperCase();
        return upperCaseStatus.equals("OK")
                ? "darkgreen"
                : upperCaseStatus.equals("FAILED")
                ? "crimson"
                : (upperCaseStatus.startsWith("IGNORE") ? "grey" : "orange");
    }

    static String getVolumeVisual(List<NumericMetric> linesOfCodePerExtension, int totalLoc, int mainLoc, String text) {
        int barWidth = Math.min(BAR_WIDTH, (int) ((double) BAR_WIDTH * totalLoc / mainLoc));
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(barWidth);
        chart.setBarHeight(BAR_HEIGHT);
        chart.setMaxBarWidth(barWidth);
        chart.setBarStartXOffset(0);
        chart.setFontSize("small");

        List<Integer> values = linesOfCodePerExtension.stream().map(metric -> metric.getValue().intValue()).collect(Collectors.toList());
        Collections.sort(values);
        Collections.reverse(values);
        return chart.getStackedBarSvg(values, Palette.getDefaultPalette(), "", text);
    }

    static String getRiskProfileVisual(RiskDistributionStats distributionStats) {
        return getRiskProfileVisual(distributionStats, Palette.getRiskPalette());
    }

    static String getRiskProfileVisual(RiskDistributionStats distributionStats, Palette palette) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(BAR_WIDTH + 20);
        chart.setBarHeight(BAR_HEIGHT);
        chart.setMaxBarWidth(BAR_WIDTH);
        chart.setBarStartXOffset(0);

        List<Integer> values = Arrays.asList(
                distributionStats.getVeryHighRiskValue(),
                distributionStats.getHighRiskValue(),
                distributionStats.getMediumRiskValue(),
                distributionStats.getLowRiskValue(),
                distributionStats.getNegligibleRiskValue());

        return chart.getStackedBarSvg(values, palette, "", "");
    }

    static String getDuplicationVisual(Number duplicationPercentage) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(BAR_WIDTH + 20);
        chart.setBarHeight(BAR_HEIGHT);
        chart.setMaxBarWidth(BAR_WIDTH);
        chart.setBarStartXOffset(2);
        chart.setActiveColor("crimson");
        chart.setBackgroundColor("#9DC034");
        chart.setBackgroundStyle("");

        return chart.getPercentageSvg(duplicationPercentage.doubleValue(), "", "");
    }

    static String addDiffDiv(double value, double refValue) {
        double diff = value - refValue;
        String diffText = getDiffText(diff, refValue);
        StringBuilder html = new StringBuilder("<div style='margin-top: 24px; margin-top: 0; text-align: left; color: " +
                (diff == 0 ? "lightgrey" : (diff < 0 ? "#b9936c" : "#6b5b95")) + "'>");
        if (diff > 0) {
            html.append("+" + diffText + " ⬆");
        } else if (diff < 0) {
            html.append("" + diffText + " ⬇ ");
        } else {
            html.append("" + diffText + "");
        }

        html.append("</div>");

        return html.toString();
    }

    private static String getDiffText(double diff, double refValue) {
        String diffText;
        if (Math.abs(refValue) < 0.0000000000000000000001) {
            diffText = "";
        } else {
            double percentage = 100.0 * diff / refValue;
            diffText = diff + " (" + (percentage > 0 ? "+" : (percentage < 0 ? "-" : ""))
                    + FormattingUtils.getFormattedPercentage(Math.abs(percentage)) + "%)";
        }
        return diffText;
    }
}
