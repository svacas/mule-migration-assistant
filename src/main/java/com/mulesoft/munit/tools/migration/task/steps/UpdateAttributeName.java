package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class UpdateAttributeName extends MigrationStep {

    private String attributeName;
    private String newValue;

    public UpdateAttributeName(String attributeName, String newValue) {
        this.attributeName = attributeName;
        this.newValue = newValue;
    }

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                Attribute att = node.getAttribute(attributeName);
                if (att != null) {
                    att.setName(newValue);
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Update attribute name exception. " + ex.getMessage());
        }
    }
}
