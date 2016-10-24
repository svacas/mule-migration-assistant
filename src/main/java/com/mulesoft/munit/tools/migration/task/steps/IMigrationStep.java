package com.mulesoft.munit.tools.migration.task.steps;


import org.jdom2.Document;
import org.jdom2.Element;

public interface IMigrationStep {

    /**
     * <p>Method to execute the step on a particular nodes collection</p>
     *
     *
     * @throws Exception <p>Case the step fails</p>
     */
     void execute() throws Exception;
}
