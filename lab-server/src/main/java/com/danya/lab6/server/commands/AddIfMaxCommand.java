package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class AddIfMaxCommand extends Command {
    private final CollectionManager collectionManager;

    public AddIfMaxCommand(CollectionManager collectionManager) {
        super("add_if_max", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup group = (StudyGroup) request.getArgument();
        if (group == null) {
            return new Response(false, "Ошибка: Сервер не получил объект группы.");
        }
        if (collectionManager.addIfMax(group)) {
            return new Response(true, "Группа успешно добавлена, так как она превышает максимальную!");
        }
        return new Response(true, "Группа не добавлена, так как она не превышает максимальный элемент.");
    }
}