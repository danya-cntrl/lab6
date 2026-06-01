package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup updatedGroup = (StudyGroup) request.getArgument();
        if (updatedGroup == null) {
            return new Response(false, "Ошибка: Сервер не получил объект группы.");
        }

        int id = updatedGroup.getId();
        if (collectionManager.getById(id) == null) {
            return new Response(false, "Элемент с таким ID не найден.");
        }

        collectionManager.removeById(id);
        collectionManager.add(updatedGroup);
        return new Response(true, "Элемент успешно обновлен.");
    }
}