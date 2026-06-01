package com.danya.lab6.server.managers;

import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.server.utils.RequestHandler;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerNetworkManager {
    private final int basePort;
    private final RequestHandler requestHandler;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private boolean isRunning = true;

    public ServerNetworkManager(int basePort, RequestHandler requestHandler) {
        this.basePort = basePort;
        this.requestHandler = requestHandler;
    }

    public void start() {
        int port = basePort;
        boolean flag = false;

        while (!flag) {
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.socket().bind(new InetSocketAddress(port));
                flag = true;

                System.out.println("[INFO] Сервер успешно запущен");
                System.out.println("[INFO] адрес: localhost, порт: " + port);

                selector = Selector.open();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            } catch (BindException e) {
                System.out.println("[WARN] Порт " + port + " занят. Пробую следующий");
                port++;
            } catch (IOException e) {
                System.err.println("[CRITICAL] Ошибка при запуске сервера: " + e.getMessage());
                return;
            }
        }

        runEventLoop();
    }

    private void runEventLoop() {
        System.out.println("[INFO] Ожидание подключений и запросов");

        while (isRunning) {
            try {
                int readyChannels = selector.select();
                if (readyChannels == 0) continue;
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            acceptConnection(key);
                        } else if (key.isReadable()) {
                            readRequest(key);
                        }
                    }
                    keyIterator.remove();
                }
            } catch (IOException e) {
                System.err.println("[ERROR] Ошибка в цикле селектора: " + e.getMessage());
            }
        }
    }

    private void acceptConnection(SelectionKey key) {
        try {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = server.accept();
            clientChannel.configureBlocking(false);

            System.out.println("[INFO] Клиент подключился: " + clientChannel.getRemoteAddress());
            clientChannel.register(selector, SelectionKey.OP_READ);

        } catch (IOException e) {
            System.err.println("[ERROR] Не удалось принять подключение: " + e.getMessage());
        }
    }


    private void readRequest(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 64);

        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                disconnect(key, clientChannel);
                return;
            }

            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {

                Request request = (Request) ois.readObject();
                System.out.println("[INFO] Успешно прочитана команда: " + request.getCommandName());

                Response response = requestHandler.handle(request);
                sendResponse(clientChannel, response);
            } catch (ClassNotFoundException e) {
                System.err.println("[ERROR] Ошибка десериализации: класс не найден.");
                sendResponse(clientChannel, new Response(false, "Серверная ошибка десериализации."));
            }

            disconnect(key, clientChannel);

        } catch (IOException e) {
            System.err.println("[WARN] Клиент отключился во время обмена данными.");
            disconnect(key, clientChannel);
        }
    }


    private void sendResponse(SocketChannel clientChannel, Response response) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(response);
            oos.flush();
            byte[] responseBytes = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
            System.out.println("[INFO] Ответ успешно отправлен клиенту: " + clientChannel.getRemoteAddress());

        } catch (IOException e) {
            System.err.println("[ERROR] Не удалось отправить ответ клиенту: " + e.getMessage());
        }
    }


    private void disconnect(SelectionKey key, SocketChannel clientChannel) {
        key.cancel();
        try {
            System.out.println("[INFO] Соединение закрыто для: " + clientChannel.getRemoteAddress());
            clientChannel.close();
        } catch (IOException ignored) {}
    }

    public void stop() {
        this.isRunning = false;
        if (selector != null) {
            try { selector.close(); serverChannel.close(); } catch (IOException ignored) {}
        }
    }
}