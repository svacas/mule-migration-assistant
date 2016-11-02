package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;
import org.junit.Test;

import java.util.Collections;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.GetNodesFromFile;
import static org.junit.Assert.*;

public class UpdateAttributeNameTest {
    private UpdateAttributeName updateAttributeName;

    @Test
    public void updateNameToNonExistingAttribute() throws Exception {
        updateAttributeName = new UpdateAttributeName("condi", "pepe");
        GetNodesFromFile("//munit:assert-true", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        Element node = updateAttributeName.getNodes().get(0);
        assertNull(node.getAttribute("pepe"));
    }

    @Test
    public void updateNameToAttribute() throws Exception {
        updateAttributeName = new UpdateAttributeName("condition", "pepe");
        GetNodesFromFile("//munit:assert-true", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        Element node = updateAttributeName.getNodes().get(0);
        assertNotNull(node.getAttribute("pepe"));
    }

    @Test
    public void updateNameToAttributeToAlreadyDeclaredOne() throws Exception {
        updateAttributeName = new UpdateAttributeName("message", "level");
        GetNodesFromFile("//*[contains(local-name(),'logger')]", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        Element node = updateAttributeName.getNodes().get(0);
        assertNotNull(node.getAttribute("level"));
    }

    @Test (expected = MigrationStepException.class)
    public void updateNameToEmptyString() throws Exception {
        updateAttributeName = new UpdateAttributeName("message", "");
        GetNodesFromFile("//*[contains(local-name(),'logger')]", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        Element node = updateAttributeName.getNodes().get(0);
    }

    @Test (expected = MigrationStepException.class)
    public void updateNameToNullString() throws Exception {
        updateAttributeName = new UpdateAttributeName("message", null);
        GetNodesFromFile("//*[contains(local-name(),'logger')]", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        Element node = updateAttributeName.getNodes().get(0);
    }

    @Test
    public void executeTaskToEmptyNodeList() throws Exception {
        updateAttributeName = new UpdateAttributeName("message", "test");
        GetNodesFromFile("//lala", updateAttributeName, "src/test/resources/sample-file.xml");
        updateAttributeName.execute();
        assertEquals(Collections.<Element>emptyList(), updateAttributeName.getNodes());
    }

}