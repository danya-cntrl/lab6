package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class MinByCoordinatesCommand extends Command {
    private final CollectionManager collectionManager;

    public MinByCoordinatesCommand(CollectionManager collectionManager) {
        super("min_by_coordinates", "вывести любой объект из коллекции, значение поля coordinates которого является минимальным");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup minGroup = collectionManager.minByCoordinates();
        if (minGroup == null) {
            return new Response(true, "Коллекция пуста.");
        }
        return new Response(true, "Группа с минимальными координатами:\n" + minGroup);
    }
}