package com.danya.lab5.client.utils;

import com.danya.lab5.common.models.Coordinates;
import com.danya.lab5.common.models.Location;
import com.danya.lab5.common.models.Person;
import com.danya.lab5.common.models.StudyGroup;

public class Validator {

    public boolean validateStudyGroup(StudyGroup group) {
        if (group == null) return false;

        return validateName(group.getName()) &&
                validateCoordinates(group.getCoordinates()) &&
                validateStudentsCount(group.getStudentsCount()) &&
                validatePerson(group.getGroupAdmin());
    }

    public boolean validateCoordinates(Coordinates coords) {
        if (coords == null) return false;
        return validateX(coords.getX()) && validateY(coords.getY());
    }

    public boolean validatePerson(Person person) {
        if (person == null) return false;
        return validatePersonName(person.getName()) &&
                validateBirthday(person.getBirthday()) &&
                validateHeight(person.getHeight()) &&
                validatePassportID(person.getPassportID()) &&
                 validateLocation(person.getLocation());
    }

    public boolean validateLocation(Location loc) {
        if (loc == null) return false;
        return validateXOfLocation(loc.getX()) &&
                validateYOfLocation(loc.getY()) &&
                validateNameOfLocation(loc.getName());
    }

    public boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateX(Float x) {
        return x != null;
    }

    public boolean validateY(int y)  {
        return (y < 476);
    }

    public boolean validateStudentsCount(Long count) {
        if (count == null || count <= 0) {
            return false;
        }
        return true;
    }

    public boolean validateEnum(Class<? extends Enum<?>> enumClass, String fieldName)  {
        if (fieldName == null) {
            return false;
        }
        if (enumClass.getSimpleName().equals("FormOfEducation") && fieldName.trim().isEmpty()) {
            return true;
        }

        Enum<?>[] values = enumClass.getEnumConstants();
        for (Enum<?> v : values) {
            if (v.name().equals(fieldName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean validatePersonName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateBirthday(java.util.Date birthday) {
        return birthday != null;
    }

    public boolean validateHeight(double height) {
        return height > 0;
    }

    public boolean validatePassportID(String passportID) {
        return passportID.length() >= 4;
    }

    public boolean validateXOfLocation(double x) {
        return true;
    }

    public boolean validateYOfLocation(Double y) {
        return y != null;
    }

        public boolean validateNameOfLocation(String name) {
            return name == null || !(name.trim().isEmpty());
        }

}