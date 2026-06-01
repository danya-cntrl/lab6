package com.danya.lab5.common.models;

import java.util.Objects;

public class Location {
    private double x;
    private Double y; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле может быть null

    public Location(double x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Location[\n   x=" + x + ",\n   y=" + y + ",\n   name=" + name + "\n  ]";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(x, location.x) &&
                Objects.equals(y, location.y) &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }
}
