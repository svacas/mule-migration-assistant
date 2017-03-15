package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Element;
import org.junit.Test;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.GetNodesFromFile;
import static org.junit.Assert.*;

public class MoveAttributeToChildNodeTest {
    private MoveAttributeToChildNode moveAttStep;

    @Test
    public void moveToChildNode() throws Exception {
        moveAttStep = new MoveAttributeToChildNode("messageProcessor", "with-attributes");
        GetNodesFromFile("//mock:when", moveAttStep, "src/test/resources/sample-file.xml");
        moveAttStep.execute();
        Element node = moveAttStep.getNodes().get(0);
        assertNotNull(node.getChildren().get(0).getAttribute("messageProcessor"));
    }

    @Test
    public void moveAttributeToNotDefinedChildNode() throws Exception {
        moveAttStep = new MoveAttributeToChildNode("messageProcessor", "pepe");
        GetNodesFromFile("//mock:when", moveAttStep, "src/test/resources/sample-file.xml");
        moveAttStep.execute();
        Element node = moveAttStep.getNodes().get(0);
        assertNull(node.getChild("pepe"));
    }

    @Test
    public void moveAttributeNotExistsOnNode() throws Exception {
        moveAttStep = new MoveAttributeToChildNode("pepe", "with-attributes");
        GetNodesFromFile("//mock:when", moveAttStep, "src/test/resources/sample-file.xml");
        moveAttStep.execute();
        Element node = moveAttStep.getNodes().get(0);
        assertNull(node.getChildren().get(0).getAttribute("pepe"));
    }

}
