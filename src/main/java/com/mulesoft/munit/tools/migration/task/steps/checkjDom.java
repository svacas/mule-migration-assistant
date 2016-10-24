package com.mulesoft.munit.tools.migration.task.steps;

import java.io.*;
import java.util.*;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.*;


public class checkjDom {
    private Document document;

    public Document getDocument() {
        return document;
    }

    private void setDocument(Document document) {
        this.document = document;
    }

    public checkjDom (String filePath) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File(filePath);
        setDocument(saxBuilder.build(file));
    }

    public void replaceNamespace(String XpathNodes, String newNamespace,  String newName)  {

        List<Element> nodes = getNodesFromXPath(XpathNodes);
        Namespace namespace = getDocument().getRootElement().getNamespace(newNamespace);

        for (Element node : nodes) {
            node.setNamespace(namespace);
            node.setName(newName);
        }

    }

    public void addNamespace(String newNamespace, String newNameSpaceUri, String schemaLocation) {

        Namespace nspc = Namespace.getNamespace(newNamespace , newNameSpaceUri);
        Element mule = getDocument().getRootElement();
        mule.addNamespaceDeclaration(nspc);
        Attribute muleSchemaLocation = mule.getAttributes().get(0);
        muleSchemaLocation.setValue(muleSchemaLocation.getValue() + " " + newNameSpaceUri + " " + schemaLocation + " ");
    }



    public void addAttribute(String XpathExpression, String attribute, String value) {

        List<Element> nodes = getNodesFromXPath(XpathExpression);

        for (Element node : nodes) {
            Attribute att = new Attribute(attribute,value);
            node.setAttribute(att);
        }
    }

    public void updateAttribute(String XpathExpression, String attribute, String value) {

        List<Element> nodes = getNodesFromXPath(XpathExpression);

        for (Element node : nodes) {
            Attribute att = node.getAttribute(attribute);
            att.setValue(value);
        }
    }

    public void attributeToNode(String attribute, String attributeNameForValue, String XpathParentNode, String namespace) {

        List<Element> nodes = getNodesFromXPath(XpathParentNode);

        for (Element node : nodes) {
            Attribute att = node.getAttribute(attribute);

            //TODO extraxt attribute and create subchild node with it

        }


    }

    public void moveAttributeToChildNode(String attribute, String XpathParentNode, String ChildNomeName) {

        List<Element> nodes = getNodesFromXPath(XpathParentNode);

        for (Element node : nodes) {
            Attribute att = node.getAttribute(attribute);


            //TODO extract attribut and move it to a different node

        }


    }


    public List<Element> getNodesFromXPath(String XpathExpression) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(XpathExpression, Filters.element(), null, getDocument().getRootElement().getAdditionalNamespaces());
        List<Element> nodes = xpath.evaluate(getDocument());
        return nodes;
    }

}
