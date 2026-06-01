package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;

public class MaxByStudentsCountCommand extends Command {
    private final CollectionManager collectionManager;

    public MaxByStudentsCountCommand(CollectionManager collectionManager) {
        super("max_by_students_count", "вывести любой объект из коллекции, значение поля studentsCount которого является максимальным");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        StudyGroup maxGroup = collectionManager.maxByStudentCount();
        if (maxGroup == null) {
            return new Response(true, "Коллекция пуста.");
        }
        return new Response(true, "Группа с максимальным количеством студентов:\n" + maxGroup);
    }
}