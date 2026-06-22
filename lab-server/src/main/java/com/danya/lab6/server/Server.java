package com.danya.lab6.server;

import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.server.managers.FileManager;
import com.danya.lab6.server.utils.RequestHandler;
import com.danya.lab6.server.managers.ServerNetworkManager;

import java.util.*;

public class Server {
    public static void main(String[] args) {
        int port = 6067;

        Map<String, String> envMap = new HashMap<>();
        envMap.put("1", "LAB6_FILE_1");
        envMap.put("2", "LAB6_FILE_2");
        envMap.put("3", "LAB6_FILE_3");

        if (args.length != 1) {
            System.err.println("Передайте только номер файла, программа выполняется с помощью переменной окружения $LAB5_FILE_(номер)");
            System.exit(1);
        }

        String envName;
        envName = envMap.getOrDefault(args[0], "");

        FileManager fileManager = new FileManager(envName);
        CollectionManager collectionManager = new CollectionManager(fileManager);
        RequestHandler requestHandler = new RequestHandler(collectionManager);
        ServerNetworkManager networkManager = new ServerNetworkManager(port, requestHandler);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[INFO] Сервер завершает работу. Сохранение коллекции");
            collectionManager.save();
        }));

        System.out.println("[INFO] Сервер успешно инициализирован. Ожидание подключений на порту " + port);
        networkManager.start();
    }
}