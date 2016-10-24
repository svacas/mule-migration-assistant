package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.List;

/**
 * Created by julianpascual on 10/24/16.
 */
public class ReplaceNamespace extends MigrationStep {

    private String newNamespace;
    private String namespaceName;

    public ReplaceNamespace(String newNamespace, String namespaceName) {
        this.newNamespace = newNamespace;
        this.namespaceName = namespaceName;
    }

    public void execute() throws Exception {
        Namespace namespace = getDocument().getRootElement().getNamespace(newNamespace);
        for (Element node : getNodes()) {
            node.setNamespace(namespace);
            node.setName(namespaceName);
        }
    }
}
