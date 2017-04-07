package com.mulesoft.tools.migration.task.steps;

import com.mulesoft.tools.migration.helpers.DocumentHelpers;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CreateChildNodeTest {

    private CreateChildNode createChildNodeStep;

    @Test
    public void createNodeWithEmptyName() throws Exception {
        createChildNodeStep = new CreateChildNode("");
        DocumentHelpers.InitializeNodesForTest(createChildNodeStep);
        createChildNodeStep.execute();
        assertTrue(createChildNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createNodeFromName() throws Exception {
        createChildNodeStep = new CreateChildNode("newNode");
        DocumentHelpers.InitializeNodesForTest(createChildNodeStep);
        createChildNodeStep.execute();
        assertTrue(createChildNodeStep.getNodes().get(0).getChildren().size() == 1);
    }
}
