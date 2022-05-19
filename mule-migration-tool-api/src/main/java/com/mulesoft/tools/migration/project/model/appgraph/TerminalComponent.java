/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import org.jdom2.Element;

import java.util.Set;

/**
 * Injected node pointed by all flow paths terminal processors
 *
 * @author Mulesoft Inc.
 */
public class TerminalComponent extends FlowComponent2 {

  public TerminalComponent(Element element, String flowName, ApplicationGraph2 applicationGraph) {
    super(element, flowName, applicationGraph);
  }

  @Override
  public String getName() {
    return "Terminal__" + getFlowName();
  }

  @Override
  public FlowComponent2 rewire(Set<FlowComponent2> alreadyWired) {
    throw new UnsupportedOperationException("Cannot call rewire on terminal component!");
  }
}
