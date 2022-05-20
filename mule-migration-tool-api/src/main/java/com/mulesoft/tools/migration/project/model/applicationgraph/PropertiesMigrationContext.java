/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.project.model.applicationgraph;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Models the context for properties migration
 *
 * @author Mulesoft Inc.
 * @since 1.3.0
 */
public class PropertiesMigrationContext {

  private final Map<String, PropertyMigrationContext> inboundContext;
  private final Map<String, PropertyMigrationContext> outboundContext;

  public PropertiesMigrationContext(Map<String, PropertyMigrationContext> inboundContext,
                                    Map<String, PropertyMigrationContext> outboundContext) {
    this.inboundContext = ImmutableMap.copyOf(inboundContext);
    this.outboundContext = ImmutableMap.copyOf(outboundContext);
  }

  public Map<String, PropertyMigrationContext> getInboundContext() {
    return this.inboundContext;
  }

  public Map<String, PropertyMigrationContext> getOutboundContext() {
    return this.outboundContext;
  }

  public PropertiesMigrationContext merge(PropertiesMigrationContext target) {
    return new PropertiesMigrationContext(merge(inboundContext, target.inboundContext), merge(outboundContext, target.outboundContext));
  }

  private Map<String, PropertyMigrationContext> merge(Map<String, PropertyMigrationContext> source, Map<String, PropertyMigrationContext> target) {
    Set<String> keys = new HashSet<>(source.keySet());
    keys.addAll(target.keySet());
    Map<String, PropertyMigrationContext> result = new HashMap<>();
    for (String key : keys) {
      if (source.containsKey(key) && target.containsKey(key)) {
        // TODO what to do if translations differ?
        //  - outbound: translation does not matter
        //  - inbound: ??? two sources with same property-name pointing to different attributes
        result.put(key, new PropertyMigrationContext(source.get(key).getTranslation(), source.get(key).isOptional() || target.get(key).isOptional()));
      } else if (source.containsKey(key)) {
        result.put(key, new PropertyMigrationContext(source.get(key).getTranslation(), source.get(key).isOptional()));
      } else {
        result.put(key, new PropertyMigrationContext(target.get(key).getTranslation(), target.get(key).isOptional()));
      }
    }
    return result;
  }

}
