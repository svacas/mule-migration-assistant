package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ReplaceNodesQNameTest {
    private ReplaceNodesQName replaceQName;

    @Test
    public void replaceQNameTestNodes() throws Exception {
        replaceQName = new ReplaceNodesQName("munit", "test2");
        InitializeNodesForTest("//munit:test");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("test2", newName);
    }

    @Test
    public void replaceQNameSubChildNodes() throws Exception {
        replaceQName = new ReplaceNodesQName("mock", "mock");
        InitializeNodesForTest("//mock:when");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("mock", newName);
    }



    private void InitializeNodesForTest(String Xpath) throws Exception{
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("src/test/resources/sample-file.xml");
        Document document = saxBuilder.build(file);
        XPathExpression<Element> xpath = XPathFactory.instance().compile(Xpath, Filters.element(), null, document.getRootElement().getAdditionalNamespaces());
        List<Element> nodes = xpath.evaluate(document);
        replaceQName.setDocument(document);
        replaceQName.setNodes(nodes);
    }



}