package com.danya.lab5.client.io;

import com.danya.lab5.client.exceptions.RecurtionException;

import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class InputManager {
    private final Stack<Scanner> scannerStack = new Stack<>();
    private final Stack<String> pathStack = new Stack<>();
    private boolean scriptMode = false;

    public InputManager(Scanner consoleScanner) {
        this.scannerStack.push(consoleScanner);
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    public String nextLine() {
        if (scannerStack.isEmpty()) {
            return null;
        }

        Scanner currentScanner = scannerStack.peek();
        if (currentScanner.hasNextLine()) {
            return currentScanner.nextLine().trim();
        } else {
            if (scannerStack.size() > 1) {
                scannerStack.pop();
                pathStack.pop();
                return nextLine();
            }
        }
        return null;
    }

    public void loadFile(String path) throws FileNotFoundException, RecurtionException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не обнаружен");
        }
        String absolutePath = file.getAbsolutePath();
        if (pathStack.contains(absolutePath)) {
            throw new RecurtionException("В файле обнаружена рекурссия");
        }

        scannerStack.push(new Scanner(file));
        pathStack.push(absolutePath);
    }

    public void closeScript() {
        if (scannerStack.size() >1) {
            scannerStack.pop();
            pathStack.pop();
        }
        if (isInteractive()) {
            scriptMode = false;
        }
    }

    public boolean isInteractive() {
        return scannerStack.size() == 1;
    }
}
