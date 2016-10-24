package com.mulesoft.munit.tools.migration.task.steps;

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
        for (Element node : getNodes()) {
            Attribute att = new Attribute(attributeName, attributeValue);
            node.setAttribute(att);
        }
    }
}
