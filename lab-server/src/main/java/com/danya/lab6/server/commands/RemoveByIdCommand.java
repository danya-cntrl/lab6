package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Object arg = request.getArgument();
        if (arg == null) {
            return new Response(false, "Ошибка: Не указан ID.");
        }
        int id;
        if (arg instanceof Integer) {
            id = (Integer) arg;
        } else {
            try {
                id = Integer.parseInt(arg.toString());
            } catch (NumberFormatException e) {
                return new Response(false, "Ошибка: ID должен быть числом.");
            }
        }
        if (collectionManager.removeById(id)) {
            return new Response(true, "Элемент успешно удален.");
        }
        return new Response(false, "Элемент с таким ID не найден.");
    }
}