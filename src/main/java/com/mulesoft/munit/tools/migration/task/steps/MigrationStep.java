package com.mulesoft.munit.tools.migration.task.steps;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

public abstract class MigrationStep {

    private List<Element> nodes;
    private Document document;
    private String stepDescriptor;

    public void setStepDescriptor(String descriptor) {
        this.stepDescriptor = descriptor;
    }

    public String getStepDescriptor() {
        return this.stepDescriptor;
    }

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

    public abstract void execute() throws Exception;
}
