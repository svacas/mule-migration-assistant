package com.mulesoft.munit.tools.migration.task.steps;

import org.junit.Test;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.InitializeNodesForTest;
import static org.junit.Assert.*;

public class CreateChildNodeFromAttributeTest {
    private CreateChildNodeFromAttribute createAttNodeStep;

    @Test
    public void setAttributeNotExists() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("lala");
        InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createWithEmptyAttributeValue() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("");
        InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createSimpleNodeFromAttribute() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("prop");
        InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 1);
    }

}
