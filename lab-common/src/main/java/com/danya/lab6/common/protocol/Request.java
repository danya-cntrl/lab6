package com.danya.lab6.common.protocol;

import java.io.Serializable;

public class Request implements Serializable {

    private final String commandName;
    private final Serializable argument;

    public Request(String commandName) {
        this.commandName = commandName;
        this.argument = null;
    }

    public Request(String commandName, Serializable argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Serializable getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return "Request{command='" + commandName + "', hasArg=" + (argument != null) + "}";
    }
}
