package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ReplaceNodesQNameTest {
    private ReplaceNodesQName replaceQName;

    @Test
    public void replaceQName() throws Exception {

        replaceQName = new ReplaceNodesQName("", "");
        InitializeNodesForTest();

    }


    private void InitializeNodesForTest() throws Exception{
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("src/test/resources/sample-file.xml");
        Document document = saxBuilder.build(file);
        replaceQName.setNodes(null);
    }

}