package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.junit.Test;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.InitializeNodesForTest;
import static org.junit.Assert.assertTrue;

public class AddAttributeTest {
    private AddAttribute attributeStep;

    @Test
    public void addAtributeOnNode() throws Exception {
        attributeStep = new AddAttribute("pepe", "lala");
        InitializeNodesForTest(attributeStep);
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

    @Test
    public void addAtributeOnNodeComplexValue() throws Exception {
        attributeStep = new AddAttribute("pepe", "#[payload().asString()]");
        InitializeNodesForTest(attributeStep);
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

    @Test
    public void addAtributeOnNodeAlreadyExistsOverwrites() throws Exception {
        attributeStep = new AddAttribute("pepe", "lala");
        InitializeNodesForTest(attributeStep);
        attributeStep.execute();
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

    @Test(expected = MigrationStepException.class)
    public void addNullAttribute() throws Exception {
        attributeStep = new AddAttribute(null, "lala");
        InitializeNodesForTest(attributeStep);
        attributeStep.execute();
    }

    @Test
    public void addEmptyAttribute() throws Exception {
        attributeStep = new AddAttribute("ignore", "");
        InitializeNodesForTest(attributeStep);
        attributeStep.execute();
        assertTrue(attributeStep.getNodes().get(0).hasAttributes() == true );
    }

}
