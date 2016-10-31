package com.mulesoft.munit.tools.migration;


import com.mulesoft.munit.tools.migration.task.MigrationTask;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MigrationJob {

    private String xmlPath;
    private ArrayList<MigrationTask> tasks = new ArrayList<MigrationTask>();
    private Document document;

    public Document getDocument() {
        return document;
    }

    public void setDocument(String filePath) throws Exception {
        this.xmlPath = filePath;
    }

    public void addTask(MigrationTask task) {
        this.tasks.add(task);
    }

    public void execute() throws Exception {
        this.document = generateDoc(this.xmlPath);
        for (MigrationTask task: tasks) {
            task.setDocument(this.document);
            task.execute();
        }

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(this.document, new FileOutputStream(this.xmlPath));
//        xmlOutputter.output(this.document, new FileOutputStream("testout.xml"));
    }

    public Document generateDoc(String filePath) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File(filePath);
        return saxBuilder.build(file);
    }

}
