package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class MoveAttributeToChildNode extends MigrationStep {

    private String attribute;

    public MoveAttributeToChildNode(String attribute) {
        this.attribute = attribute;
    }

    public void execute() throws Exception {
        for (Element node : getNodes()) {
            Attribute att = node.getAttribute(attribute);
            if (att != null) {

            }
            //TODO extract attribut and move it to a different node
        }
    }
}
