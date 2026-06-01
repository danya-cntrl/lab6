package com.danya.lab6.common.models;

import com.danya.lab6.common.models.enums.FormOfEducation;
import com.danya.lab6.common.models.enums.Semester;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;


public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private static int nextId = 1;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле не может быть null

    @Override
    public int compareTo(StudyGroup other) {
        return this.studentsCount.compareTo(other.studentsCount);
    }

    public StudyGroup(String name, Coordinates coordinates, Long studentsCount, FormOfEducation formOfEducation, Semester semester, Person admin) {
        this.id = nextId;
        nextId++;
        this.name = name;
        this.coordinates = coordinates;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semester;
        this.groupAdmin = admin;
        this.creationDate = LocalDate.now();
    }

    public static void updateNextId(Collection<StudyGroup> collection) {
        int maxId = 0;
        for (StudyGroup group : collection) {
            if (group.getId() > maxId) {
                maxId = group.getId();
            }
        }
        nextId = maxId + 1;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCreationDate(LocalDate date) {
        this.creationDate = date;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Long getStudentsCount() {
        return studentsCount;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    @Override
    public String toString() {
        String sem = (semesterEnum == null ? "не указан" : semesterEnum.toString());
        String educateForm = (formOfEducation == null ? "не указан" : formOfEducation.toString());
        return "Group[\n id=" + id + ",\n name=" + name + ",\n students=" + studentsCount + ",\n " +
                coordinates.toString() + ",\n creationDate = " + creationDate + ",\n formOfEducation="+ educateForm +
                ",\n semesterEnum=" + sem + ",\n " + groupAdmin.toString() + "\n]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudyGroup that = (StudyGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
