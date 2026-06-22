package com.danya.lab6.client.managers;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientNetworkManager {
    private final String host;
    private final int basePort;
    private static final int MAX_PORT_ATTEMPTS = 20;

    public ClientNetworkManager(String host, int basePort) {
        this.host = host;
        this.basePort = basePort;
    }

    public Response sendRequest(Request request) {
        int portToTry = basePort;

        for (int attempt = 0; attempt < MAX_PORT_ATTEMPTS; attempt++) {
            try (Socket socket = new Socket(host, portToTry)) {

                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                byte[] requestBytes = serialize(request);

                ByteBuffer header = ByteBuffer.allocate(4);
                header.putInt(requestBytes.length);

                out.write(header.array());
                out.write(requestBytes);
                out.flush();

                byte[] lengthBytes = in.readNBytes(4);

                if (lengthBytes.length != 4) {
                    throw new IOException("Не удалось получить длину ответа");
                }

                int responseLength = ByteBuffer.wrap(lengthBytes).getInt();
                byte[] responseBytes = in.readNBytes(responseLength);
                return deserializeResponse(responseBytes);

            } catch (ConnectException e) {
                portToTry++;
            } catch (IOException e) {
                return new Response(false, "Ошибка связи с сервером: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                return new Response(false, "Ошибка десериализации ответа");
            }
        }
        return new Response(false, "Не удалось подключиться к серверу");
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
        }
        return baos.toByteArray();
    }

    private Response deserializeResponse(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Response) ois.readObject();
        }
    }
}