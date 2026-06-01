package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryCommand extends Command {
    private final List<String> history;

    public HistoryCommand(List<String> history) {
        super("history", "вывести последние 11 команд (без их аргументов)");
        this.history = history;
    }

    @Override
    public Response execute(Request request) {
        if (history.isEmpty()) {
            return new Response(true, "История команд пуста.");
        }

        String result = history.stream()
                .collect(Collectors.joining("\n"));

        return new Response(true, "Последние команды:\n" + result);
    }
}