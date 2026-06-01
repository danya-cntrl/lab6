package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный вывод все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String result = collectionManager.show();
        return new Response(true, result);
    }
}