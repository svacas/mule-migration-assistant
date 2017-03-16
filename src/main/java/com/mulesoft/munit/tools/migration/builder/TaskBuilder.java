package com.mulesoft.munit.tools.migration.builder;

import com.google.gson.Gson;
import com.mulesoft.munit.tools.migration.task.MigrationTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TaskBuilder {

    public static MigrationTask build(JSONObject taskDef) throws Exception {

        MigrationTask migrationTask = new Gson().fromJson(taskDef.toJSONString(), MigrationTask.class);

        JSONArray steps = (JSONArray) taskDef.get("stepsDefinition");

        for (Object step : steps) {
            JSONObject stepObj = (JSONObject) step;
            migrationTask.addStep(StepBuilder.build(stepObj));
        }

        return migrationTask;
    }

}
