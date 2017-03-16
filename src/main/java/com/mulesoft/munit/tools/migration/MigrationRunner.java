package com.mulesoft.munit.tools.migration;

public class MigrationRunner {
    public static final String CONFIG_FILE_PARAMETER = "-migrationConfigFile";
    public static final String FILES_PARAMETER = "-files";
    public static final String BACKUP_PARAMETER = "-backup";

    public static void main(String args[]) throws Exception {

        String configFile = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(CONFIG_FILE_PARAMETER)) {
                configFile = args[i + 1];
            }
        }

        MigrationJob job = new MigrationJob();
        job.setConfigFilePath(configFile);

        job.execute();
    }

}
