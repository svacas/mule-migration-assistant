package com.mulesoft.munit.tools.migration.task.steps;

import com.mulesoft.munit.tools.migration.exception.MigrationStepException;
import com.sun.org.apache.xpath.internal.operations.Neg;
import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NegateAttributeValue extends MigrationStep {

    private String attributeName;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public NegateAttributeValue(String attributeName) {
        setAttributeName(attributeName);
    }

    public NegateAttributeValue(){}

    public void execute() throws Exception {
        Attribute att;
        try {
            if (this.getAttributeName() != null) {
                for (Element node : getNodes()) {
                    att = node.getAttribute(this.getAttributeName());
                    if (att != null) {
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
        } catch (Exception ex) {
            throw new MigrationStepException("Negate attribute exception. " + ex.getMessage());
        }
    }


}
