package com.mulesoft.munit.tools.migration;

import com.mulesoft.munit.tools.migration.task.MigrationTask;
import com.mulesoft.munit.tools.migration.task.steps.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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


    @Test
    public void checkStepExecution() throws Exception {

        MigrationTask task = new MigrationTask("//munit:test");
        MigrationStep step;

        step = new AddAttribute("description", "MyNewDescription2");
        task.addStep(step);

        migrationJob.addTask(task);
        migrationJob.execute();

    }

    @Test
    public void checkMultipleStepExecution() throws Exception {

        MigrationTask task = new MigrationTask("//munit:test");
        MigrationStep step;

        step = new AddAttribute("description", "MyNewDescription4");

        task.addStep(step);

        step = new AddAttribute("enable", "false");

        task.addStep(step);

        migrationJob.addTask(task);
        migrationJob.execute();

    }

    @Test
    public void checkMoveSetMessagePayloadExecution() throws Exception {

        MigrationTask task = new MigrationTask("//munit:test/munit:set");
        MigrationStep step;

        step = new CreateChildNodeFromAttribute("payload");
        task.addStep(step);

        step = new MoveAttributeToChildNode("encoding", "payload");
        task.addStep(step);

        step = new MoveAttributeToChildNode("mimeType", "payload");
        task.addStep(step);

        migrationJob.addTask(task);
        migrationJob.execute();

    }

}