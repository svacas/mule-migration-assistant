package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class MoveAttributeToChildNode extends MigrationStep {

    private String attribute;
    private String childNode;

    public MoveAttributeToChildNode(String attribute, String childNode) {

        this.attribute = attribute;
        this.childNode = childNode;
    }

    public void execute() throws Exception {
        for (Element node : getNodes()) {
            Attribute att = node.getAttribute(attribute);
            if (att != null) {
                Element child =  node.getChild(childNode, node.getNamespace());
                if (child != null) {
                    node.removeAttribute(att);
                    child.setAttribute(att);
                }
            }
        }
    }
}
