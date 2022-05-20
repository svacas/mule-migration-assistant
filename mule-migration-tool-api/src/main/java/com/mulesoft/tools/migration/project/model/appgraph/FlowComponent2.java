/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import com.mulesoft.tools.migration.project.model.applicationgraph.PropertiesMigrationContext;
import org.jdom2.Element;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
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

  /**
   * Preserves a property context per flow call stack.
   * For main-flow key is MAIN_CONTEXT.
   * For sub-flow level 1 key is the node-id of the processor after flow-ref.
   *  TODO what to do with terminals?
   *    - synthesize on flow-ref processing when no return processor present?
   *    - ???
   * For sub-flow level 2 keys is node-id-sub-flow1 + "__" + node-id-flow.
   */
  protected Map<String, PropertiesMigrationContext> propertiesMigrationContext = new HashMap<>();
  protected static final String MAIN_CONTEXT = "main";

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

//  public FlowComponent2 propagatePropertiesContext(Map<String, PropertiesMigrationContext> parentPropertiesMigrationContext, Map<FlowComponent2, Integer> visited) {
  public FlowComponent2 propagatePropertiesContext(FlowComponent2 parent, Map<FlowComponent2, Integer> visited) {
    int visits = visited.getOrDefault(this, 0);
    int parents = applicationGraph.graph.inDegreeOf(this);
    if (visits >= parents) throw new RuntimeException("too many visits!");

    // TODO review 
    propertiesMigrationContext = parent.getPropertiesMigrationContextMap(uuid);

    visited.compute(this, (k, v) -> visits + 1);
    return getNextProcessor();
  }

  public PropertiesMigrationContext getPropertiesMigrationContext() {
    return propertiesMigrationContext.values().stream().reduce(PropertiesMigrationContext::merge).orElseThrow(RuntimeException::new);
  }

  public Map<String, PropertiesMigrationContext> getPropertiesMigrationContextMap(String callerId) {
    return this.propertiesMigrationContext.entrySet().stream()
            .filter(e -> e.getKey().startsWith(callerId) || e.getKey().equals(MAIN_CONTEXT))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public FlowComponent2 getNextProcessor() {
    Set<DefaultEdge> edges = applicationGraph.graph.outgoingEdgesOf(this);
    if (edges.size() != 1) throw new RuntimeException("single next processor expected");
    return applicationGraph.graph.getEdgeTarget(edges.iterator().next());
  }

  @Override
  public String toString() {
    return getName();
  }
}
