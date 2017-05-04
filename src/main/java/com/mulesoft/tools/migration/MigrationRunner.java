package com.mulesoft.tools.migration;

import com.mulesoft.tools.migration.exception.ConsoleOptionsException;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mulesoft.tools.migration.MigrationRunner.MigrationConsoleOptions.*;

public class MigrationRunner {

    private ArrayList<String> files;
    private String migrationConfigFile;
    private String migrationConfigDir;
    private Boolean backup;

    public static void main(String args[]) throws Exception {
        MigrationRunner migrationRunner = new MigrationRunner();
        migrationRunner.initializeOptions(args);

        MigrationJob job = new MigrationJob();
        job.setBackUpProfile(migrationRunner.backup);
        job.setDocuments(migrationRunner.files);
        job.setConfigFilePath(migrationRunner.migrationConfigFile);
        job.setConfigFileDir(migrationRunner.migrationConfigDir);

        job.execute();
    }

    /**
     * Initialises the console options with Apache Command Line
     * @param args
     */
    private void initializeOptions(String[] args) {

        Options options = new Options();

        options.addOption(MIGRATION_CONFIG_FILE,true,"Migration config file (json) containing all the rules and step" );
        options.addOption(MIGRATION_CONFIG_DIR,true,"Migration config root directory containing all the json files with the rules configurations" );
        options.addOption(FILES,true,"List of paths separated by ';' example: path1;path2...etc");
        options.addOption(BACKUP,true,"Flag to determine if you want a backup of your original files");
        options.addOption(HELP,false,"Shows the help");

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);

            if(line.hasOption(MIGRATION_CONFIG_FILE) && !line.hasOption(MIGRATION_CONFIG_DIR)) {
                this.migrationConfigFile = line.getOptionValue(MIGRATION_CONFIG_FILE);
            } else if (!line.hasOption(MIGRATION_CONFIG_FILE) && line.hasOption(MIGRATION_CONFIG_DIR))  {
                this.migrationConfigDir = line.getOptionValue(MIGRATION_CONFIG_DIR);
            } else {
                throw new ConsoleOptionsException("You must specify a migration config file OR a config dir");
            }

            if(line.hasOption(FILES)) {
                this.files = new ArrayList<>(Arrays.asList(line.getOptionValue(FILES).split(";")));
            }else{
                throw new ConsoleOptionsException("You must specify a file path or a list of paths separated by ';' example: path1;path2...etc");
            }

            if(line.hasOption(BACKUP)) {
                this.backup = Boolean.parseBoolean(line.getOptionValue(BACKUP));
            }else{
                this.backup = Boolean.FALSE;
            }

            if(line.hasOption(HELP)) {
                printHelp(options);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ConsoleOptionsException e) {
            printHelp(options);
            System.exit(-1);
        }
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("migration-tool - Help", options);
    }

    static class MigrationConsoleOptions {
        public final static String MIGRATION_CONFIG_FILE = "migrationConfigFile";
        public final static String MIGRATION_CONFIG_DIR= "migrationConfigDir";
        public final static String FILES= "files";
        public final static String BACKUP= "backup";
        public final static String HELP= "help";
    }
}