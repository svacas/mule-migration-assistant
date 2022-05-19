/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.library.applicationgraph;

import com.google.common.collect.Lists;
import com.mulesoft.tools.migration.project.model.appgraph.ApplicationGraph2;
import com.mulesoft.tools.migration.project.model.applicationgraph.GraphRenderer2;
import org.jdom2.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.mulesoft.tools.migration.helper.DocumentHelper.getDocument;

public class ApplicationGraph2Test {

  private static final Path CONFIG_EXAMPLES_PATH = Paths.get("mule/apps/nocompatibility");
  public static final String MULE_3_CONFIG = "nocomp-01.xml";

  private final Path configPath;
  private Document doc;
  private ApplicationGraphCreator2 applicationGraphCreator;
  private ApplicationGraph2 graph;

  public ApplicationGraph2Test() {
    configPath = CONFIG_EXAMPLES_PATH.resolve(MULE_3_CONFIG);
  }

  @Before
  public void setUp() throws Exception {
    doc = getDocument(this.getClass().getClassLoader().getResource(configPath.toString()).toURI().getPath());
    applicationGraphCreator = new ApplicationGraphCreator2();
    graph = applicationGraphCreator.create(Lists.newArrayList(doc));
  }

  @Test
  public void testAbleToFindGraphComponentFromXMLElement() throws IOException {
    GraphRenderer2.render(graph, MULE_3_CONFIG);

    //        String loggerSubflow3Expression = "//*[local-name()='sub-flow' and @name='flow3']/*[local-name()='logger']";
    //        Element loggerElement =
    //                Iterables.getOnlyElement(XPathFactory.instance().compile(loggerSubflow3Expression, Filters.element()).evaluate(doc));
    //
    //        FlowComponent flowComponent = graph.findFlowComponent(loggerElement);
    //        assertNotNull(flowComponent);
    //        assertTrue(flowComponent instanceof MessageProcessor);
  }

}
