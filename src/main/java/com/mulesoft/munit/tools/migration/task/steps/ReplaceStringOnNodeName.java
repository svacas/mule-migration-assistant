package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;

public class ReplaceStringOnNodeName extends MigrationStep{

    private String stringToReplace;
    private String newValue;

    public ReplaceStringOnNodeName(String stringToRemove, String newValue) {
        this.stringToReplace = stringToRemove;
        this.newValue = newValue;
    }

    public void execute() throws Exception {
        try {
            for (Element node : this.getNodes()) {
                if (node.getName().contains(stringToReplace)) {
                    node.setName(node.getName().replace(stringToReplace, newValue));
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Replace string on node step exception. " + ex.getMessage());
        }
    }
}
