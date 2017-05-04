package com.mulesoft.tools.migration;

import org.jdom2.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mulesoft.tools.migration.helpers.DocumentHelpers.getDocument;
import static com.mulesoft.tools.migration.helpers.DocumentHelpers.restoreTestDocument;

public class MigrationJobFileTest {
    private MigrationJob migrationJob;
    private Document docRestoreFile;
    private String USE_CASE_FILE_PATH = "src/test/resources/mule/examples/file/file-use-case.xml";
    private String TASKS_DIR_PATH = "src/test/resources/mule/tasks/file";

    @Before
    public void setUp() throws Exception {
        ArrayList<String> filePath1 = new ArrayList<>();
        filePath1.add(USE_CASE_FILE_PATH);

        migrationJob = new MigrationJob();
        migrationJob.setDocuments(filePath1);
        migrationJob.setBackUpProfile(Boolean.FALSE);
        docRestoreFile = getDocument(filePath1.get(0));
    }

    @Test
    public void jobWithTasksOnConfigFile() throws Exception {
        ArrayList<String> files = new ArrayList<String>(Arrays.asList(USE_CASE_FILE_PATH));

        migrationJob.setDocuments(files);
        migrationJob.setConfigFileDir(TASKS_DIR_PATH);
        migrationJob.execute();
    }

    @After
    public void restoreFileState() throws Exception {
        restoreTestDocument(docRestoreFile,USE_CASE_FILE_PATH);
    }
}
