package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.w3c.dom.Attr;

public class SetNodeNamespace extends MigrationStep {

    private String newNamespace;
    private String newNameSpaceUri;
    private String schemaLocationUrl;

    public SetNodeNamespace(String newNamespace, String newNameSpaceUri, String schemaLocationUrl) {
        this.newNamespace = newNamespace;
        this.newNameSpaceUri = newNameSpaceUri;
        this.schemaLocationUrl = schemaLocationUrl;
    }

    public void execute() throws Exception {
        try {
            Namespace nspc = Namespace.getNamespace(newNamespace, newNameSpaceUri);
            if (nspc != null && getDocument() != null) {
                Element muleNode = getDocument().getRootElement();
                muleNode.addNamespaceDeclaration(nspc);
                Attribute muleSchemaLocation = muleNode.getAttributes().get(0);
                if (schemaLocationNotDefined(muleNode)) {
                    muleSchemaLocation.setValue(muleSchemaLocation.getValue() + " " + newNameSpaceUri + " " + schemaLocationUrl + " ");
                }
                if (this.getNodes() != null) {
                    for (Element node : this.getNodes()) {
                        node.setNamespace(nspc);
                    }
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException(ex.getMessage());
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
