package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.w3c.dom.Attr;

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
        if (nspc != null && getDocument() != null) {
            Element muleNode = getDocument().getRootElement();
            muleNode.addNamespaceDeclaration(nspc);
            Attribute muleSchemaLocation = muleNode.getAttributes().get(0);
            if (schemaLocationNotDefined(muleNode)) {
                muleSchemaLocation.setValue(muleSchemaLocation.getValue() + " " + newNameSpaceUri + " " + schemaLocationUrl + " ");
            }
        }
    }

    private boolean schemaLocationNotDefined(Element node) {
        Attribute att = node.getAttribute("schemaLocation", node.getNamespace("xsi"));
        if (att.getValue().contains(this.newNameSpaceUri) && att.getValue().contains(this.schemaLocationUrl)) {
            return false;
        } else {
            return true;
        }
    }
}
