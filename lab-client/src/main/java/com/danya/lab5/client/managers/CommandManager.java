package com.danya.lab5.client.managers;

import com.danya.lab5.client.io.InputManager;
import com.danya.lab5.client.io.StudyGroupAsker;
import com.danya.lab5.client.utils.CommandMetadata;
import com.danya.lab5.common.models.StudyGroup;
import com.danya.lab5.common.protocol.Request;
import com.danya.lab5.common.protocol.Response;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, CommandMetadata> commands = new HashMap<>();
    private final InputManager inputManager;
    private final ClientNetworkManager networkManager;
    private final StudyGroupAsker asker;

    public CommandManager (InputManager inputManager, ClientNetworkManager networkManager, StudyGroupAsker asker) {
        this.inputManager = inputManager;
        this.networkManager = networkManager;
        this.asker = asker;

        commands.put("help", new CommandMetadata("help", false, false));
        commands.put("info", new CommandMetadata("info", false, false));
        commands.put("show", new CommandMetadata("show", false, false));
        commands.put("clear", new CommandMetadata("clear", false, false));
        commands.put("history", new CommandMetadata("history", false, false));

        commands.put("add", new CommandMetadata("add", false, true));
        commands.put("add_if_max", new CommandMetadata("add_if_max", false, true));
        commands.put("remove_lower", new CommandMetadata("remove_lower", false, true));
        commands.put("update", new CommandMetadata("update", false, true));

        commands.put("remove_by_id", new CommandMetadata("remove_by_id", true, false));
        commands.put("filter_by_group_admin", new CommandMetadata("filter_by_group_admin", true, false));

        commands.put("exit", new CommandMetadata("exit", false, false));
        commands.put("execute_script", new CommandMetadata("execute_script", true, false));
    }

    public void executeLab(String input) {
        String[] tokens = input.trim().split("\\s+", 2);
        String cmdName = tokens[0].toLowerCase();
        String arg = (tokens.length > 1 ? tokens[1].trim() : "");


        CommandMetadata command = commands.get(cmdName);
        if (command == null) {
            System.out.println("КОманда не найдена");
            return;
        }
        if (command.isRequiresStringArg()  && arg.isEmpty()) {
            System.out.println("Команда требует аргумент");
            return;
        }
        if (command.isRequiresStringArg()  && !arg.isEmpty()) {
            System.out.println("Команде не нужен аргумент");
            return;
        }

        Request request;

        if (command.isRequiresObjectArg()) {
            if (!inputManager.isInteractive()) {
                System.out.println("Чтение объекта StudyGroup из скрипта...");
            } else {
                System.out.println("Введите данные для объекта StudyGroup:");
            }

            StudyGroup group = asker.ask();
            if (group == null) {
                System.out.println("Ошибка: Не удалось считать объект. Команда отменена.");
                return;
            }

            request = new Request(cmdName, group);
        }
        else if (command.isRequiresStringArg()) {
            request = new Request(cmdName, arg);
        }
        else {
            request = new Request(cmdName);
        }

        Response response = networkManager.sendRequest(request);
        if (response.isSuccess()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Ошибка при выполнении на сервере: " + response.getMessage());
        }
    }

    public void letsGo() {
        while (true) {
            String line = inputManager.nextLine();
            if (line == null) {
                if (inputManager.isInteractive()) {
                    break;
                }
                else {
                    inputManager.setScriptMode(false);
                    continue;
                }
            }

            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                executeLab(line);
            } catch (RuntimeException e) {
                System.out.println("Критическая ошибка при чтении скрипта: " + e.getMessage());
                System.out.println("Выполнение текущего скрипта прервано.");
                inputManager.closeScript();
            }
        }
    }
}
