package com.mulesoft.munit.tools.migration;


import com.mulesoft.munit.tools.migration.task.MigrationTask;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;

public class MigrationJob {

    private String xmlPath;
    private ArrayList<MigrationTask> tasks;
    private Document document;

    public MigrationJob(String filePath, ArrayList<MigrationTask> tasks) throws Exception{
        this.xmlPath = filePath;

        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File(filePath);
        this.document = saxBuilder.build(file);

        this.tasks = tasks;
    }

    public void execute() throws Exception {
        for (MigrationTask task: tasks) {
            task.setDocument(document);
            task.execute();
        }
    }

}
