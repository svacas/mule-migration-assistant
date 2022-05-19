/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.applicationgraph;

import com.mulesoft.tools.migration.project.model.appgraph.ApplicationGraph2;
import com.mulesoft.tools.migration.project.model.appgraph.FlowComponent2;
import com.mulesoft.tools.migration.project.model.appgraph.MessageSource2;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.dot.DOTExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphRenderer2 {

  private static final Logger logger = LoggerFactory.getLogger(GraphRenderer2.class);

  private static String[] COLORS = {
      "#f4ee60", // yellow
      "#f4a460", // orange
      "#f46066", // red
      "#f460b0", // pink
      "#60f4a4", // green
      "#60b0f4", // blue
  };

  static {
    Graphviz.useEngine(new GraphvizJdkEngine());
  }

  public static void render(ApplicationGraph2 graph, String filePrefix) throws IOException {
    String dot = generateDot(graph.graph);
    logger.info("\n" + filePrefix + ".dot:\n" + dot);
    MutableGraph g = new Parser().read(dot);
    Graphviz.fromGraph(g).width(1280).render(Format.PNG).toFile(new File("target/graphs/" + filePrefix + ".png"));
  }

  private static String generateDot(Graph<FlowComponent2, DefaultEdge> stringGraph)
      throws ExportException {
    DOTExporter<FlowComponent2, DefaultEdge> exporter = new DOTExporter<>(v -> GraphRenderer2.getElementName(v));
    Map<String, String> flowColors = new HashMap<>();
    exporter.setVertexAttributeProvider((v) -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      map.put("style", DefaultAttribute.createAttribute("filled"));
      map.put("fillcolor", DefaultAttribute.createAttribute(getFlowColor(v, flowColors)));
      map.put("label",
              DefaultAttribute.createAttribute(String.format("%s\n(%s)", v.getClass().getSimpleName(), getElementName(v))));
      return map;
    });
    Writer writer = new StringWriter();
    exporter.exportGraph(stringGraph, writer);
    return writer.toString();
  }

  private static String getElementName(FlowComponent2 v) {
    return v.getName().replaceAll("-", "_");
  }

  private static String getFlowColor(FlowComponent2 flowComponent, Map<String, String> flowColors) {
    return flowColors.computeIfAbsent(flowComponent.getFlowName(), fn -> {
      if (flowComponent instanceof MessageSource2) {
        return "#ffffff"; // white
      } else {
        return COLORS[(int) (flowColors.values().stream().filter(v -> !"#ffffff".equals(v)).count() % COLORS.length)];
      }
    });
  }
}
