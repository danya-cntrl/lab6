package com.danya.lab6.server.commands;

import com.danya.lab6.common.protocol.Request;
import com.danya.lab6.common.protocol.Response;
import com.danya.lab6.server.managers.CollectionManager;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String info = collectionManager.info();
        return new Response(true, info);
    }
}