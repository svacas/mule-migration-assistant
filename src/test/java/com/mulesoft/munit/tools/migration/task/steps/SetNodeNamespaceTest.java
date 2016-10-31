package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SetNodeNamespaceTest {
    private SetNodeNamespace addNamespaceStep;

    @Test
    public void addNamespace() throws Exception {
        addNamespaceStep = new SetNodeNamespace("lala","http://local","http://lala.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
        assertTrue(addNamespaceStep.getDocument().getRootElement().getAdditionalNamespaces().size() == 9);
    }

    @Test
    public void addNamespaceCheckSchemaLocationIsAdded() throws Exception {
        addNamespaceStep = new SetNodeNamespace("lala","http://local","http://lala.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
        assertTrue(addNamespaceStep.getDocument().getRootElement().getAttributes().get(0).getValue().contains("lala.xsd"));
    }

    @Test
    public void addDuplicateNamespace() throws Exception {
        addNamespaceStep = new SetNodeNamespace("munit","http://www.mulesoft.org/schema/mule/munit","http://www.mulesoft.org/schema/mule/munit/current/mule-munit.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
        assertTrue(addNamespaceStep.getDocument().getRootElement().getAttributes().get(0).getValue().contains("munit.xsd"));
    }

    @Test
    public void addNameSpaceEmptySchemaUrl() throws Exception {
        addNamespaceStep = new SetNodeNamespace("test", "htp://localhost", null);
        InitializeDocForTest();
        addNamespaceStep.execute();
    }

    @Test (expected = MigrationStepException.class)
    public void addNameSpaceEmptyUrl() throws Exception {
        addNamespaceStep = new SetNodeNamespace("test", null, "http://localhost/m.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
    }

    @Test
    public void addNameSpaceEmptyNamespace() throws Exception {
        addNamespaceStep = new SetNodeNamespace("test", "htp://localhost", "http://localhost/m.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
    }

    private void InitializeDocForTest() throws Exception{
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("src/test/resources/sample-file.xml");
        Document document = saxBuilder.build(file);
        addNamespaceStep.setDocument(document);
    }


}
