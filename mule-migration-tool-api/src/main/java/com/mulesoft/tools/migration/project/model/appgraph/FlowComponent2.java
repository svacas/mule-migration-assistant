/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import org.jdom2.Element;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GraphV2
 * @author Mulesoft Inc.
 */
public class FlowComponent2 {

  protected Element xmlElement;
  private String flowName;
  private String uuid;

  ApplicationGraph2 applicationGraph;

  public FlowComponent2(Element element, String flowName, ApplicationGraph2 applicationGraph) {
    this.xmlElement = element;
    this.flowName = flowName;
    this.uuid = UUID.randomUUID().toString();
    this.applicationGraph = applicationGraph;
  }

  public String getName() {
    return xmlElement.getName() + "__" + flowName + "__" + uuid.substring(0, 4);
  }

  public String getFlowName() {
    return flowName;
  }

  /**
   * rewires the current node and returns the next node (if any) to continue rewiring
   */
  public FlowComponent2 rewire(Set<FlowComponent2> alreadyWired) {
    alreadyWired.add(this);
    Set<FlowComponent2> collect = applicationGraph.graph.outgoingEdgesOf(this).stream()
        .map(e -> applicationGraph.graph.getEdgeTarget(e))
        .collect(Collectors.toSet());
    if (collect.size() > 1)
      throw new RuntimeException("wtf!");
    if (collect.size() == 1) {
      return collect.iterator().next();
    }

    TerminalComponent terminalComponent = new TerminalComponent(xmlElement, flowName, applicationGraph);
    applicationGraph.graph.addVertex(terminalComponent);
    applicationGraph.graph.addEdge(this, terminalComponent);
    return terminalComponent;
  }

  @Override
  public String toString() {
    return getName();
  }
}
