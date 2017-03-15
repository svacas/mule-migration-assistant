package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;
import org.junit.Test;

import java.util.Collections;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.GetNodesFromFile;
import static org.junit.Assert.*;

public class ReplaceStringOnNodeNameTest {
    private ReplaceStringOnNodeName replaceStringStep;

    @Test
    public void nodeNotContainStringToRemove() throws Exception {
        replaceStringStep = new ReplaceStringOnNodeName("when2", "pepe");
        GetNodesFromFile("//mock:when", replaceStringStep, "src/test/resources/sample-file.xml");
        replaceStringStep.execute();
        Element node = replaceStringStep.getNodes().get(0);
        assertFalse(node.getName().equals("pepe"));
    }

    @Test (expected = MigrationStepException.class)
    public void replaceToNullString() throws Exception {
        replaceStringStep = new ReplaceStringOnNodeName("when", null);
        GetNodesFromFile("//mock:when", replaceStringStep, "src/test/resources/sample-file.xml");
        replaceStringStep.execute();
    }

    @Test (expected = MigrationStepException.class)
    public void replaceToEmptyString() throws Exception {
        replaceStringStep = new ReplaceStringOnNodeName("when", "");
        GetNodesFromFile("//mock:when", replaceStringStep, "src/test/resources/sample-file.xml");
        replaceStringStep.execute();
    }

    @Test
    public void replaceSimpleString() throws Exception {
        replaceStringStep = new ReplaceStringOnNodeName("true", "that");
        GetNodesFromFile("//munit:assert-true", replaceStringStep, "src/test/resources/sample-file.xml");
        replaceStringStep.execute();
        Element node = replaceStringStep.getNodes().get(0);
        assertTrue(node.getName().equals("assert-that"));
    }

    @Test
    public void executeTaskOverEmptyNodeCollection() throws Exception {
        replaceStringStep = new ReplaceStringOnNodeName("true", "that");
        replaceStringStep.setNodes(Collections.<Element>emptyList());
        replaceStringStep.execute();
        assertEquals(Collections.<Element>emptyList(), replaceStringStep.getNodes());
    }

}
