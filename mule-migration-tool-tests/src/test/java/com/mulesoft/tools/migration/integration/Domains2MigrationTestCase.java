/*
 * Copyright (c) 2020, Mulesoft, LLC. All rights reserved.
 * Use of this source code is governed by a BSD 3-Clause License
 * license that can be found in the LICENSE.txt file.
 */
package com.mulesoft.tools.migration.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Domains2MigrationTestCase extends EndToEnd2TestCase {

  @Parameters(name = "{0}")
  public static Object[] params() {
    return new Object[] {"domain1"};
  }

  private final String domainToMigrate;

  public Domains2MigrationTestCase(String domainToMigrate) {
    this.domainToMigrate = domainToMigrate;
  }

  @Test
  public void test() throws Exception {
    simpleCase(domainToMigrate);
  }

}
