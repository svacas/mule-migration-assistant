package com.mulesoft.munit.tools.migration;


import com.mulesoft.munit.tools.migration.task.MigrationTask;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;

public class MigrationJob {

    private String xmlPath;
    private ArrayList<MigrationTask> tasks = new ArrayList<MigrationTask>();
    private Document document;

    public Document getDocument() {
        return document;
    }

    public MigrationJob(String filePath) throws Exception{
        this.xmlPath = filePath;

        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File(filePath);
        this.document = saxBuilder.build(file);
    }

    public void addTask(MigrationTask task) {
        this.tasks.add(task);
    }

    public void execute() throws Exception {
        for (MigrationTask task: tasks) {
            task.setDocument(this.document);
            task.execute();
        }
    }

}
