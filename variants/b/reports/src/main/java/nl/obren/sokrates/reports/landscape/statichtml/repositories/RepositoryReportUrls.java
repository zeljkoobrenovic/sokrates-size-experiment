package nl.obren.sokrates.reports.landscape.statichtml.repositories;

import nl.obren.sokrates.reports.core.ReportConstants;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import org.apache.commons.lang3.StringUtils;

class RepositoryReportUrls {
    private LandscapeAnalysisResults landscapeAnalysisResults;

    RepositoryReportUrls(LandscapeAnalysisResults landscapeAnalysisResults) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
    }

    String getImageWithLink(RepositoryAnalysisResults repositoryAnalysis, String logoLink) {
        String prefix = landscapeAnalysisResults.getConfiguration().getRepositoryReportsUrlPrefix();
        return "<a href='" + this.getRepositoryReportUrl(repositoryAnalysis) + "' target='_blank'>" +
                (StringUtils.isNotBlank(logoLink)
                        ? ("<img src='" + getLogoLink(prefix + repositoryAnalysis.getSokratesRepositoryLink()
                        .getHtmlReportsRoot().replace("/index.html", ""), logoLink) + "' " +
                        "style='width: 20px' " +
                        "onerror=\"this.onerror=null;this.src='" + ReportConstants.SOKRATES_SVG_ICON_SMALL_BASE64 + "'\">")
                        : ReportConstants.SOKRATES_SVG_ICON_SMALL) +
                "</a>";
    }

    private String getLogoLink(String repositoryLinkPrefix, String link) {
        return link.startsWith("/") || link.contains("://") || link.startsWith("data:image")
                ? link
                : StringUtils.appendIfMissing(repositoryLinkPrefix, "/") + link;
    }

    String getRepositoryReportFolderUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return landscapeAnalysisResults.getConfiguration().getRepositoryReportsUrlPrefix() + repositoryAnalysis.getSokratesRepositoryLink().getHtmlReportsRoot() + "/";
    }

    String getRepositoryReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "index.html";
    }

    String getDuplicationReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "Duplication.html";
    }

    String getFileSizeReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "FileSize.html";
    }

    String getUnitSizeReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "UnitSize.html";
    }

    String getConditionalComplexityReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "ConditionalComplexity.html";
    }

    String getFileAgeReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "FileAge.html";
    }

    String getFileChangeFrequencyReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "FileChangeFrequency.html";
    }

    String getFeaturesReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "FeaturesOfInterest.html";
    }

    String getControlsReportUrl(RepositoryAnalysisResults repositoryAnalysis) {
        return getRepositoryReportFolderUrl(repositoryAnalysis) + "Controls.html";
    }
}
