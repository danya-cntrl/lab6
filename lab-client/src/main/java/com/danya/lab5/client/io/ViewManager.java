package com.danya.lab5.client.io;

public class ViewManager {

    public void print(Object o) {
        System.out.print(o);
    }

    public void println(Object o) {
        System.out.println(o);
    }

    public void printError(Object o) {
        System.err.println("Ошибка: " + o);
    }
}
