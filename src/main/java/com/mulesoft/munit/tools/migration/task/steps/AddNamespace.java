package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class AddNamespace extends MigrationStep {

    private String newNamespace;
    private String newNameSpaceUri;
    private String schemaLocationUrl;

    public AddNamespace(String newNamespace, String newNameSpaceUri, String schemaLocationUrl) {
        this.newNamespace = newNamespace;
        this.newNameSpaceUri = newNameSpaceUri;
        this.schemaLocationUrl = schemaLocationUrl;
    }

    public void execute() throws Exception {
        Namespace nspc = Namespace.getNamespace(newNamespace , newNameSpaceUri);
        Element mule = getDocument().getRootElement();
        mule.addNamespaceDeclaration(nspc);
        Attribute muleSchemaLocation = mule.getAttributes().get(0);
        muleSchemaLocation.setValue(muleSchemaLocation.getValue() + " " + newNameSpaceUri + " " + schemaLocationUrl + " ");
    }
}
