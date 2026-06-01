package com.danya.lab6.client;

import com.danya.lab6.client.io.InputManager;
import com.danya.lab6.client.io.StudyGroupAsker;
import com.danya.lab6.client.managers.ClientNetworkManager;
import com.danya.lab6.client.managers.CommandManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        int servPort = 6067;

        System.out.println("Запущено клиентское приложение");
        System.out.println("хост = " + host + ", базовый порт = " + servPort);

        Scanner consoleScanner = new Scanner(System.in);
        InputManager inputManager = new InputManager(consoleScanner);
        StudyGroupAsker asker = new StudyGroupAsker(inputManager);

        ClientNetworkManager networkManager = new ClientNetworkManager(host, servPort);
        CommandManager commandManager = new CommandManager(inputManager, networkManager, asker);
        System.out.println("Программа запущена. Введите 'help' для получения справки");

        commandManager.letsGo();
    }
}
