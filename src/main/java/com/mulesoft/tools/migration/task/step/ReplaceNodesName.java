package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class ReplaceNodesName extends MigrationStep {

    private String nodeNamespace;
    private String newNodeName;

    public ReplaceNodesName(){}

    public ReplaceNodesName(String nodeNamespace, String newNodeName) {
        setNodeNamespace(nodeNamespace);
        setNewNodeName(newNodeName);
    }

    public void execute() throws Exception {
        try {
            if (getDocument() != null) {
                Namespace namespace = getDocument().getRootElement().getNamespace(getNodeNamespace());
                if (namespace != null) {
                    for (Element node : getNodes()) {
                        node.setNamespace(namespace);
                        node.setName(getNewNodeName());

                        getReportingStrategy().log("Node replaced:" + newNodeName);
                    }
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Replace node name exception. " + ex.getMessage());
        }
    }

    public String getNodeNamespace() {
        return nodeNamespace;
    }

    public void setNodeNamespace(String nodeNamespace) {
        this.nodeNamespace = nodeNamespace;
    }

    public String getNewNodeName() {
        return newNodeName;
    }

    public void setNewNodeName(String newNodeName) {
        this.newNodeName = newNodeName;
    }

}
