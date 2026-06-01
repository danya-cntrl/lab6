package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup newGroup = (StudyGroup) request.getArgument();

        if (newGroup != null) {
            collectionManager.add(newGroup);
            return new Response(true, "Группа добавлена.");
        } else {
            return new Response(false, "Ошибка: Сервер не получил объект группы.");
        }
    }
}