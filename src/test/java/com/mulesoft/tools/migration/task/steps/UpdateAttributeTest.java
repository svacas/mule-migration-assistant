package com.mulesoft.tools.migration.task.steps;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import com.mulesoft.tools.migration.helpers.DocumentHelpers;
import org.jdom2.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateAttributeTest {
    private UpdateAttribute updateAttribute;

    @Test
    public void updateValueToNonExistingAttribute() throws Exception {
        updateAttribute = new UpdateAttribute("condi", "lala");
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
        assertNull(node.getAttribute("pepe"));
    }

    @Test
    public void updateSimpleValue() throws Exception {
        updateAttribute = new UpdateAttribute("condition", "lala");
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
        assertEquals("lala", node.getAttributeValue("condition"));
    }

    @Test
    public void updateValueToComplex() throws Exception {
        updateAttribute = new UpdateAttribute("condition", "#[message.flowVars('pepe')]");
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
        assertEquals("#[message.flowVars('pepe')]", node.getAttributeValue("condition"));
    }

    @Test
    public void updateValueToPlaceholder() throws Exception {
        updateAttribute = new UpdateAttribute("condition", "${myprop}");
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
        assertEquals("${myprop}", node.getAttributeValue("condition"));
    }

    @Test (expected = MigrationStepException.class)
    public void updateValueToNull() throws Exception {
        updateAttribute = new UpdateAttribute("condition", null);
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
    }

    @Test
    public void updateValueToEmpty() throws Exception {
        updateAttribute = new UpdateAttribute("condition", "");
        DocumentHelpers.GetNodesFromFile("//munit:assert-true", updateAttribute, "src/test/resources/sample-file.xml");
        updateAttribute.execute();
        Element node = updateAttribute.getNodes().get(0);
        assertEquals("", node.getAttributeValue("condition"));
    }
}
