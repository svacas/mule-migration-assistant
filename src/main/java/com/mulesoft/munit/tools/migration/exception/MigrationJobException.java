package com.mulesoft.munit.tools.migration.exception;

import com.sun.tools.javac.util.List;

public class MigrationJobException extends Exception {

    private String filePath;
    private List<String> exceptions;

    public MigrationJobException(String message, List<String> exceptions, String filePath) {
        super(message);
        this.filePath = filePath;
        this.exceptions = exceptions;
    }



}
