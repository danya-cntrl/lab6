package com.danya.lab5.client.utils;

public class CommandMetadata {
    private final String name;
    private final boolean requiresStringArg;
    private final boolean requiresObjectArg;

    public CommandMetadata(String name, boolean requiresStringArg, boolean requiresObjectArg) {
        this.name = name;
        this.requiresStringArg = requiresStringArg;
        this.requiresObjectArg = requiresObjectArg;
    }

    public String getName() {
        return name;
    }

    public boolean isRequiresStringArg() {
        return requiresStringArg;
    }

    public boolean isRequiresObjectArg() {
        return requiresObjectArg;
    }
}
