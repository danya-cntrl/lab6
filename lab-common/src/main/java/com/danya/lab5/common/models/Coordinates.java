package com.danya.lab5.common.models;

import java.util.Objects;

public class Coordinates implements Comparable<Coordinates>{
    private Float x; //Поле не может быть null
    private int y; //Максимальное значение поля: 639

    public Coordinates(Float x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Coordinates other) {
        return this.getDistance().compareTo(other.getDistance());
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Coordinates[\n  x = " + x + ",\n  y = " + y + "\n ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
