/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import org.jdom2.Element;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;

/**
 * GraphV2
 * @author Mulesoft Inc.
 */
public class FlowRef2 extends FlowComponent2 {

  public FlowRef2(Element element, String flowName, ApplicationGraph2 applicationGraph) {
    super(element, flowName, applicationGraph);
  }

  public FlowComponent2 rewire(Set<FlowComponent2> alreadyWired) {
    FlowComponent2 nextComponentInFlow = null;

    // 1. un-wire flow-ref to next element in flow if any
    Set<DefaultEdge> edges = applicationGraph.graph.outgoingEdgesOf(this);
    if (edges.size() > 1)
      throw new RuntimeException("wtf!");
    if (edges.size() == 1) {
      nextComponentInFlow = applicationGraph.graph.getEdgeTarget(edges.iterator().next());
      applicationGraph.graph.removeEdge(this, nextComponentInFlow);
    }

    // 2. wire flow-ref to target
    FlowComponent2 firstComponentInTargetFlow = applicationGraph.getFlowRefTargetComponent(getTargetFlowName());
    applicationGraph.graph.addEdge(this, firstComponentInTargetFlow);

    // 3. continue wiring target flow
    FlowComponent2 terminalComponent = applicationGraph.continueSemanticFlowWiring(firstComponentInTargetFlow, alreadyWired);

    // 4. wire final element to source flow element after flow-ref
    if (nextComponentInFlow != null) {
      applicationGraph.graph.addEdge(terminalComponent, nextComponentInFlow);
    }

    alreadyWired.add(this);

    return nextComponentInFlow;
  }

  private String getTargetFlowName() {
    return xmlElement.getAttributeValue("name");
  }

}
