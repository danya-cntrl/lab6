package com.danya.lab6.server.managers;

import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.server.utils.ClientSession;
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
            clientChannel.register(selector, SelectionKey.OP_READ, new ClientSession());

        } catch (IOException e) {
            System.err.println("[ERROR] Не удалось принять подключение: " + e.getMessage());
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
        }

        return baos.toByteArray();
    }

    private Request deserializeRequest(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Request) ois.readObject();
        }
    }

    private void readRequest(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientSession session = (ClientSession) key.attachment();

        try {
            if (session.isReadingLength()) {
                int read = clientChannel.read(session.getLengthBuffer());
                if (read == -1) {
                    disconnect(key, clientChannel);
                    return;
                }
                if (session.getLengthBuffer().hasRemaining()) {
                    return;
                }

                session.getLengthBuffer().flip();
                int messageLength = session.getLengthBuffer().getInt();
                session.setDataBuffer(ByteBuffer.allocate(messageLength));
                session.setReadingLength(false);
            }

            int read = clientChannel.read(session.getDataBuffer());
            if (read == -1) {
                disconnect(key, clientChannel);
                return;
            }
            if (session.getDataBuffer().hasRemaining()) {
                return;
            }

            session.getDataBuffer().flip();
            byte[] requestBytes = new byte[session.getDataBuffer().remaining()];
            session.getDataBuffer().get(requestBytes);
            Request request = deserializeRequest(requestBytes);

            if (!"amebaLab6".equals(request.getAmebaToken())) {
                System.out.println("[WARN] запрос от неизвестного клиента");
                disconnect(key, clientChannel);
                return;
            }

            System.out.println("[INFO] Получена команда: " + request.getCommandName());

            Response response = requestHandler.handle(request);
            sendResponse(clientChannel, response);
            disconnect(key, clientChannel);
        } catch (IOException e) {
            System.err.println("[ERROR] Ошибка чтения: " + e.getMessage());
            disconnect(key, clientChannel);
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] Не найден класс Request");
            disconnect(key, clientChannel);
        }
    }

    private void sendResponse(SocketChannel clientChannel, Response response) {
        try {
            byte[] responseBytes = serialize(response);
            ByteBuffer buffer = ByteBuffer.allocate(4 + responseBytes.length);

            buffer.putInt(responseBytes.length);
            buffer.put(responseBytes);
            buffer.flip();

            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Не удалось отправить ответ: " + e.getMessage());
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