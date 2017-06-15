package com.mulesoft.tools.migration.report;

/**
 * Created by davidcisneros on 6/7/17.
 */
public interface ReportingStrategy {

    void log(String message, ReportCategory reportCategory);
}
