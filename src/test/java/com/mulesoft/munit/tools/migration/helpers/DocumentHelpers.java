package com.mulesoft.munit.tools.migration.helpers;

import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DocumentHelpers {

    public static Document getDocument(String path) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File(path);
        Document document = saxBuilder.build(file);
        return document;
    }

    public static void restoreTestDocument(Document doc, String path) throws Exception {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(doc, new FileOutputStream(path));
    }

    public static List<Element> getElementsFromDocument(Document doc, String xPathExpression) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(xPathExpression, Filters.element(), null, doc.getRootElement().getAdditionalNamespaces());
        List<Element> nodes = xpath.evaluate(doc);
        return nodes;
    }

    public static void InitializeNodesForTest(MigrationStep step) {
        ArrayList<Element> nodes = new ArrayList<Element>();
        Element node = new Element("munitNode");
        nodes.add(node);
        step.setNodes(nodes);
    }

}
