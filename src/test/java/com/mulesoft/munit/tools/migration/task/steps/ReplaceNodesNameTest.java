package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;
import org.junit.Test;

import java.util.Collections;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.GetNodesFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReplaceNodesNameTest {
    private ReplaceNodesName replaceQName;

    @Test
    public void replaceQNameTestNodes() throws Exception {
        replaceQName = new ReplaceNodesName("munit", "test2");
        GetNodesFromFile("//munit:test", replaceQName, "src/test/resources/sample-file.xml");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("test2", newName);
    }

    @Test
    public void replaceQNameSubChildNodes() throws Exception {
        replaceQName = new ReplaceNodesName("mock", "mock");
        GetNodesFromFile("//mock:when", replaceQName, "src/test/resources/sample-file.xml");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("mock", newName);
    }

    @Test
    public void replaceQNameNotFoundNameSpace() throws Exception {
        replaceQName = new ReplaceNodesName("lalero", "test");
        GetNodesFromFile("//mock:when", replaceQName, "src/test/resources/sample-file.xml");
        replaceQName.execute();
        String newName = replaceQName.getNodes().get(0).getName();
        assertEquals("when", newName);
    }

    @Test
    public void replaceQNameEmptyNodes() throws Exception {
        replaceQName = new ReplaceNodesName("munit", "lala");
        GetNodesFromFile("//mock:when2423", replaceQName, "src/test/resources/sample-file.xml");
        replaceQName.execute();
        assertTrue(replaceQName.getNodes()== Collections.<Element>emptyList());
    }
}