package com.danya.lab6.server.managers;

import com.danya.lab6.common.models.StudyGroup;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class CollectionManager {
    private HashSet<StudyGroup> collection;
    private final LocalDate lastInitTime;
    private final FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.lastInitTime = LocalDate.now();
        this.collection = fileManager.readCollection();
        if (this.collection == null) {
            this.collection = new HashSet<>();
        }

        StudyGroup.updateNextId(this.collection);
    }

    public void save() {
        fileManager.writeCollection(collection);
    }

    public void add(StudyGroup group) {
        collection.add(group);
    }

    public void clear() {
        collection.clear();
    }

    public String show() {
        if (collection.isEmpty()) return "Коллекция пуста";

        return collection.stream()
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
    }

    public String info() {
        return "Тип: " + collection.getClass().getSimpleName() +
                "\nДата инициализации: " + lastInitTime +
                "\nКоличество элементов: " + collection.size();
    }

    public boolean removeById(int id) {
        return collection.removeIf(group -> group.getId() == id);
    }

    public StudyGroup getById(int id) {
        return collection.stream()
                .filter(group -> group.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean addIfMax(StudyGroup group) {
        boolean isMax = collection.stream()
                .max(StudyGroup::compareTo)
                .map(maxElement -> group.compareTo(maxElement) > 0)
                .orElse(true);
        if (isMax) {
            add(group);
            return true;
        }
        return false;
    }

    public boolean removeLower(StudyGroup group) {
        int originalSize = collection.size();
        collection.removeIf(element -> element.compareTo(group) < 0);
        return collection.size() < originalSize;
    }

    public StudyGroup maxByStudentCount() {
        return collection.stream()
                .max(Comparator.comparingLong(StudyGroup::getStudentsCount))
                .orElse(null);
    }

    public StudyGroup minByCoordinates() {
        return collection.stream()
                .min(Comparator.comparing(StudyGroup::getCoordinates))
                .orElse(null);
    }

    public List<StudyGroup> filterByGroupAdmin(String adminName) {
        if (adminName == null || adminName.trim().isEmpty()) return List.of();

        return collection.stream()
                .filter(group -> group.getGroupAdmin() != null)
                .filter(group -> adminName.equals(group.getGroupAdmin().getName()))
                .collect(Collectors.toList());
    }

    public HashSet<StudyGroup> getCollection() { return collection; }
}