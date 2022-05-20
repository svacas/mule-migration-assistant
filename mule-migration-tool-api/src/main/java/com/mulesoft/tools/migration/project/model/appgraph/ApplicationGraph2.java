/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import com.mulesoft.tools.migration.project.model.applicationgraph.PropertiesMigrationContext;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GraphV2
 * @author Mulesoft Inc.
 */
public class ApplicationGraph2 {

  public Graph<FlowComponent2, DefaultEdge> graph;

  private List<MessageSource2> sources = new ArrayList<>();
  private Map<String, FlowComponent2> flowRefTargets = new HashMap<>();

  public ApplicationGraph2() {
    graph = new DefaultDirectedGraph<>(DefaultEdge.class);
  }

  public void linealFlowWiring(List<FlowComponent2> flowComponents) {
    if (flowComponents.isEmpty())
      return;
    FlowComponent2 firstFlowComponent = flowComponents.get(0);
    if (firstFlowComponent instanceof MessageSource2) {
      sources.add((MessageSource2) firstFlowComponent);
    } else {
      // source-less flow, possible target of flow-refs
      flowRefTargets.put(firstFlowComponent.getFlowName(), firstFlowComponent);
    }
    FlowComponent2 previousFlowComp = null;
    for (FlowComponent2 comp : flowComponents) {
      graph.addVertex(comp);
      if (previousFlowComp != null) {
        graph.addEdge(previousFlowComp, comp);
      }
      previousFlowComp = comp;
    }
  }

  public void startSemanticFlowWiring() {
    Set<FlowComponent2> alreadyWired = new HashSet<>();
    for (MessageSource2 source : sources) {
      FlowComponent2 terminal = continueSemanticFlowWiring(source, alreadyWired);
      source.setTerminalComponent(terminal);
    }
  }

  public FlowComponent2 continueSemanticFlowWiring(FlowComponent2 flowComponent, Set<FlowComponent2> alreadyWired) {
    FlowComponent2 current = flowComponent;
    while (!(current instanceof TerminalComponent)) {
      current = current.rewire(alreadyWired);
    }
    return current;
  }

  public FlowComponent2 getFlowRefTargetComponent(String flowName) {
    // TODO report error if not found
    return flowRefTargets.get(flowName);
  }

  public void startPropertiesContextPropagation() {
    Map<FlowComponent2, Integer> visited = new HashMap<>();
    for (MessageSource2 source : sources) {
//      Map<String, PropertiesMigrationContext> propertiesMigrationContextMap = source.getPropertiesMigrationContextMap();
      continuePropertiesContextPropagation(source.getFirstProcessor(), source, visited);
      source.mergeTerminalPropertiesContext();
    }
  }

//  private FlowComponent2 continuePropertiesContextPropagation(FlowComponent2 flowComponent, Map<String, PropertiesMigrationContext> parentPropertiesMigrationContext, Map<FlowComponent2, Integer> visited) {
  private FlowComponent2 continuePropertiesContextPropagation(FlowComponent2 flowComponent, FlowComponent2 parent, Map<FlowComponent2, Integer> visited) {
    FlowComponent2 current = flowComponent;
    while (!(current instanceof TerminalComponent)) {
      current = current.propagatePropertiesContext(parent, visited);
    }
    return current;
  }

}
