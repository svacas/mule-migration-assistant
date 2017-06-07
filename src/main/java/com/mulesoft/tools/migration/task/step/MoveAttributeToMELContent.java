package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

import static com.mulesoft.tools.migration.mel.MELUtils.getMELExpressionFromValue;

public class MoveAttributeToMELContent extends MigrationStep {

    private String attributeName;

    public MoveAttributeToMELContent(String attributeName) {
        setAttributeName(attributeName);
    }

    public MoveAttributeToMELContent(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                Attribute att = node.getAttribute(getAttributeName());
                if (att != null) {
                    node.removeAttribute(att);
                    node.setText(getMELExpressionFromValue(att.getValue()));

                    getReportingStrategy().log("Attribute moved to MEL content:" + att);
                }
            }
        }catch (Exception ex) {
            throw new MigrationStepException("Move attribute to MEL content exception. " + ex.getMessage());
        }
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
