package com.danya.lab6.client.managers;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

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
            try (Socket socket = new Socket(host, portToTry);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                oos.writeObject(request);
                oos.flush();

                return (Response) ois.readObject();

            } catch (ConnectException e) {
                portToTry++;
            } catch (IOException e) {
                return new Response(false, "Ошибка связи с сервером на порту " + portToTry + ": " + e.getMessage());
            } catch (ClassNotFoundException e) {
                return new Response(false, "Ошибка десериализации ответа: класс не найден.");
            }
        }

        return new Response(false, "Не удалось подключиться к серверу. Были проверены порты от "
                + basePort + " до " + (portToTry - 1) + ". Возможно сервер лежит");
    }
}