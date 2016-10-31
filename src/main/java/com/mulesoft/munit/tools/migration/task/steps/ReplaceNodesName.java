package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class ReplaceNodesName extends MigrationStep {

    private String nodeNamespace;
    private String newNodeName;

    public ReplaceNodesName(String nodeNamespace, String newNodeName) {
        this.nodeNamespace = nodeNamespace;
        this.newNodeName = newNodeName;
    }

    public void execute() throws Exception {
        try {
            if (getDocument() != null) {
                Namespace namespace = getDocument().getRootElement().getNamespace(nodeNamespace);
                if (namespace != null) {
                    for (Element node : getNodes()) {
                        node.setNamespace(namespace);
                        node.setName(newNodeName);
                    }
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Replace node name exception. " + ex.getMessage());
        }
    }

}
