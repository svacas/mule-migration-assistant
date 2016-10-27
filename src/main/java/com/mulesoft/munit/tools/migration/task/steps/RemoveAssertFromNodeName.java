package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;

public class RemoveAssertFromNodeName extends MigrationStep{


    public void execute() throws Exception {
        for (Element node : this.getNodes()) {
            if (node.getName().contains("assert-")) {
                node.setName(node.getName().replace("assert-", ""));
            }
        }
    }
}
