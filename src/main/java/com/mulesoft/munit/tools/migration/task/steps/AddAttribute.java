package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class AddAttribute extends MigrationStep {

    private String attributeName;
    private String attributeValue;

    public AddAttribute(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public void execute() throws Exception {
        try {
            for (Element node : this.getNodes()) {
                Attribute att = new Attribute(attributeName, attributeValue);
                node.setAttribute(att);
            }
        } catch (Exception ex) {
            throw new MigrationStepException(ex.getMessage());
        }
    }
}
