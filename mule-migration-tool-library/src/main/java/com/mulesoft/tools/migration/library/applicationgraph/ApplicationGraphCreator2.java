/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.library.applicationgraph;

import com.google.common.collect.ImmutableList;
import com.mulesoft.tools.migration.project.model.appgraph.ApplicationGraph2;
import com.mulesoft.tools.migration.project.model.appgraph.FlowComponent2;
import com.mulesoft.tools.migration.project.model.appgraph.FlowRef2;
import com.mulesoft.tools.migration.project.model.appgraph.MessageSource2;
import com.mulesoft.tools.migration.project.model.applicationgraph.ApplicationGraph;
import com.mulesoft.tools.migration.project.model.applicationgraph.Flow;
import com.mulesoft.tools.migration.project.model.applicationgraph.FlowComponent;
import com.mulesoft.tools.migration.project.model.applicationgraph.FlowRef;
import com.mulesoft.tools.migration.project.model.applicationgraph.MessageProcessor;
import com.mulesoft.tools.migration.step.category.MigrationReport;
import com.mulesoft.tools.migration.util.ExpressionMigrator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mulesoft.tools.migration.step.util.XmlDslUtils.CORE_NS_URI;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.getAllElementsFromNamespaceXpathSelector;

/**
 * GraphV2
 * @author Mulesoft Inc.
 */
public class ApplicationGraphCreator2 {

  public static final String FLOW_XPATH =
      getAllElementsFromNamespaceXpathSelector(CORE_NS_URI, ImmutableList.of("flow", "sub-flow"), true, false);

  private ExpressionMigrator expressionMigrator;

  public ApplicationGraph2 create(ArrayList<Document> applicationDocuments) {
    return create(applicationDocuments, null);
  }

  public ApplicationGraph2 create(ArrayList<Document> applicationDocuments, MigrationReport report) {

    ApplicationGraph2 applicationGraph = new ApplicationGraph2();

    // step 1: get flow elements
    List<Flow> applicationFlows = applicationDocuments.stream()
        .map(this::getFlows)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    // step 2:
    //      - convert xmlElements -> flowComponent
    //      - add all flow components to graph
    //      - lineal flow/sub-flow wiring
    applicationFlows.forEach(flow -> {
      List<FlowComponent2> flowComponents = getFlowComponents(flow, applicationGraph);
      //            flow.setComponents(flowComponents);
      applicationGraph.linealFlowWiring(flowComponents);
    });

    // step 3: specialized flow wiring (flow-refs, choice, async, apikit, ...)
    applicationGraph.startSemanticFlowWiring();

    // PropertyContext propagation
    // TODO

    return applicationGraph;
  }

  private List<Flow> getFlows(Document document) {
    List<Element> flowsAsXml = getChildrenMatchingExpression(document.getRootElement(), FLOW_XPATH, Filters.element());

    return flowsAsXml.stream()
        .map(this::convertToFlow)
        .collect(Collectors.toList());
  }

  private <T> List<T> getChildrenMatchingExpression(Element elementToEvaluate, String expression, Filter<T> filter) {
    return XPathFactory.instance().compile(expression, filter).evaluate(elementToEvaluate);
  }

  private Flow convertToFlow(Element flowAsXml) {
    return new Flow(flowAsXml);
  }

  private List<FlowComponent2> getFlowComponents(Flow flow, ApplicationGraph2 applicationGraph) {
    Element flowAsXmL = flow.getXmlElement();

    return flowAsXmL.getContent().stream()
        .filter(Element.class::isInstance)
        .map(Element.class::cast)
        //                .map(xmlElement -> convertAndAddToGraph(xmlElement, flow, applicationFlows, report, applicationGraph))
        .map(xmlElement -> createFlowComponent(xmlElement, flow.getName(), applicationGraph))
        .collect(Collectors.toList());
  }

  private FlowComponent2 createFlowComponent(Element xmlElement, String flowName, ApplicationGraph2 applicationGraph) {
    if (isMessageSource(xmlElement)) {
      return new MessageSource2(xmlElement, flowName, applicationGraph);
    }
    if ("flow-ref".equals(xmlElement.getName())) {
      return new FlowRef2(xmlElement, flowName, applicationGraph);
    }
    return new FlowComponent2(xmlElement, flowName, applicationGraph);
  }

  private boolean isMessageSource(Element xmlElement) {
    return "listener".equals(xmlElement.getName());
  }

  private FlowComponent convertAndAddToGraph(Element xmlElement, Flow parentFlow,
                                             List<Flow> applicationFlows, MigrationReport report,
                                             ApplicationGraph applicationGraph) {
    FlowComponent component = null;
    if (xmlElement.getName().equals("flow-ref")) {
      if (!expressionMigrator.isWrapped(xmlElement.getAttribute("name").getValue())) {
        String destinationFlowName = xmlElement.getAttribute("name").getValue();
        Optional<Flow> destinationFlow = applicationFlows.stream()
            .filter(flow -> flow.getName().equals(destinationFlowName))
            .findFirst();

        if (destinationFlow.isPresent()) {
          return new FlowRef(xmlElement, parentFlow, destinationFlow.get(), applicationGraph);
        } else {
          //                    report.report("nocompatibility.missingflow", xmlElement, xmlElement);
        }
      } else {
        //                report.report("nocompatibility.dynamicflowref", xmlElement, xmlElement);
      }
      //        } else if (isPropertySource(xmlElement, parentFlow)) {
      //            component = new PropertiesSourceComponent(xmlElement, parentFlow, applicationGraph);
    } else {
      component = new MessageProcessor(xmlElement, parentFlow, applicationGraph);
    }

    applicationGraph.addFlowComponent(component);
    return component;
  }

  public void setExpressionMigrator(ExpressionMigrator expressionMigrator) {
    this.expressionMigrator = expressionMigrator;
  }

}
