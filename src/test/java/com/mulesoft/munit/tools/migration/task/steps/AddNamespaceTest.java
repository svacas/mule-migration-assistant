package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AddNamespaceTest {
    private AddNamespace addNamespaceStep;

    @Test
    public void addNamespace() throws Exception {
        addNamespaceStep = new AddNamespace("lala","http://local","http://lala.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
        assertTrue(addNamespaceStep.getDocument().getRootElement().getAdditionalNamespaces().size() == 9);
    }

    @Test
    public void addNamespaceCheckSchemaLocationIsAdded() throws Exception {
        addNamespaceStep = new AddNamespace("lala","http://local","http://lala.xsd");
        InitializeDocForTest();
        addNamespaceStep.execute();
        assertTrue(addNamespaceStep.getDocument().getRootElement().getAttributes().get(0).getValue().contains("lala.xsd"));
    }

    private void InitializeDocForTest() throws Exception{
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("src/test/resources/sample-file.xml");
        Document document = saxBuilder.build(file);
        addNamespaceStep.setDocument(document);
    }


}
