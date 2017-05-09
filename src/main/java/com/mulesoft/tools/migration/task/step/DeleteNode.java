package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;

public class DeleteNode extends MigrationStep {

    public DeleteNode(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                node.detach();
            }
        }catch (Exception ex) {
            throw new MigrationStepException("Remove node exception. " + ex.getMessage());
        }
    }
}
