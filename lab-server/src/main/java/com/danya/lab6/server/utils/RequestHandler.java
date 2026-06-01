package com.danya.lab6.server.utils;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.commands.*;
import com.danya.lab6.server.managers.CollectionManager;

import java.util.*;


public class RequestHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final List<String> history = new ArrayList<>();
    private static final int MAX_HISTORY = 11;


    public RequestHandler(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        registerCommands();
    }


    private void registerCommands() {
        commands.put("add", new AddCommand(collectionManager));
        commands.put("add_if_max", new AddIfMaxCommand(collectionManager));
        commands.put("clear", new ClearCommand(collectionManager));
        commands.put("filter_by_group_admin", new FilterByGroupAdminCommand(collectionManager));
        commands.put("help", new HelpCommand());
        commands.put("history", new HistoryCommand(history));
        commands.put("info", new InfoCommand(collectionManager));
        commands.put("max_by_students_count", new MaxByStudentsCountCommand(collectionManager));
        commands.put("min_by_coordinates", new MinByCoordinatesCommand(collectionManager));
        commands.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commands.put("remove_lower", new RemoveLowerCommand(collectionManager));
        commands.put("show", new ShowCommand(collectionManager));
        commands.put("update", new UpdateCommand(collectionManager));
    }


    public Response handle(Request request) {
        String commandName = request.getCommandName();
        Command command = commands.get(commandName);

        if (command == null) {
            return new Response(false, "Ошибка: Команда '" + commandName + "' не поддерживается сервером.");
        }

        addToHistotyList(commandName);

        try {
            return command.execute(request);
        } catch (Exception e) {
            System.err.println("[ERROR] Ошибка при выполнении команды " + commandName + ": " + e.getMessage());
            return new Response(false, "Критическая ошибка на сервере при выполнении команды: " + e.getMessage());
        }
    }

    private void addToHistotyList(String cmdName) {
        history.add(cmdName);
        if (history.size() > MAX_HISTORY) {
            history.remove(0);
        }
    }
}