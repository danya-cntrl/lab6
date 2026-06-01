package com.danya.lab5.common.protocol;

import java.io.Serializable;

public class Response implements Serializable {

    private final boolean success;
    private final String message;
    private final Serializable data;

    public Response(boolean success, String message, Serializable data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Response(boolean success, String message) {
        this(success, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Serializable getData() {
        return data;
    }
}