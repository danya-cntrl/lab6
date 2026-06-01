package com.danya.lab6.common.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Date birthday; //Поле не может быть null
    private double height; //Значение поля должно быть больше 0
    private String passportID; //Длина строки должна быть не меньше 4, Строка не может быть пустой, Поле может быть null
    private Location location; //Поле не может быть null

    public Person(String name, Date birthday, double height, String passportID, Location location) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.passportID = passportID;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public double getHeight() {
        return height;
    }

    public String getPassportID() {
        return passportID;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "GroupAdmin[\n  name=" + name + ",\n  birthday=" + birthday +
                ",\n  height=" + height + ",\n  passportID=" + passportID +
                ",\n  " +
                "" + location +
                "\n ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        if (Double.compare(person.height, height) != 0) return false;
        if (!Objects.equals(name, person.name)) return false;
        if (!Objects.equals(birthday, person.birthday)) return false;
        if (!Objects.equals(passportID, person.passportID)) return false;
        return Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthday, height, passportID, location);
    }
}
