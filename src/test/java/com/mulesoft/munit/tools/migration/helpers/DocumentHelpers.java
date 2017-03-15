package com.mulesoft.munit.tools.migration.helpers;

import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

    public static void InitializeNodesForTest(MigrationStep step) throws Exception {
        ArrayList<Element> nodes = new ArrayList<Element>();
        String exampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
                "      xmlns:munit=\"http://www.mulesoft.org/schema/mule/munit\"\n" +
                "      xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "      xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "            http://www.mulesoft.org/schema/mule/munit http://www.mulesoft.org/schema/mule/munit/current/mule-munit.xsd\">\n" +
                "    <munit:test name=\"annotatedComponent-Test-Flow\" description=\"Test\">\n" +
                "        <munit:assert-true message=\"this is sample\" prop=\"${lala}\" condition=\"#['the new payload'.equals(payload)]\"/>\n" +
                "    </munit:test>\n" +
                "</mule>";
        InputStream stream = new ByteArrayInputStream(exampleXML.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document anotherDocument = builder.build(stream);
        nodes.add(anotherDocument.getRootElement().getChildren("test", anotherDocument.getRootElement().getNamespace("munit")).get(0).getChild("assert-true", anotherDocument.getRootElement().getNamespace("munit")));
        step.setNodes(nodes);
    }

    public static void GetNodesFromFile(String Xpath, MigrationStep step, String filePath) throws Exception {
        Document document = getDocument(filePath);
        List<Element> nodes = getElementsFromDocument(document, Xpath);
        step.setDocument(document);
        step.setNodes(nodes);
    }
}
