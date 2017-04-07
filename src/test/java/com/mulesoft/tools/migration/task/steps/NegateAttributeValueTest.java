package com.mulesoft.tools.migration.task.steps;

import org.jdom2.Element;
import org.junit.Test;

import java.util.List;

import static com.mulesoft.tools.migration.helpers.DocumentHelpers.InitializeNodesForTest;
import static org.junit.Assert.*;

public class NegateAttributeValueTest {
    private NegateAttributeValue negateAtt;

    @Test
    public void notFoundAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("lala");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
        List<Element> nodes = negateAtt.getNodes();
        assertTrue(nodes.get(0).getAttribute("lala") == null);
    }

    @Test
    public void nullAttribute() throws Exception {
        negateAtt = new NegateAttributeValue(null);
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
        assertTrue(negateAtt.getNodes() != null);
    }

    @Test
    public void negateSimpleAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("message");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
        List<Element> nodes = negateAtt.getNodes();
        assertEquals(nodes.get(0).getAttributeValue("message"), "#[not(this is sample)]");
    }

    @Test
    public void negateAttributeInsideQuotes() throws Exception {
        negateAtt = new NegateAttributeValue("condition");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
        List<Element> nodes = negateAtt.getNodes();
        assertEquals( "#[not('the new payload'.equals(payload))]", nodes.get(0).getAttributeValue("condition"));
    }

    @Test
    public void negatePlaceHolderAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("prop");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
        List<Element> nodes = negateAtt.getNodes();
        assertEquals("#[not(${lala})]", nodes.get(0).getAttributeValue("prop"));
    }
}
