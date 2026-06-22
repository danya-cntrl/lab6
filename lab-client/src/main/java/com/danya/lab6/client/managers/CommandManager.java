package com.danya.lab6.client.managers;

import com.danya.lab6.client.exceptions.RecurtionException;
import com.danya.lab6.client.io.InputManager;
import com.danya.lab6.client.io.StudyGroupAsker;
import com.danya.lab6.client.utils.CommandMetadata;
import com.danya.lab6.common.models.StudyGroup;
import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;

import java.io.FileNotFoundException;
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
        commands.put("max_by_students_count", new CommandMetadata("max_by_students_count", false, false));
        commands.put("min_by_coordinates", new CommandMetadata("min_by_coordinates", false, false));


        commands.put("add", new CommandMetadata("add", false, true));
        commands.put("add_if_max", new CommandMetadata("add_if_max", false, true));
        commands.put("remove_lower", new CommandMetadata("remove_lower", false, true));
        commands.put("update", new CommandMetadata("update", false, true));

        commands.put("remove_by_id", new CommandMetadata("remove_by_id", true, false));
        commands.put("filter_by_group_admin", new CommandMetadata("filter_by_group_admin", true, false));

        commands.put("exit", new CommandMetadata("exit", false, false));
        commands.put("execute_script", new CommandMetadata("execute_script", true, false));
    }

    public void executeLab(String input) throws FileNotFoundException, RecurtionException {
        String[] tokens = input.trim().split("\\s+", 2);
        String cmdName = tokens[0].toLowerCase();
        String arg = (tokens.length > 1 ? tokens[1].trim() : "");


        CommandMetadata command = commands.get(cmdName);
        if (command == null) {
            System.out.println("Команда не найдена");
            return;
        }
        if (command.isRequiresStringArg()  && arg.isEmpty()) {
            System.out.println("КОманда требует аргумент");
            return;
        }
        if (!command.isRequiresStringArg()  && !arg.isEmpty()) {
            System.out.println("Команде не нужен аргумент");
            return;
        }

        if ("exit".equals(cmdName)) {
            System.out.println("Завершение работы клиентского приложения. Пока");
            System.exit(0);
        }

        if ("execute_script".equals(cmdName)) {
            System.out.println("Выполнение скрипта из файла: " + arg);
            inputManager.loadFile(arg);
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
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (RecurtionException e) {
                System.out.println(e.getMessage());
                inputManager.closeScript();
            }
        }
    }
}
