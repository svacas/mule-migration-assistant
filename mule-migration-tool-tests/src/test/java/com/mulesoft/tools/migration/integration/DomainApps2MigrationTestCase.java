/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.integration;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DomainApps2MigrationTestCase extends EndToEnd2TestCase {

  @Parameters(name = "{0}")
  public static Object[] params() {
    return new Object[] {"domain1app1"};
  }

  private final String appToMigrate;

  public DomainApps2MigrationTestCase(String appToMigrate) {
    this.appToMigrate = appToMigrate;
  }

  @Test
  public void test() throws Exception {
    simpleCase(appToMigrate, "-parentDomainBasePath", new File(getResourceUri("e2e2/input/domain1")).getAbsolutePath());
  }

}
