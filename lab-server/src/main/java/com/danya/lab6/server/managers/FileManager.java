package com.danya.lab6.server.managers;

import com.danya.lab6.common.models.StudyGroup;
import com.danya.lab6.server.utils.LocalDateAdapter;
import com.danya.lab6.common.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class FileManager {
    private final String env;
    private final Gson gson;
    private final Validator validator = new Validator();


    public FileManager(String envVariableName) {
        this.env = envVariableName;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("dd.MM.yyyy")
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }


    public void writeCollection(Collection<StudyGroup> collection) {
        String filePath = System.getenv(env);

        if (filePath == null || filePath.isEmpty()) {
            System.err.println("[ERROR] Переменная " + env + " не установлена. Ничего не сохранено.");
            return;
        }

        if (!new File(filePath).isFile()) {
            System.err.println("[ERROR] Указанный путь является директорией. Ничего не сохранено");
            return;
        }

        if (!new File(filePath).canWrite()) {
            System.err.println("[ERROR] Нет прав записи в файл. Ничего не сохранено.");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8)) {

            String json = gson.toJson(collection);
            osw.write(json);
            osw.flush();
            System.out.println("[INFO] Коллекция успешно сохранена в файл.");

        } catch (IOException e) {
            System.err.println("[ERROR] При записи в файл " + e.getMessage());
        }
    }


    public HashSet<StudyGroup> readCollection() {
        String filePath = System.getenv(env);
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("[WARN] Переменная " + env + " не установлена. Запущена пустая коллекция");
            return new HashSet<>();
        }

        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("[ERROR] Файл не найден. Запущена пустая коллекция");
            return new HashSet<>();
        }

        if (!file.isFile()) {
            System.err.println("[ERROR] Указанный путь является директорией. Запущена пустая коллекция");
            return new HashSet<>();
        }

        if (!file.canRead()) {
            System.err.println("[ERROR] Нет прав, чтобы прочесть файл. Запущена пустая коллекция");
            return new HashSet<>();
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8)) {

            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = isr.read()) != -1) {
                builder.append((char) character);
            }

            String json = builder.toString().trim();
            if (json.isEmpty()) {
                System.err.println("[INFO] Файл пуст. Загружена пустая коллекция.");
                return new HashSet<>();
            }

            Type collectionType = new TypeToken<HashSet<StudyGroup>>() {
            }.getType();
            HashSet<StudyGroup> rawCollection = gson.fromJson(json, collectionType);
            if (rawCollection == null) {
                return new HashSet<>();
            }

            HashSet<StudyGroup> validCollection = new HashSet<>();
            for (StudyGroup group : rawCollection) {
                if (validator.validateStudyGroup(group)) {
                    validCollection.add(group);
                } else {
                    System.err.println("[ERROR] Группа с ID " + group.getId() + " содержит неправильные данные и будет пропущена.");
                }
            }
            return validCollection;

        } catch (IOException e) {
            System.err.println("[ERROR] При чтении файла: " + e.getMessage());
            return new HashSet<>();
        } catch (JsonSyntaxException e) {
            System.err.println("[ERROR] JSON в файле поврежден. Загружена пустая коллекция.");
            return new HashSet<>();
        }
    }
}
