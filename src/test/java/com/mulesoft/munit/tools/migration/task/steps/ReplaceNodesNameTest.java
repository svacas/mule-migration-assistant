package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Test;

import java.util.List;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.getDocument;
import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.getElementsFromDocument;
import static org.junit.Assert.*;

public class ReplaceNodesNameTest {
    private ReplaceNodesName replaceQName;

    @Test
    public void replaceQNameTestNodes() throws Exception {
        replaceQName = new ReplaceNodesName("munit", "test2");
        InitializeNodesForTest("//munit:test");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("test2", newName);
    }

    @Test
    public void replaceQNameSubChildNodes() throws Exception {
        replaceQName = new ReplaceNodesName("mock", "mock");
        InitializeNodesForTest("//mock:when");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("mock", newName);
    }



    private void InitializeNodesForTest(String Xpath) throws Exception{
        Document document = getDocument("src/test/resources/sample-file.xml");
        List<Element> nodes = getElementsFromDocument(document, Xpath);
        replaceQName.setDocument(document);
        replaceQName.setNodes(nodes);
    }



}