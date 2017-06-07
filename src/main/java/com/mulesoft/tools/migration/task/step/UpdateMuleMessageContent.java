package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import com.mulesoft.tools.migration.message.MuleMessageUtils;
import org.jdom2.Element;

public class UpdateMuleMessageContent extends MigrationStep {

    public UpdateMuleMessageContent(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                if(null != node.getText()) {
                    node.setText(MuleMessageUtils.replaceContent(node.getText()));

                    getReportingStrategy().log("Mule Message content updated for node:" + node);
                }
            }
        }catch (Exception ex) {
            throw new MigrationStepException("Remove node exception. " + ex.getMessage());
        }
    }
}
