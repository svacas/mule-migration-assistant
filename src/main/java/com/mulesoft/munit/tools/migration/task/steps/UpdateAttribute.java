package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Created by julianpascual on 10/24/16.
 */
public class UpdateAttribute extends MigrationStep {

    private String attributeName;
    private String newValue;

    public UpdateAttribute(String attributeName, String newValue) {
        this.attributeName = attributeName;
        this.newValue = newValue;
    }

    public void execute() throws Exception {
        for (Element node : getNodes()) {
            Attribute att = node.getAttribute(attributeName);
            if (att != null) {
                att.setValue(newValue);
            }
        }
    }
}
