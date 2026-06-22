package com.danya.lab6.server.utils;

import java.nio.ByteBuffer;

public class ClientSession {

    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
    private ByteBuffer dataBuffer;
    private boolean readingLength = true;

    public ByteBuffer getLengthBuffer() {
        return lengthBuffer;
    }

    public ByteBuffer getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(ByteBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public boolean isReadingLength() {
        return readingLength;
    }

    public void setReadingLength(boolean readingLength) {
        this.readingLength = readingLength;
    }
}