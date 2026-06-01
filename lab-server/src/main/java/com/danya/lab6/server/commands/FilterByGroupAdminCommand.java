package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;
import com.danya.lab6.common.models.StudyGroup;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByGroupAdminCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterByGroupAdminCommand(CollectionManager collectionManager) {
        super("filter_by_group_admin", "вывести элементы, значение поля groupAdmin которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String adminName = (String) request.getArgument();
        if (adminName == null || adminName.trim().isEmpty()) {
            return new Response(false, "Ошибка: Не указано имя админа.");
        }
        List<StudyGroup> filtered = collectionManager.filterByGroupAdmin(adminName);
        if (filtered.isEmpty()) {
            return new Response(true, "Группы с заданным администратором не найдены.");
        }
        String result = filtered.stream()
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
        return new Response(true, result);
    }
}