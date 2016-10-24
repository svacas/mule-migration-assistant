package com.mulesoft.munit.tools.migration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by julianpascual on 10/24/16.
 */
public class MigrationJobTest {
    private MigrationJob migrationJob;

    @Before
    public void setUp() throws Exception {
        migrationJob = new MigrationJob("src/test/resources/sample-file.xml");
    }

    @Test
    public void testExecuteEmptySteps() throws Exception {
        migrationJob.execute();
        assertNotNull(migrationJob);
    }

}