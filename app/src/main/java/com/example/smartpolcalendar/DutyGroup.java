package com.example.smartpolcalendar;

import java.io.Serializable;

public class DutyGroup implements Serializable {
    private final String groupName;
    private final String[] pattern;
    private final String baseDate;

    public DutyGroup(String groupName, String[] pattern, String baseDate) {
        this.groupName = groupName;
        this.pattern = pattern;
        this.baseDate = baseDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public String[] getPattern() {
        return pattern;
    }

    public String getBaseDate() {
        return baseDate;
    }
}