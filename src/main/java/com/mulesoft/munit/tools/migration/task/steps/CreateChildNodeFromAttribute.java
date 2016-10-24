package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * Created by julianpascual on 10/24/16.
 */
public class CreateChildNodeFromAttribute extends MigrationStep {

    private String attribute;

    public CreateChildNodeFromAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void execute() throws Exception {
        for (Element node : getNodes()) {
            Attribute att = node.getAttribute(attribute);
            //TODO extraxt attribute and create subchild node with it
        }
    }
}
