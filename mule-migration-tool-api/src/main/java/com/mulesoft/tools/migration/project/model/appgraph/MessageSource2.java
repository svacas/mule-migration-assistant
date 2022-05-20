/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import com.mulesoft.tools.migration.project.model.applicationgraph.PropertiesMigrationContext;
import com.mulesoft.tools.migration.project.model.applicationgraph.PropertyMigrationContext;
import org.jdom2.Element;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * GraphV2
 * @author Mulesoft Inc.
 */
public class MessageSource2 extends FlowComponent2 {

  private FlowComponent2 terminalComponent;

  public MessageSource2(Element element, String flowName, ApplicationGraph2 applicationGraph) {
    super(element, flowName, applicationGraph);
  }

  public void setTerminalComponent(FlowComponent2 terminalComponent) {
    this.terminalComponent = terminalComponent;
  }

  @Override
  public PropertiesMigrationContext getPropertiesMigrationContext() {
    if (propertiesMigrationContext.isEmpty()) {
      initializeSourcePropertiesMigrationContext();
    }
    return super.getPropertiesMigrationContext();
  }

  private void initializeSourcePropertiesMigrationContext() {
    // TODO get properties from each source type
    String inboundPrefix = xmlElement.getName();
    Map<String, PropertyMigrationContext> inboundContext = new HashMap<>();
    inboundContext.put(inboundPrefix + "1", new PropertyMigrationContext("attributes." + inboundPrefix + "1", false));
    inboundContext.put(inboundPrefix + "2", new PropertyMigrationContext("attributes." + inboundPrefix + "2", false));
    inboundContext.put(inboundPrefix + "3", new PropertyMigrationContext("attributes." + inboundPrefix + "3", true));
    Map<String, PropertyMigrationContext> outboundContext = new HashMap<>();
    propertiesMigrationContext.put(MAIN_CONTEXT, new PropertiesMigrationContext(inboundContext, outboundContext));
  }

  public void mergeTerminalPropertiesContext() {
    // TODO
  }

  public FlowComponent2 getFirstProcessor() {
    Set<DefaultEdge> edges = applicationGraph.graph.edgesOf(this);
    if (edges.size() != 1) throw new RuntimeException();
    return applicationGraph.graph.getEdgeTarget(edges.iterator().next());
  }
}
