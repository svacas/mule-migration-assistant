/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.library.mule.steps.ftp;

import static com.mulesoft.tools.migration.library.mule.steps.core.properties.InboundPropertiesHelper.addAttributesMapping;
import static com.mulesoft.tools.migration.library.mule.steps.file.FileInboundEndpoint.migrateFileFilters;
import static com.mulesoft.tools.migration.library.mule.steps.ftp.FtpConfig.FTP_NAMESPACE;
import static com.mulesoft.tools.migration.step.util.TransportsUtils.migrateInboundEndpointStructure;
import static com.mulesoft.tools.migration.step.util.TransportsUtils.migrateSchedulingStrategy;
import static com.mulesoft.tools.migration.step.util.TransportsUtils.processAddress;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.CORE_NAMESPACE;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.addMigrationAttributeToElement;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.copyAttributeIfPresent;
import static com.mulesoft.tools.migration.step.util.XmlDslUtils.migrateRedeliveryPolicyChildren;

import com.mulesoft.tools.migration.step.category.MigrationReport;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Migrates the inbound endpoints of the ftp transport
 *
 * @author Mulesoft Inc.
 * @since 1.0.0
 */
public class FtpInboundEndpoint extends AbstractFtpEndpoint {

  public static final String XPATH_SELECTOR =
      "/*/mule:flow/*[namespace-uri() = '" + FTP_NS_URI + "' and local-name() = 'inbound-endpoint'][1]";

  @Override
  public String getDescription() {
    return "Update FTP inbound endpoints.";
  }

  public FtpInboundEndpoint() {
    this.setAppliedTo(XPATH_SELECTOR);
  }

  @Override
  public void execute(Element object, MigrationReport report) throws RuntimeException {
    object.setName("listener");
    object.setNamespace(FTP_NAMESPACE);
    addMigrationAttributeToElement(object, new Attribute("isMessageSource", "true"));

    String configName = object.getAttributeValue("connector-ref");
    Optional<Element> config;
    if (configName != null) {
      config = getApplicationModel().getNodeOptional("/*/*[namespace-uri() = '" + FTP_NS_URI
          + "' and local-name() = 'config' and @name = '" + configName + "']");
    } else {
      config = getApplicationModel().getNodeOptional("/*/*[namespace-uri() = '" + FTP_NS_URI + "' and local-name() = 'config']");
    }

    Element ftpConfig = migrateFtpConfig(object, config, configName, config, report);
    Element connection = ftpConfig.getChild("connection", FTP_NAMESPACE);

    addAttributesToInboundProperties(object, report);

    Element redelivery = object.getChild("idempotent-redelivery-policy", CORE_NAMESPACE);
    if (redelivery != null) {
      redelivery.setName("redelivery-policy");
      Attribute exprAttr = redelivery.getAttribute("idExpression");

      if (exprAttr != null) {
        // TODO MMT-128
        exprAttr.setValue(exprAttr.getValue().replaceAll("#\\[header\\:inbound\\:originalFilename\\]", "#[attributes.name]"));

        if (getExpressionMigrator().isWrapped(exprAttr.getValue())) {
          exprAttr.setValue(getExpressionMigrator()
              .wrap(getExpressionMigrator().migrateExpression(exprAttr.getValue(), true, object)));
        }
      }

      migrateRedeliveryPolicyChildren(redelivery, report);
    }

    migrateSchedulingStrategy(object, OptionalInt.of(1000));

    doExecute(object, report);

    migrateFileFilters(object, report, FTP_NAMESPACE, getApplicationModel());

    processAddress(object, report).ifPresent(address -> {
      connection.setAttribute("host", address.getHost());
      connection.setAttribute("port", address.getPort());

      if (address.getCredentials() != null) {
        String[] credsSplit = address.getCredentials().split(":");

        connection.setAttribute("username", credsSplit[0]);
        connection.setAttribute("password", credsSplit[1]);
      }
      object.setAttribute("directory", address.getPath() != null ? address.getPath() : "/");
    });
    copyAttributeIfPresent(object, connection, "host");
    copyAttributeIfPresent(object, connection, "port");
    copyAttributeIfPresent(object, connection, "user", "username");
    copyAttributeIfPresent(object, connection, "password");

    if (object.getAttribute("path") != null) {
      object.getAttribute("path").setName("directory");
    }
    if (object.getAttribute("connector-ref") != null) {
      object.getAttribute("connector-ref").setName("config-ref");
    } else {
      object.setAttribute("config-ref", ftpConfig.getAttributeValue("name"));
    }
    object.removeAttribute("name");

    copyAttributeIfPresent(object, connection, "passive");
    if (object.getAttribute("binary") != null) {
      connection.setAttribute("transferMode", "true".equals(object.getAttributeValue("binary")) ? "BINARY" : "ASCII");
      object.removeAttribute("binary");
    }

    if (object.getAttribute("encoding") != null) {
      object.getParent().addContent(3, new Element("set-payload", CORE_NAMESPACE)
          .setAttribute("value", "#[payload]")
          .setAttribute("encoding", object.getAttributeValue("encoding")));
      object.removeAttribute("encoding");
    }
    if (object.getAttribute("responseTimeout") != null) {
      copyAttributeIfPresent(object, connection, "responseTimeout", "connectionTimeout");
      connection.setAttribute("connectionTimeoutUnit", "MILLISECONDS");
    }

    if (object.getAttribute("exchange-pattern") != null) {
      object.removeAttribute("exchange-pattern");
    }
  }

  protected Optional<Element> fetchConfig(String configName) {
    return getApplicationModel().getNodeOptional("/*/*[namespace-uri() = '" + FTP_NS_URI + "' and local-name() = 'config']");
  }

  protected void doExecute(Element object, MigrationReport report) {
    // Nothing to do
  }

  private void addAttributesToInboundProperties(Element object, MigrationReport report) {
    migrateInboundEndpointStructure(getApplicationModel(), object, report, true);

    try {
      addAttributesMapping(getApplicationModel(), "org.mule.extension.ftp.api.ftp.FtpFileAttributes",
                           inboundToAttributesExpressions());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, String> inboundToAttributesExpressions() {
    Map<String, String> expressionsPerProperty = new LinkedHashMap<>();
    expressionsPerProperty.put("originalFilename", "message.attributes.name");
    expressionsPerProperty.put("fileSize", "message.attributes.size");
    expressionsPerProperty.put("timestamp", "message.attributes.timestamp");

    return expressionsPerProperty;
  }

}
