package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "вывести справку по доступным командам");
    }

    @Override
    public Response execute(Request request) {
        String helpText = "add : добавить новый элемент в коллекцию\n" +
                "add_if_max : добавить новый элемент, если его значение превышает максимальное\n" +
                "clear : очистить коллекцию\n" +
                "filter_by_group_admin : вывести элементы, значение поля groupAdmin которых равно заданному\n" +
                "help : вывести справку по доступным командам\n" +
                "history : вывести последние 11 команд\n" +
                "info : вывести информацию о коллекции\n" +
                "max_by_students_count : вывести объект с максимальным значением поля studentsCount\n" +
                "min_by_coordinates : вывести объект с минимальным значением поля coordinates\n" +
                "remove_by_id : удалить элемент из коллекции по его id\n" +
                "remove_lower : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "show : вывести все элементы коллекции\n" +
                "update : обновить значение элемента коллекции, id которого равен заданному";
        return new Response(true, helpText);
    }
}