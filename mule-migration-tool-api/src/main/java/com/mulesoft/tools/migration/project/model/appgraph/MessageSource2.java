/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.appgraph;

import org.jdom2.Element;

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
}
