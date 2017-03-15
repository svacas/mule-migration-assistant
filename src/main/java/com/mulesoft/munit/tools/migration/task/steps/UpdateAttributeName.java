package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class UpdateAttributeName extends MigrationStep {

    private String attributeName;
    private String newValue;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public UpdateAttributeName(String attributeName, String newValue) {
        setAttributeName(attributeName);
        setNewValue(newValue);
    }

    public UpdateAttributeName() {}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                Attribute att = node.getAttribute(getAttributeName());
                if (att != null) {
                    att.setName(getNewValue());
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Update attribute name exception. " + ex.getMessage());
        }
    }

}
