package com.mulesoft.tools.migration.message;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Created by davidcisneros on 5/10/17.
 */
public class MuleMessageUtils {

    private static final String HTTP_PROPERTIES_PACKAGE = "/message/http.keys.properties";
    private static final String WS_PROPERTIES_PACKAGE = "/message/ws.keys.properties";

    private static Properties properties;

    private MuleMessageUtils() {
    }

    public static String replaceContent(String content) throws Exception {
        for(Map.Entry<Object, Object> property : getProperties().entrySet()) {
            content = content.replace(property.getKey().toString(),property.getValue().toString());
            content = content.replace("'"+property.getKey().toString()+"'",property.getValue().toString());
        }
        return content;
    }

    private static Properties getProperties() throws Exception {
        if(properties == null) {
            properties = new Properties();
            loadProperties(properties, HTTP_PROPERTIES_PACKAGE);
            loadProperties(properties, WS_PROPERTIES_PACKAGE);
        }
        return properties;
    }

    private static void loadProperties(Properties properties, String path) throws Exception {
        InputStream in = MuleMessageUtils.class.getResourceAsStream(path);
        properties.load(in);
        in.close();
    }
}
