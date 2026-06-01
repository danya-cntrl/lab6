package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Response execute(Request request);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}