package com.mulesoft.munit.tools.migration.task;

import com.google.common.base.Strings;
import com.mulesoft.munit.tools.migration.exception.MigrationTaskException;
import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MigrationTask {

    private String xpathSelector;
    private Document doc;
    private ArrayList<MigrationStep> steps;
    private List<Element> nodes;
    private String taskDescriptor;

    public void setTaskDescriptor(String descriptor) {
        this.taskDescriptor = descriptor;
    }

    public String getTaskDescriptor() {
        return this.taskDescriptor;
    }

    public void setDocument(Document document) {
        this.doc = document;
    }

    public MigrationTask(String xpathSelector) {
        this.xpathSelector = xpathSelector;
        this.steps = new ArrayList<>();
    }

    public MigrationTask() {
        this.steps = new ArrayList<>();
    }

    public void addStep(MigrationStep step) {
        if(step != null) {
            this.steps.add(step);
        }
    }

    public void execute() throws Exception {
        try {
            nodes = getNodesFromXPath(this.xpathSelector);
            for (MigrationStep step : steps) {
                step.setDocument(this.doc);
                step.setNodes(nodes);
                step.execute();
            }
        } catch (Exception ex) {
            throw new MigrationTaskException("Task execution exception. " + ex.getMessage());
        }
    }

    private List<Element> getNodesFromXPath(String XpathExpression) {
        if (!Strings.isNullOrEmpty(XpathExpression) && doc != null) {
            XPathExpression<Element> xpath = XPathFactory.instance().compile(XpathExpression, Filters.element(), null, doc.getRootElement().getAdditionalNamespaces());
            List<Element> nodes = xpath.evaluate(doc);
            return nodes;
        } else {
            return Collections.emptyList();
        }
    }
}
