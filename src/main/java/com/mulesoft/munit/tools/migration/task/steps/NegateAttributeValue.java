package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NegateAttributeValue extends MigrationStep {

    private String attrbuteName;

    public NegateAttributeValue(String attrbuteName) {
        this.attrbuteName = attrbuteName;
    }

    public void execute() throws Exception {
        Attribute att;
        for (Element node : getNodes()) {
            att = node.getAttribute(this.attrbuteName);
            if ( att != null) {
                String attValue = att.getValue();
                Pattern pattern = Pattern.compile("#\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(attValue);
                if (matcher.find()) {
                    att.setValue("#[not(" + matcher.group(1) + ")]");
                } else {
                    att.setValue("#[not(" + attValue + ")]");
                }
            }
        }
    }
}
