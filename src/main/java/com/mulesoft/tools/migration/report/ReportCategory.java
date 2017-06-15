package com.mulesoft.tools.migration.report;

/**
 * Created by davidcisneros on 6/15/17.
 */
public enum ReportCategory {

    RULE_APPLIED("RULE APPLIED"),
    WORKING_WITH_NODES("WORKING WITH NODE"),
    ERROR("ERROR"),
    WORKING_WITH_FILE("WORKING WITH FILE");

    private final String description;

    ReportCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
