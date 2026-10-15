/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.contributors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.obren.sokrates.sourcecode.filehistory.ActivityWindows;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * One contributor of a repository, with commit counts per activity window (30/90/180/365 days).
 * <p>
 * Synonyms not used in the code: contributor = author = committer = developer = person = engineer = user
 * (the identity is the commit e-mail, see {@code email} / {@code userName}).
 * <p>
 * Built by {@link GitContributorsUtil}. Changes together with the identity rules in {@code PeopleConfig} and
 * {@code EmailTransformations} (landscape), {@code GitHistoryUtils} and {@code ContributorsReportGenerator}.
 */
public class Contributor {
    /** "Recently active": an alias of {@link ActivityWindows#DAYS_30}. */
    public static final int RECENTLY_ACTIVITY_THRESHOLD_DAYS = ActivityWindows.DAYS_30;
    /** "Active": an alias of {@link ActivityWindows#DAYS_180}. */
    public static final int ACTIVITY_THRESHOLD_DAYS = ActivityWindows.DAYS_180;
    /** "Rookie" (first commit within the window): an alias of {@link ActivityWindows#DAYS_365}. */
    public static final int ROOKIE_THRESHOLD_DAYS = ActivityWindows.DAYS_365;
    private String email = "";
    private String userName = "";
    private int commitsCount = 0;
    private int commitsCount30Days = 0;
    private int commitsCount90Days = 0;
    private int commitsCount180Days = 0;
    private int commitsCount365Days = 0;
    private String firstCommitDate = "";
    private String latestCommitDate = "";
    private List<String> activeYears = new ArrayList<>();
    private List<String> commitDates = new ArrayList<>();

    private boolean bot = false;

    public Contributor() {
    }

    public Contributor(String email) {
        this.email = email;
    }

    @JsonIgnore
    public void addCommit(String date) {
        if (!commitDates.contains(date)) {
            commitDates.add(date);
        }
        firstCommitDate = DateUtils.earlierDate(firstCommitDate, date);
        latestCommitDate = DateUtils.laterDate(latestCommitDate, date);
        if (date.length() > 4) {
            String year = date.substring(0, 4);
            if (!activeYears.contains(year)) {
                activeYears.add(year);
            }
            Collections.sort(activeYears);

            if (DateUtils.isCommittedLessThanDaysAgo(date, ActivityWindows.DAYS_30)) {
                commitsCount30Days += 1;
            }
            if (DateUtils.isCommittedLessThanDaysAgo(date, ActivityWindows.DAYS_90)) {
                commitsCount90Days += 1;
            }
            if (DateUtils.isCommittedLessThanDaysAgo(date, ActivityWindows.DAYS_180)) {
                commitsCount180Days += 1;
            }
            if (DateUtils.isCommittedLessThanDaysAgo(date, ActivityWindows.DAYS_365)) {
                commitsCount365Days += 1;
            }
        }

        commitsCount += 1;
    }

    public boolean isActive() {
        return isActive(ACTIVITY_THRESHOLD_DAYS);
    }

    public boolean isActive(int rangeInDays) {
        return DateUtils.isDateWithinRange(latestCommitDate, rangeInDays);
    }

    public boolean isRookie() {
        return isRookie(ACTIVITY_THRESHOLD_DAYS);
    }

    @JsonIgnore
    public boolean isRookieAtDate(String date) {
        String elements[] = date.split("-");
        if (elements.length >= 3) {
            Calendar cal = DateUtils.getCalendar(date);
            cal.add(Calendar.YEAR, -1);

            String rookieStartDate = new SimpleDateFormat(DateUtils.DATE_FORMAT).format(cal.getTime());
            return firstCommitDate.compareTo(rookieStartDate) >= 0;
        }
        return false;
    }

    @JsonIgnore
    public boolean isRookie(int activityThreshold) {
        if (StringUtils.isBlank(firstCommitDate) || !isActive(activityThreshold)) {
            return false;
        }

        Calendar cal = DateUtils.getCalendar();
        cal.add(Calendar.DATE, -ROOKIE_THRESHOLD_DAYS);

        String thresholdDate = new SimpleDateFormat(DateUtils.DATE_FORMAT).format(cal.getTime());

        return firstCommitDate.compareTo(thresholdDate) > 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(int commitsCount) {
        this.commitsCount = commitsCount;
    }

    public String getFirstCommitDate() {
        return firstCommitDate;
    }

    public void setFirstCommitDate(String firstCommitDate) {
        this.firstCommitDate = firstCommitDate;
    }

    public String getLatestCommitDate() {
        return latestCommitDate;
    }

    public void setLatestCommitDate(String latestCommitDate) {
        this.latestCommitDate = latestCommitDate;
    }

    public List<String> getActiveYears() {
        return activeYears;
    }

    public void setActiveYears(List<String> activeYears) {
        this.activeYears = activeYears;
    }

    public int getCommitsCount30Days() {
        return commitsCount30Days;
    }

    public void setCommitsCount30Days(int commitsCount30Days) {
        this.commitsCount30Days = commitsCount30Days;
    }

    public int getCommitsCount90Days() {
        return commitsCount90Days;
    }

    public void setCommitsCount90Days(int commitsCount90Days) {
        this.commitsCount90Days = commitsCount90Days;
    }

    public int getCommitsCount180Days() {
        return commitsCount180Days;
    }

    public void setCommitsCount180Days(int commitsCount180Days) {
        this.commitsCount180Days = commitsCount180Days;
    }

    public int getCommitsCount365Days() {
        return commitsCount365Days;
    }

    public void setCommitsCount365Days(int commitsCount365Days) {
        this.commitsCount365Days = commitsCount365Days;
    }

    public List<String> getCommitDates() {
        return commitDates;
    }

    public void setCommitDates(List<String> commitDates) {
        this.commitDates = commitDates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Contributor)) {
            return false;
        }

        Contributor contributor = (Contributor) obj;

        return contributor.getEmail().equalsIgnoreCase(this.getEmail());
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }
}
