package com.mulesoft.munit.tools.migration.task;

import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.List;


public class MigrationTask {

    private String xpathSelector;
    private ArrayList<MigrationStep> steps;
    private Document doc;
    private List<Element> nodes;

    public void setDocument(Document document) {
        this.doc = document;
    }

    public MigrationTask(String xpathSelector, ArrayList<MigrationStep> steps) {
        this.xpathSelector = xpathSelector;
        this.steps = steps;
        this.nodes = getNodesFromXPath(xpathSelector);
    }

    public void execute() throws Exception {
        for (MigrationStep step : steps) {
            step.setDocument(doc);
            step.setNodes(nodes);
            step.execute();
        }
    }

    private List<Element> getNodesFromXPath(String XpathExpression) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(XpathExpression, Filters.element(), null, doc.getRootElement().getAdditionalNamespaces());
        List<Element> nodes = xpath.evaluate(doc);
        return nodes;
    }
}
