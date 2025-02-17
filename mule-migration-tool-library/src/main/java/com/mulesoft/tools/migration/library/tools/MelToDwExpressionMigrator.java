/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.library.tools;

import static com.mulesoft.tools.migration.library.tools.PluginsVersions.targetVersion;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.addCompatibilityNamespace;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.join;

import com.mulesoft.tools.*;
import com.mulesoft.tools.migration.library.tools.mel.DefaultMelCompatibilityResolver;
import com.mulesoft.tools.migration.library.tools.mel.MelCompatibilityResolver;
import com.mulesoft.tools.migration.project.model.ApplicationModel;
import com.mulesoft.tools.migration.project.model.pom.Dependency;
import com.mulesoft.tools.migration.step.category.MigrationReport;
import com.mulesoft.tools.migration.util.ExpressionMigrator;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConverters;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Migrate mel expressions to dw expression
 *
 * @author Mulesoft Inc.
 * @since 1.0.0
 */
public class MelToDwExpressionMigrator implements ExpressionMigrator {

  private static Logger logger = LoggerFactory.getLogger(MelToDwExpressionMigrator.class);

  private final MigrationReport report;

  private final Pattern EXPRESSION_WRAPPER = Pattern.compile("^\\s*#\\[(.*)]\\s*$", Pattern.DOTALL);
  private final Pattern EXPRESSION_TEMPLATE_WRAPPER = Pattern.compile(".*#\\[(.*)].*", Pattern.DOTALL);

  private final MelCompatibilityResolver compatibilityResolver = new MelCompatibilityResolver();
  private final ApplicationModel model;

  public MelToDwExpressionMigrator(MigrationReport report, ApplicationModel model) {
    this.report = report;
    this.model = model;
  }

  @Override
  public String migrateExpression(String originalExpression, boolean dataWeaveBodyOnly, Element element) {
    return migrateExpression(originalExpression, dataWeaveBodyOnly, element, false);
  }

  @Override
  public String migrateExpression(String originalExpression, boolean dataWeaveBodyOnly, Element element, boolean enricher) {
    if (!isWrapped(originalExpression) && !originalExpression.contains("#[")) {
      return originalExpression;
    }
    String unwrapped = unwrap(originalExpression);
    unwrapped = unwrapped.replaceAll("mel:", "");
    String migratedExpression;
    if (!unwrapped.contains("#[")) {
      migratedExpression = wrap(translateSingleExpression(unwrapped, dataWeaveBodyOnly, element, enricher));
    } else {
      // Probably an interpolation
      TemplateParser muleStyleParser = TemplateParser.createMuleStyleParser();
      migratedExpression = muleStyleParser.translate(originalExpression,
                                                     (script) -> translateSingleExpression(script, dataWeaveBodyOnly,
                                                                                           element, enricher));
      if (migratedExpression.contains("mel:")) {
        addCompatibilityNamespace(element.getDocument());
      }
    }

    return StringUtils.replaceAll(migratedExpression, "\\r\\n|[\\r\\n]", " ");
  }

  public String translateSingleExpression(String unwrappedExpression, boolean dataWeaveBodyOnly, Element element,
                                          boolean enricher) {
    logger.debug("  --->> Evaluating MEL expression at element {} -> {}", element != null ? element.getName() : "null",
                 unwrappedExpression);
    String migratedExpression;
    MigrationResult result;
    try {
      result = Migrator.migrate(unwrappedExpression);
      migratedExpression = result.getGeneratedCode();
    } catch (Exception e) {
      return compatibilityResolver.resolve(unwrappedExpression, element, report, model, this, enricher);
    }
    if (result.metadata().children().exists(a -> a instanceof NonMigratable)) {
      List<NonMigratable> metadata =
          (List<NonMigratable>) (List<?>) JavaConverters.seqAsJavaList(result.metadata().children())
              .stream()
              .filter(a -> a instanceof NonMigratable)
              .collect(toList());

      metadata.forEach(a -> report.report(a.reason(), element, element));

      return new DefaultMelCompatibilityResolver().resolve(unwrappedExpression, element, report, model, this, enricher);
    }

    if (migratedExpression.contains("message.inboundAttachments")) {
      report.report("message.expressionsAttachments", element, element);
    }

    if (result.metadata().children().exists(a -> a instanceof JavaModuleRequired)) {
      Dependency javaModuleDependency = new Dependency.DependencyBuilder()
          .withGroupId("org.mule.module")
          .withArtifactId("mule-java-module")
          .withVersion(targetVersion("mule-java-module"))
          .withClassifier("mule-plugin")
          .build();
      model.getPomModel().ifPresent(m -> m.addDependency(javaModuleDependency));
    }

    migratedExpression = resolveServerContext(migratedExpression);
    migratedExpression = resolveIdentifiers(migratedExpression);

    if (dataWeaveBodyOnly) {
      migratedExpression = migratedExpression.replaceFirst("%dw 2\\.0\n---", "").trim();
    }

    report.melExpressionSuccess(unwrappedExpression);
    return escapeUnderscores(migratedExpression);
  }

  private String resolveServerContext(String expression) {
    return expression.replaceAll("(vars\\.)?server\\.dateTime", "now()")
        .replaceAll("(vars\\.)?server\\.nanoSeconds", "System.nanoTime()")
        .replaceAll("(vars\\.)?server\\.ip", "server.ip")
        .replaceAll("(vars\\.)?server\\.host", "server.host");
  }

  public String resolveIdentifiers(String expression) {
    return expression.replaceAll("flowVars", "vars")
        .replaceAll("recordVars", "vars")
        .replaceAll("message\\.id", "correlationId")
        .replaceAll("message\\.inboundProperties", "vars.compatibility_inboundProperties")
        .replaceAll("message\\.outboundProperties", "vars.compatibility_outboundProperties")
        .replaceAll("message\\.inboundAttachments", "payload.parts")
        .replaceAll("message\\.dataType\\.mimeType", "message.^mediaType")
        .replaceAll("message\\.dataType\\.encoding", "message.^encoding");
  }

  private String escapeUnderscores(String expression) {
    return Arrays.stream(expression.split("\\."))
        .map(part -> part.startsWith("_") ? format("'%s'", part) : part)
        .collect(Collectors.joining("."));
  }

  @Override
  public String unwrap(String originalExpression) {
    checkExpression(originalExpression);
    Matcher wrappedExpressionMatcher = EXPRESSION_WRAPPER.matcher(originalExpression);
    if (wrappedExpressionMatcher.matches()) {
      return unwrap(wrappedExpressionMatcher.group(1).trim());
    }
    return originalExpression;
  }

  @Override
  public String wrap(String originalExpression) {
    checkExpression(originalExpression);
    return isWrapped(originalExpression) ? originalExpression : "#[" + originalExpression + "]";
  }

  @Override
  public boolean isWrapped(String originalExpression) {
    checkExpression(originalExpression);
    return EXPRESSION_WRAPPER.matcher(originalExpression).matches();
  }

  @Override
  public boolean isTemplate(String originalExpression) {
    checkExpression(originalExpression);
    return EXPRESSION_TEMPLATE_WRAPPER.matcher(originalExpression).matches();
  }

  private void checkExpression(String expression) {
    requireNonNull(expression, "Expression cannot be null");
  }
}
