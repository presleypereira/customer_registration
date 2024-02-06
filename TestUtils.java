package com.shellenergy.functiontest.helper;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNullElse;

public class TestUtils {

    private TestUtils() {
    }

    public static String getEnvOrDefault(final String envKey, final String defaultValue) {
        return requireNonNullElse(getenv(envKey), defaultValue);
    }
}