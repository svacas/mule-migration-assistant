package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class CreateChildNodeFromAttribute extends MigrationStep {

    private String attribute;

    public CreateChildNodeFromAttribute(String attribute) {
        setAttribute(attribute);
    }

    public CreateChildNodeFromAttribute(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                Attribute att = node.getAttribute(getAttribute());
                if (att != null) {
                    Element child = new Element(getAttribute());
                    child.setNamespace(node.getNamespace());
                    Attribute newAtt = new Attribute("value", att.getValue());
                    child.setAttribute(newAtt);
                    node.addContent(child);
                    node.removeAttribute(att);

                    getReportingStrategy().log("Child node from attribute created:" + attribute);
                }
            }
        }catch (Exception ex) {
            throw new MigrationStepException("Create child node exception. " + ex.getMessage());
        }
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
