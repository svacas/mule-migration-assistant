package com.mulesoft.munit.tools.migration.builder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulesoft.munit.tools.migration.task.steps.MigrationStep;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by julianpascual on 3/15/17.
 */
public class StepBuilder {

    public static MigrationStep build(JSONObject stepDef) throws Exception{

        String stepType = stepDef.get("type").toString();
        String stepParameters = stepDef.get("parameters").toString();
        String description = stepDef.get("stepDescriptor").toString();
        String methodName;
        Method method;

        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> parameters = new Gson().fromJson(stepParameters, mapType);

        try {

            Class<?> clazz = Class.forName(stepType);
            MigrationStep step = (MigrationStep) clazz.newInstance();

            for (String parameter : parameters.keySet()) {
                methodName = "set" + StringUtils.capitalize(parameter);
                method = clazz.getMethod(methodName, String.class);
                method.invoke(step, parameters.get(parameter));
            }

            method = clazz.getMethod("setStepDescriptor", String.class);
            method.invoke(step, description);

            return step;

        } catch (Exception ex) {
            throw new Exception("Failed to instance step: " + stepType);
        }
    }

}
