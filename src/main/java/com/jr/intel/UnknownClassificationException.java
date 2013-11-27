package com.jr.intel;

import java.util.Map;

public class UnknownClassificationException extends Exception {

    public UnknownClassificationException(Map<String, ?> attributes) {
        super(buildMessage(attributes));
    }

    private static String buildMessage(Map<String, ?> attributes) {
        // TODO Auto-generated method stub
        return null;
    }
}
