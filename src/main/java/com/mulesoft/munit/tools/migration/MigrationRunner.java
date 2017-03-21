package com.mulesoft.munit.tools.migration;

import java.util.ArrayList;

public class MigrationRunner {

    //TODO Add extra love to this

    public static final String CONFIG_FILE_PARAMETER = "-migrationConfigFile";
    public static final String FILES_PARAMETER = "-files";
    public static final String BACKUP_PARAMETER = "-backup";

    public static void main(String args[]) throws Exception {

        String configFile = null;
        Boolean backUp = false;
        String paths;
        ArrayList<String> files = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(CONFIG_FILE_PARAMETER)) {
                configFile = args[i + 1];
            }
            if (args[i].equalsIgnoreCase(FILES_PARAMETER)) {
                paths = args[i + 1];

                if (paths.contains(";")) {

                }
            }
            if (args[i].equalsIgnoreCase(BACKUP_PARAMETER)) {
                backUp = Boolean.parseBoolean(args[i + 1]);
            }
        }

        MigrationJob job = new MigrationJob();
        job.setBackUpProfile(backUp);
        job.setDocuments(files);
        job.setConfigFilePath(configFile);

        job.execute();
    }

}
