package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;
import org.jdom2.Namespace;

public class ReplaceNodesQName extends MigrationStep {

    private String nodeNamespace;
    private String newNodeName;

    public ReplaceNodesQName(String nodeNamespace, String newNodeName) {
        this.nodeNamespace = nodeNamespace;
        this.newNodeName = newNodeName;
    }

    public void execute() throws Exception {
        if (getDocument() != null) {
            Namespace namespace = getDocument().getRootElement().getNamespace(nodeNamespace);
            if (namespace != null) {
                for (Element node : getNodes()) {
                    node.setNamespace(namespace);
                    node.setName(newNodeName);
                }
            }
        }
    }

}
