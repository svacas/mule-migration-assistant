package com.mulesoft.tools.migration.task.steps;

import com.mulesoft.tools.migration.helpers.DocumentHelpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateChildNodeFromAttributeTest {
    private CreateChildNodeFromAttribute createAttNodeStep;

    @Test
    public void setAttributeNotExists() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("lala");
        DocumentHelpers.InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createWithEmptyAttributeValue() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("");
        DocumentHelpers.InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 0);
    }

    @Test
    public void createSimpleNodeFromAttribute() throws Exception {
        createAttNodeStep = new CreateChildNodeFromAttribute("prop");
        DocumentHelpers.InitializeNodesForTest(createAttNodeStep);
        createAttNodeStep.execute();
        assertTrue(createAttNodeStep.getNodes().get(0).getChildren().size() == 1);
    }

}
