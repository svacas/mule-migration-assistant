package com.mulesoft.munit.tools.migration.task;

import com.mulesoft.munit.tools.migration.task.steps.AddAttribute;
import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.mulesoft.munit.tools.migration.helpers.DocumentHelpers.getDocument;
import static org.junit.Assert.*;

/**
 * Created by julianpascual on 10/26/16.
 */
public class MigrationTaskTest {
    private MigrationTask migrationTask;

    @Test
    public void setNullSelector() throws Exception {
        migrationTask = new MigrationTask(null);
        migrationTask.execute();
        assertEquals(0, getListSize(migrationTask));
    }

    @Test
    public void setEmptySelector() throws Exception {
        migrationTask = new MigrationTask("");
        migrationTask.execute();
        assertEquals(0, getListSize(migrationTask));
    }

    @Test
    public void setSelectorForNoNode() throws Exception {
        migrationTask = new MigrationTask("//pepe");
        migrationTask.setDocument(getDocument("src/test/resources/sample-file.xml"));
        migrationTask.execute();
        assertEquals(0, getListSize(migrationTask));
    }

    @Test
    public void addNullStepToTask() throws Exception {
        migrationTask = new MigrationTask("//pepe");
        migrationTask.setDocument(getDocument("src/test/resources/sample-file.xml"));
        migrationTask.addStep(null);
        migrationTask.execute();
        assertEquals(0, getListSize(migrationTask));
    }

    @Test
    public void setSelectorForNodes() throws Exception {
        migrationTask = new MigrationTask("//munit:test");
        migrationTask.setDocument(getDocument("src/test/resources/sample-file.xml"));
        migrationTask.execute();
        assertEquals(34, getListSize(migrationTask));
    }

    @Test
    public void setSelectorForNodesAndExecuteStep() throws Exception {
        migrationTask = new MigrationTask("//munit:test");
        migrationTask.setDocument(getDocument("src/test/resources/sample-file.xml"));
        MigrationStep step = new AddAttribute("pepe", "pepa");
        migrationTask.addStep(step);
        migrationTask.execute();
        assertEquals(34, getListSize(migrationTask));
    }

    @Test
    public void setSelectorForNodesAndExecuteStepEmptyDoc() throws Exception {
        migrationTask = new MigrationTask("//munit:test");
        MigrationStep attStep = new AddAttribute("pepe", "test");
        migrationTask.addStep(attStep);
        migrationTask.execute();
        assertEquals(0, getListSize(migrationTask));
    }


    public int getListSize(MigrationTask task) throws Exception{
        int size;
        Field field = task.getClass().getDeclaredField("nodes");
        field.setAccessible(true);
        size = ((List)field.get(task)).size();
        return size;
    }

}