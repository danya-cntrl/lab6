package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup group = (StudyGroup) request.getArgument();
        if (group == null) {
            return new Response(false, "Ошибка: Сервер не получил объект группы.");
        }
        if (collectionManager.removeLower(group)) {
            return new Response(true, "Меньшие элементы успешно удалены.");
        }
        return new Response(true, "В коллекции не нашлось элементов меньше заданного.");
    }
}