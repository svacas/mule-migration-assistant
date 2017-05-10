package com.mulesoft.tools.migration.message;

import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by davidcisneros on 5/10/17.
 */
public class MuleMessageUtils {

    private MuleMessageUtils() {
    }

    public static String replaceContent(String content) {
        String newContent = content;
        newContent = newContent.replace("message.inboundProperties","attributes");
        newContent = newContent.replace("inboundProperties","attributes");
        newContent = newContent.replace("'http.query.params'","queryParams");
        newContent = newContent.replace("http.query.params","queryParams");
        newContent = newContent.replace("'http.uri.params'","uriParams");
        newContent = newContent.replace("http.uri.params","uriParams");
        return newContent;
    }
}
