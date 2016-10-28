package com.mulesoft.munit.tools.migration.task.steps;

import org.junit.Test;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.InitializeNodesForTest;
import static org.junit.Assert.*;

public class NegateAttributeValueTest {
    private NegateAttributeValue negateAtt;

    @Test
    public void notFoundAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("lala");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
    }

    @Test
    public void nullAttribute() throws Exception {
        negateAtt = new NegateAttributeValue(null);
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
    }

    @Test
    public void negateSimpleAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("enable");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
    }

    @Test
    public void negateAttributeInsideQuotes() throws Exception {
        negateAtt = new NegateAttributeValue("payload");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
    }

    @Test
    public void negatePlaceHolderAttribute() throws Exception {
        negateAtt = new NegateAttributeValue("payload");
        InitializeNodesForTest(negateAtt);
        negateAtt.execute();
    }
}