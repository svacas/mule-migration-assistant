package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.dw.DataweaveUtils;
import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Element;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class MigrateDataweaveScript extends MigrationStep {

    public MigrateDataweaveScript(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {

                if(!isEmpty(node.getText())) {
                    node.setText(DataweaveUtils.getMigratedScript(node.getText()));
                    getReportingStrategy().log("Dataweave script migrated to dw 2");
                }
            }
        }catch (Exception ex) {
            throw new MigrationStepException("Move attribute to MEL content exception. " + ex.getMessage());
        }
    }
}
