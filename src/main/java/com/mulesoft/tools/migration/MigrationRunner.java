package com.mulesoft.tools.migration;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
            } else if (args[i].equalsIgnoreCase(FILES_PARAMETER)) {
                paths = args[i + 1];
                if (StringUtils.isEmpty(paths)) {
                    throw new  IllegalArgumentException("Need to provide the paths of files to migrate. Argument: " + FILES_PARAMETER + ":<path1;path2...etc>");
                } else {
                    files = new ArrayList<>(Arrays.asList(paths.split(";")));
                }
            } else if (args[i].equalsIgnoreCase(BACKUP_PARAMETER)) {
                backUp = Boolean.parseBoolean(args[i + 1]);
            }
        }

        if (StringUtils.isEmpty(configFile)) {
            throw new  IllegalArgumentException("Need to provide a configuration file with the details of the migration. Argument: " + CONFIG_FILE_PARAMETER + ":<path>");
        }

        MigrationJob job = new MigrationJob();
        job.setBackUpProfile(backUp);
        job.setDocuments(files);
        job.setConfigFilePath(configFile);

        job.execute();
    }

}
