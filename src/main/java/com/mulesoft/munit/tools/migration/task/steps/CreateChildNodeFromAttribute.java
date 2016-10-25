package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class CreateChildNodeFromAttribute extends MigrationStep {

    private String attribute;

    public CreateChildNodeFromAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void execute() throws Exception {
        for (Element node : getNodes()) {
            Attribute att = node.getAttribute(attribute);
            if (att != null) {
                Element child = new Element(attribute);
                child.setNamespace(node.getNamespace());
                Attribute newAtt = new Attribute("value", att.getValue());
                child.setAttribute(newAtt);
                node.addContent(child);
                node.removeAttribute(att);
            }
        }
    }
}
