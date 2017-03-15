package com.mulesoft.munit.tools.migration.builder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by julianpascual on 3/15/17.
 */
public class StepBuilder {

    public static MigrationStep build(JSONObject stepDef) {

        String stepType = stepDef.get("type").toString();
        String StepParameters = stepDef.get("parameters").toString();

        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> parameters = new Gson().fromJson(StepParameters, mapType);


        return null;
    }

}
