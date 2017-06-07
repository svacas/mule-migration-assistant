package com.mulesoft.tools.migration.task;

import com.google.common.base.Strings;
import com.mulesoft.tools.migration.exception.MigrationTaskException;
import com.mulesoft.tools.migration.report.ConsoleReportStrategy;
import com.mulesoft.tools.migration.report.ReportingStrategy;
import com.mulesoft.tools.migration.task.step.MigrationStep;
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
    private ReportingStrategy reportingStrategy;
    private ArrayList<MigrationStep> steps;
    private List<Element> nodes;
    private String taskDescriptor;

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
            if (null == reportingStrategy) {
                reportingStrategy = new ConsoleReportStrategy();
            }
            nodes = getNodesFromXPath(this.xpathSelector);
            getReportingStrategy().log("******************** Working over:" + this.xpathSelector);
            for (MigrationStep step : steps) {
                step.setReportingStrategy(this.reportingStrategy);
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

    public void setTaskDescriptor(String descriptor) {
        this.taskDescriptor = descriptor;
    }

    public String getTaskDescriptor() {
        return this.taskDescriptor;
    }

    public void setDocument(Document document) {
        this.doc = document;
    }

    public void setReportingStrategy(ReportingStrategy reportingStrategy) {
        this.reportingStrategy = reportingStrategy;
    }

    public ReportingStrategy getReportingStrategy() {
        if (null == this.reportingStrategy) {
            this.reportingStrategy = new ConsoleReportStrategy();
        }
        return reportingStrategy;
    }
}
