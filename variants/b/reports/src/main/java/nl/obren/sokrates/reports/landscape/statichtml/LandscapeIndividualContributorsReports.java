package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.SystemUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.ContributorPerExtensionHelper;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.githistory.ContributorPerExtensionStats;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.PersonConfig;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nl.obren.sokrates.reports.core.ReportFileExporter.getDetailsIcon;

public class LandscapeIndividualContributorsReports {
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private final File reportsFolder;
    private List<RichTextReport> reports = new ArrayList<>();
    private final ContributorRepositoryActivityTables activityTables;

    public LandscapeIndividualContributorsReports(LandscapeAnalysisResults landscapeAnalysisResults, File reportsFolder) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.reportsFolder = reportsFolder;
        this.activityTables = new ContributorRepositoryActivityTables(landscapeAnalysisResults);
    }

    public static String getContributorIndividualReportFileName(String email) {
        return SystemUtils.getSafeFileName(email).toLowerCase() + ".html";
    }

    public List<RichTextReport> getIndividualReports(List<ContributorRepositories> contributors) {
        contributors.forEach(contributor -> {
            reports.add(getIndividualReport(contributor));
        });

        return reports;
    }

    private RichTextReport getIndividualReport(ContributorRepositories contributorRepositories) {
        Contributor contributor = contributorRepositories.getContributor();
        RichTextReport report = new RichTextReport(contributor.getEmail(), getContributorIndividualReportFileName(contributor.getEmail()));
        report.setRenderLogo(false);
        String breadcrumbsLabel = landscapeAnalysisResults.getConfiguration().getMetadata().getName() + " / Contributors";
        String breadcrumbsHtml = "<div style='opacity: 0.7; font-size: 13px; margin-bottom: 12px;'><a href='../index.html'>" + breadcrumbsLabel + "</a></div>";

        PeopleConfig peopleConfig = landscapeAnalysisResults.getPeopleConfig();
        PersonConfig personConfig = peopleConfig != null ? peopleConfig.getPersonByName(contributor.getEmail()) : null;

        String avatarHtml = getAvatarHtml(contributorRepositories, contributor, personConfig);

        report.setDisplayName(breadcrumbsHtml + avatarHtml + contributor.getEmail());

        report.startDiv("margin-top: 10px; margin-bottom: 22px;");
        addHeaderLinks(report, contributor, personConfig);
        addCommitsSummary(report, contributorRepositories, contributor);
        report.endDiv();

        addFileUpdatesPerExtension(report, contributorRepositories, peopleConfig);

        report.addLineBreak();

        addActivityTabs(report, contributorRepositories);

        return report;
    }

    private String getAvatarHtml(ContributorRepositories contributorRepositories, Contributor contributor, PersonConfig personConfig) {
        String avatarHtml = "";
        String avatarUrl;
        if (personConfig != null && StringUtils.isNotBlank(personConfig.getImage())) {
            avatarUrl = personConfig.getImage();
        } else {
            avatarUrl = LandscapeContributorsReport.getAvatarUrl(contributor.getEmail(), landscapeAnalysisResults.getConfiguration().getContributorAvatarLinkTemplate());
        }
        String defaultAvatar = contributorRepositories.getMembers().size() > 0 ? DataImageUtils.TEAM : DataImageUtils.DEVELOPER;
        if (avatarUrl != null) {
            avatarHtml = "<div style='vertical-align: middle; display: inline-block; width: 88px; margin-top: 2px;'>" +
                    "<img style='border-radius: 50%; height: 80px; width: 80px; margin-right: 10px;' src='" + avatarUrl + "' " +
                    "onerror=\"this.onerror=null;this.src='" + defaultAvatar + "';\">" +
                    "</div>";
        } else {
            avatarHtml = "<div style='vertical-align: middle; display: inline-block; width: 48px; margin-top: 2px;'>" +
                    "<img style='border-radius: 50%; height: 40px; width: 40px; margin-right: 10px;' src='" + defaultAvatar + "'>" +
                    "</div>";
        }
        return avatarHtml;
    }

    private void addHeaderLinks(RichTextReport report, Contributor contributor, PersonConfig personConfig) {
        String template = this.landscapeAnalysisResults.getConfiguration().getContributorLinkTemplate();
        if (StringUtils.isNotBlank(template)) {
            String link = LandscapeContributorsReport.getContributorUrlFromTemplate(contributor.getEmail(), template);
            report.addNewTabLink("More details...", link);
            report.setParentUrl(link);
            report.addLineBreak();
            report.addLineBreak();
        }
        if (personConfig != null && personConfig.getLinks().size() > 0) {
            report.startDiv("margin-bottom: 14px; margin-top: -18px; font-size: 90%");
            boolean first[] = {true};
            personConfig.getLinks().stream().filter(l -> StringUtils.isNotBlank(l.getHref())).forEach(link -> {
                if (!first[0]) {
                    report.addHtmlContent("&nbsp;|&nbsp;&nbsp;");
                }
                report.addNewTabLink(link.getLabel(), link.getHref());
                report.addHtmlContent(LandscapeReportGenerator.OPEN_IN_NEW_TAB_SVG_ICON_SMALL);
                first[0] = false;
            });
            report.endDiv();
        }
    }

    private void addCommitsSummary(RichTextReport report, ContributorRepositories contributorRepositories, Contributor contributor) {
        report.addContentInDiv("First commit date: <b>" + contributor.getFirstCommitDate() + "</b>");
        report.addContentInDiv("Latest commit date: <b>" + contributor.getLatestCommitDate() + "</b>");
        report.addContentInDiv("Repositories count: " +
                "<b>" + contributorRepositories.getRepositories().stream().filter(p -> p.getCommits30Days() > 0).count()
                + "</b><span style='color: lightgrey; font-size: 90%'> (30d)</span>&nbsp;&nbsp;&nbsp;"
                + "<b>" + contributorRepositories.getRepositories().stream().filter(p -> p.getCommits90Days() > 0).count()
                + "</b><span style='color: lightgrey; font-size: 90%'> (3m)</span>&nbsp;&nbsp;&nbsp;"
                + "<b>" + contributorRepositories.getRepositories().stream().filter(p -> p.getCommits180Days() > 0).count()
                + "</b><span style='color: lightgrey; font-size: 90%'> (6m)</span>&nbsp;&nbsp;&nbsp;"
                + "<b>" + contributorRepositories.getRepositories().stream().filter(p -> p.getCommits365Days() > 0).count()
                + "</b><span style='color: lightgrey; font-size: 90%'> (1y)</span>&nbsp;&nbsp;&nbsp;" +
                "<b>" + contributorRepositories.getRepositories().size() + "</b> <span style='color: lightgrey; font-size: 90%'> (all time)</span>");
        report.addContentInDiv("Commits count: <b>" + contributor.getCommitsCount30Days() + "</b> " +
                "<span style='color: lightgrey; font-size: 90%'>(30d)&nbsp;&nbsp;&nbsp;</span>" +
                "<b>" + contributor.getCommitsCount90Days() + "</b><span style='color: lightgrey; font-size: 90%'> (3m)&nbsp;&nbsp;&nbsp;</span>" +
                "<b>" + contributor.getCommitsCount180Days() + "</b><span style='color: lightgrey; font-size: 90%'> (6m)&nbsp;&nbsp;&nbsp;</span>" +
                "<b>" + contributor.getCommitsCount365Days() + "</b><span style='color: lightgrey; font-size: 90%'> (1y)&nbsp;&nbsp;&nbsp;</span>" +
                "<b>" + contributor.getCommitsCount() + "</b><span style='color: lightgrey; font-size: 90%'> (all time)</span>"
        );
    }

    private void addFileUpdatesPerExtension(RichTextReport report, ContributorRepositories contributorRepositories, PeopleConfig peopleConfig) {
        ContributorPerExtensionHelper helper = new ContributorPerExtensionHelper();

        List<Pair<String, ContributorPerExtensionStats>> extensionUpdates = helper.getContributorStatsPerExtension(landscapeAnalysisResults.getConfiguration(), contributorRepositories, peopleConfig);

        report.addContentInDiv("File updates per extension (90 days):");
        report.startTable();
        report.startTableRow();
        int max = helper.getContributorsPerExtensionStream(extensionUpdates).mapToInt(e -> e.getRight().getFileUpdates90Days()).max().orElse(1);

        helper.getContributorsPerExtensionStream(extensionUpdates).forEach(extensionUpdate -> {
            report.startTableCell("border: 0; text-align: center; vertical-align: bottom");
            int fileUpdates90Days = extensionUpdate.getRight().getFileUpdates90Days();
            report.addContentInDiv(FormattingUtils.getSmallTextForNumber(fileUpdates90Days), "text-align: center; font-size: 70%; color: lightgrey;");
            int height = (int) (64.0 * fileUpdates90Days / max) + 1;
            report.addContentInDiv("", "background-color: skyblue; width: 35px; height: " + height + "px;");
            report.endTableCell();
        });
        report.endTableRow();
        report.startTableRow();
        helper.getContributorsPerExtensionStream(extensionUpdates).forEach(extensionUpdate -> {
            report.addTableCell(DataImageUtils.getLangDataImageDiv30(extensionUpdate.getLeft()), "border: 0");
        });
        report.endTableRow();
        report.endTable();
    }

    private void addActivityTabs(RichTextReport report, ContributorRepositories contributorRepositories) {
        report.startTabGroup();
        report.addTab("year", "Repository Activity Per Year", true);
        report.addTab("month", "Per Month", false);
        report.addTab("week", "Per Week", false);
        List<ContributorRepositories> members = contributorRepositories.getMembers();
        if (members.size() > 0) {
            report.addTab("members", "Members (" + FormattingUtils.formatCount(members.size()) + ")", false);
        }
        report.endTabGroup();

        Collections.sort(contributorRepositories.getRepositories(), (a, b) -> 10000 * (b.getCommits30Days() - a.getCommits30Days()) +
                100 * (b.getCommits90Days() - a.getCommits90Days()) +
                (b.getCommitsCount() - a.getCommitsCount()));

        report.startTabContentSection("week", false);
        activityTables.addPerWeek(contributorRepositories, report);
        report.endTabContentSection();

        report.startTabContentSection("month", false);
        activityTables.addPerMonth(contributorRepositories, report);
        report.endTabContentSection();

        report.startTabContentSection("year", true);
        activityTables.addPerYear(contributorRepositories, report);
        report.endTabContentSection();

        if (members.size() > 0) {
            report.startTabContentSection("members", false);
            addMembers(members, report);
            report.endTabContentSection();
        }
    }

    private void addMembers(List<ContributorRepositories> members, RichTextReport report) {
        report.startTable();
        addMembersHeaderRows(report);

        final int[] index = {0};
        members.stream()
                .sorted((a, b) -> b.getContributor().getCommitsCount() - a.getContributor().getCommitsCount())
                .sorted((a, b) -> b.getContributor().getCommitsCount365Days() - a.getContributor().getCommitsCount365Days())
                .sorted((a, b) -> b.getContributor().getCommitsCount180Days() - a.getContributor().getCommitsCount180Days())
                .sorted((a, b) -> b.getContributor().getCommitsCount90Days() - a.getContributor().getCommitsCount90Days())
                .sorted((a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days())
                .forEach(member -> {
                    addMemberRow(report, index, member);
                });
        report.endTable();
    }

    private void addMembersHeaderRows(RichTextReport report) {
        report.startTableRow("");
        report.addTableCell("", "border: none; text-align: center");
        report.addTableCell("", "border: none; text-align: center");
        report.addTableCell("", "border: none; text-align: center");
        report.addMultiColumnTableCell("commits", 5, "border: none; text-align: center");
        report.addMultiColumnTableCell("period", 2, "border: none; text-align: center");
        report.endTableRow();
        report.startTableRow();
        report.addTableCell("", "border: none");
        report.addTableCell("", "border: none");
        report.addTableCell("", "border: none");
        report.addTableCell("30d", "border: none; text-align: center");
        report.addTableCell("3m", "border: none; text-align: center");
        report.addTableCell("6m", "border: none; text-align: center");
        report.addTableCell("1y", "border: none; text-align: center");
        report.addTableCell("all", "border: none; text-align: center");
        report.addTableCell("first", "border: none; text-align: center");
        report.addTableCell("last", "border: none; text-align: center");
        report.endTableRow();
    }

    private void addMemberRow(RichTextReport report, int[] index, ContributorRepositories member) {
        String email = member.getContributor().getEmail();
        String link = LandscapeContributorsReport.getContributorUrl(email).replace("contributors/", "");
        boolean reportExists = true; //new File(reportsFolder, link).exists();
        String color = member.getContributor().getCommitsCount90Days() > 0 ? "grey" : "lightgrey; opacity: 0.6;";
        report.startTableRow(member.getContributor().getCommitsCount30Days() > 0 ? "font-weight: bold;"
                : "color: " + color);

        report.addTableCell(++index[0] + ".&nbsp;", "border: none; text-align: right");
        report.startTableCell("text-align: left; max-width: 32px; border: none");
        report.startDiv("white-space: nowrap; overflow: hidden;");
        String mostCommittedLang = StringUtils.defaultString(new ContributorPerExtensionHelper().getBiggestExtension(landscapeAnalysisResults.getConfiguration(), member, landscapeAnalysisResults.getPeopleConfig()), "");
        report.addHtmlContent(DataImageUtils.getLangDataImageDiv28(mostCommittedLang));
        report.endDiv();
        report.endTableCell();
        if (reportExists) {
            report.addTableCell("<a target='_blank' href='" + link + "'>" + email + "</a>", "border: none");
        } else {
            report.addTableCell(email, "border: none");
        }
        report.addTableCell(member.getContributor().getCommitsCount30Days() + "", "border: none");
        report.addTableCell(member.getContributor().getCommitsCount90Days() + "", "border: none");
        report.addTableCell(member.getContributor().getCommitsCount180Days() + "", "border: none");
        report.addTableCell(member.getContributor().getCommitsCount365Days() + "", "border: none");
        report.addTableCell(member.getContributor().getCommitsCount() + "", "border: none");
        report.addTableCell(member.getContributor().getFirstCommitDate(), "border: none");
        report.addTableCell(member.getContributor().getLatestCommitDate(), "border: none");
        if (reportExists) {
            report.addTableCell("<a target='_blank' href='" + link + "'  title='volume details' style='vertical-align: top'>" + getDetailsIcon() + "</a>", "text-align: center; border: none");
        }

        report.endTableRow();
    }

}
