package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class AddAttributeTest {
    private AddAttribute attributeStep;

    @Test
    public void addAtributeOnNode() throws Exception {
        attributeStep = new AddAttribute("pepe", "lala");
        InitializeNodesForTest();
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

    @Test
    public void addAtributeOnNodeComplexValue() throws Exception {
        attributeStep = new AddAttribute("pepe", "#[payload().asString()]");
        InitializeNodesForTest();
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

    @Test
    public void addAtributeOnNodeAlreadyExistsOverwrites() throws Exception {
        attributeStep = new AddAttribute("pepe", "lala");
        InitializeNodesForTest();
        attributeStep.execute();
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).getAttributes().size() == 1 );
    }


    private void InitializeNodesForTest() {
        ArrayList<Element> nodes = new ArrayList<Element>();
        Element node = new Element("munitNode");
        nodes.add(node);
        attributeStep.setNodes(nodes);
    }

}