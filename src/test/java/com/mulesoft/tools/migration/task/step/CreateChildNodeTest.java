package com.mulesoft.tools.migration.task.step;

import org.junit.Test;

import static com.mulesoft.tools.migration.helper.DocumentHelper.getNodesFromFile;
import static org.junit.Assert.assertTrue;

public class CreateChildNodeTest {

    private CreateChildNode createChildNodeStep;

    private static final String EXAMPLE_FILE_PATH = "src/test/resources/munit/examples/simple.xml";

    @Test
    public void createNodeWithEmptyName() throws Exception {
        createChildNodeStep = new CreateChildNode("");
        getNodesFromFile("//munit:assert-true", createChildNodeStep, EXAMPLE_FILE_PATH);
        createChildNodeStep.execute();
        assertTrue(createChildNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createNodeFromName() throws Exception {
        createChildNodeStep = new CreateChildNode("newNode");
        getNodesFromFile("//munit:assert-true", createChildNodeStep, EXAMPLE_FILE_PATH);
        createChildNodeStep.execute();
        assertTrue(createChildNodeStep.getNodes().get(0).getChildren().size() == 1);
    }
}
