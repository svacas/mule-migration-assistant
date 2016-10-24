package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by julianpascual on 10/24/16.
 */
public abstract class MigrationStep implements IMigrationStep {

    private List<Element> nodes;
    private Document document;


    public void setNodes(List<Element> nodes) {
        this.nodes = nodes;
    }

    public List<Element> getNodes() {
        return this.nodes;
    }

    public void  setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return this.document;
    }

}
