package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;

public class ReplaceStringOnNodeName extends MigrationStep{

    private String stringToReplace;
    private String newValue;

    public ReplaceStringOnNodeName(String stringToReplace, String newValue) {
        setStringToReplace(stringToReplace);
        setNewValue(newValue);
    }

    public ReplaceStringOnNodeName(){}

    public void execute() throws Exception {
        try {
            for (Element node : this.getNodes()) {
                if (node.getName().contains(getStringToReplace())) {
                    node.setName(node.getName().replace(getStringToReplace(), getNewValue()));

                    getReportingStrategy().log("Node string replaced:" + stringToReplace);
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Replace string on node step exception. " + ex.getMessage());
        }
    }

    public String getStringToReplace() {
        return stringToReplace;
    }

    public void setStringToReplace(String stringToReplace) {
        this.stringToReplace = stringToReplace;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
