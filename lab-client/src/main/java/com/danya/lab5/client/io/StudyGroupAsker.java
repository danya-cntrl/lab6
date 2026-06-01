package com.danya.lab5.client.io;

import com.danya.lab5.client.utils.Validator;
import com.danya.lab5.common.models.Coordinates;
import com.danya.lab5.common.models.Location;
import com.danya.lab5.common.models.Person;
import com.danya.lab5.common.models.StudyGroup;
import com.danya.lab5.common.models.enums.FormOfEducation;
import com.danya.lab5.common.models.enums.Semester;

import java.util.Arrays;

public class StudyGroupAsker {
    private final InputManager inputManager;
    private final ViewManager view = new ViewManager();
    private final Validator validator = new Validator();

    public StudyGroupAsker(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public StudyGroup ask() {
        if (!inputManager.isScriptMode()) {
            view.println("Ввод данных группы");
        }

        String name = askName();
        Coordinates coords = askCoordinates();
        Long studentsCount = askStudentCount();
        FormOfEducation form = askFormOfEducation();
        Semester sem = askSemester();
        Person admin = askGroupAdmin();

        return new StudyGroup(name, coords, studentsCount, form, sem, admin);
    }

    private String askName() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите название группы: ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);
            line = line.trim();

            if (validator.validateName(line)) {
                return line;
            } else {
                if (inputManager.isScriptMode()) {
                    throw new RuntimeException("Ошибка в скрипте: невалидное имя '" + line + "'");
                }
                view.println("Ошибка: Имя не может быть пустым/");
            }
        }
    }


    private Long askStudentCount() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите кол-во студентов: ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            line = line.trim();

            if (line.equalsIgnoreCase("exit")) System.exit(0);

            try {
                long count = Long.parseLong(line);

                if (validator.validateStudentsCount(count)) {
                    return count;
                } else {
                    if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка в скрипте: studentsCount <= 0");
                    view.println("Ошибка: Количество студентов должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode()) throw new RuntimeException("В скрипте не число");
                view.println("Ошибка: Введите целое число");
            }
        }
    }

    private Semester askSemester() {
        while (true) {
            if (!inputManager.isScriptMode()) {
                view.println("Существующие семестры: " + Arrays.toString(Semester.values()));
                view.print("Введите семестр: ");
            }
            String line = inputManager.nextLine();
            if (line == null) return null;
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            if (validator.validateEnum(Semester.class, line)) {
                return Semester.valueOf(line.toUpperCase());
            } else {
                if (inputManager.isScriptMode()) {
                    throw new RuntimeException("Ошибка в скрипте: неверное значение семестра '" + line + "'");
                }
                view.println("Ошибка: Такого семестра нет в списке. Попробуйте еще раз.");
            }
        }
    }

    private FormOfEducation askFormOfEducation() {
        while (true) {
            if (!inputManager.isScriptMode()) {
                view.println("Формы обучения: " + Arrays.toString(FormOfEducation.values()));
                view.print("Введите форму обучения (или пустую строку для null): ");
            }

            String line = inputManager.nextLine();
            if (line == null) return null;
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            if (line.isEmpty()) {
                return null;
            }

            if (validator.validateEnum(FormOfEducation.class, line)) {
                return FormOfEducation.valueOf(line.toUpperCase());
            } else {
                if (inputManager.isScriptMode()) {
                    throw new RuntimeException("Ошибка в скрипте: неверная форма обучения '" + line + "'");
                }
                view.println("Ошибка: Такой формы обучения нет в списке.");
            }
        }
    }

    public Coordinates askCoordinates() {
        Float x = askX();
        if (x == null) return null;
        Integer y = askY();
        if (y == null) return null;
        return new Coordinates(x, y);
    }

    private Float askX() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите координату x (Float): ");
            String line = inputManager.nextLine();

            if (line == null) return null;

            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();
            try {
                Float x = Float.parseFloat(line);
                if (validator.validateX(x)) {
                    return x;
                } else {
                    if (inputManager.isScriptMode())
                        throw new RuntimeException("Ошибка в скрипте: x не прошел валидацию");
                    view.println("Ошибка: Координата x не соответствует требованиям");
                }
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode()) {
                    throw new RuntimeException("Ошибка в скрипте: x должен быть числом с плавающей точкой");
                }
                view.println("Ошибка: x должен быть числом с плавающей точкой");
            }
        }
    }

    private Integer askY() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите координату y: ");
            String line = inputManager.nextLine();

            if (line == null) return null;

            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();
            try {
                Integer y = Integer.parseInt(line);
                if (validator.validateY(y)) {
                    return y;
                } else {
                    if (inputManager.isScriptMode())
                        throw new RuntimeException("Ошибка в скрипте: y должен быть меньше 476");
                    view.println("Ошибка: y должен быть меньше 476");
                }
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка в скрипте: введено не число");
                view.println("Ошибка: Введено не число");
            }
        }
    }

    private Location askLocation() {
        Double x = askLocationX();
        if (x == null) return null;
        Double y = askLocationY();
        if (y == null) return null;
        String name = askLocationName();

        return new Location(x, y, name);
    }

    private Double askLocationX() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите Location.X (double): ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);
            line = line.trim();
            try {
                double x = Double.parseDouble(line);
                if (validator.validateXOfLocation(x)) return x;
                if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка X в скрипте");
                view.println("Ошибка: Некорректный X");
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode()) throw new RuntimeException("X - не число");
                view.println("Ошибка: Введите число double");
            }
        }
    }

    private Double askLocationY() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите Location.Y (Double): ");
            String line = inputManager.nextLine();

            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();

            try {
                Double y = Double.parseDouble(line);

                if (validator.validateYOfLocation(y)) {
                    return y;
                } else {
                    if (inputManager.isScriptMode())
                        throw new RuntimeException("Ошибка в скрипте: Location.Y не прошел валидацию");
                    view.println("Ошибка: Значение Y не удовлетворяет условиям");
                }
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode())
                    throw new RuntimeException("Ошибка в скрипте: Location.Y должен быть числом");
                view.println("Ошибка: Введите число (дробная часть через точку)");
            }
        }
    }

    private String askLocationName() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите название локации (пустая строка для null): ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();
            if (line.isEmpty()) return null;

            if (validator.validateNameOfLocation(line)) {
                return line;
            } else {
                if (inputManager.isScriptMode()) throw new RuntimeException("Имя локации не может быть пустым");
                view.println("Ошибка: Строка не может быть пустой");
            }
        }
    }

    public Person askGroupAdmin() {
        String name = askPersonName();
        if (name == null) return null;

        java.util.Date birthday = askBirthday();
        if (birthday == null) return null;

        Double height = askPersonHeight();
        if (height == null) return null;

        String passport = askPassportID();

        Location location = askLocation();

        return new Person(name, birthday, height, passport, location);
    }

    private String askPersonName() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите имя админа: ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);
            line = line.trim();

            if (validator.validateName(line)) {
                return line;
            } else {
                if (inputManager.isScriptMode()) {
                    throw new RuntimeException("Ошибка в скрипте: невалидное имя '" + line + "'");
                }
                view.println("Ошибка: Имя не может быть пустым!");
            }
        }
    }

    private Double askPersonHeight() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите рост (double > 0): ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);
            line = line.trim();
            try {
                double h = Double.parseDouble(line);
                if (validator.validateHeight(h)) return h;
                else {
                    if (inputManager.isScriptMode()) throw new RuntimeException("Рост должен быть > 0");
                    view.println("Ошибка: Рост должен быть больше 0");
                }
            } catch (NumberFormatException e) {
                if (inputManager.isScriptMode()) throw new RuntimeException("Рост - не число");
                view.println("Ошибка: Введите число double");
            }
        }
    }

    private String askPassportID() {
        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите паспортИД (пустая строка для null, min 4 символа): ");
            String line = inputManager.nextLine();
            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();
            if (line.isEmpty()) return null;

            if (validator.validatePassportID(line)) {
                return line;
            } else {
                if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка PassportID");
                view.println("Ошибка: Длина минимум 4 и не пустая");
            }
        }
    }

    private java.util.Date askBirthday() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        while (true) {
            if (!inputManager.isScriptMode()) view.print("Введите дату рождения (день.месяц.год): ");
            String line = inputManager.nextLine();

            if (line == null) return null;
            if (line.equalsIgnoreCase("exit")) System.exit(0);

            line = line.trim();
            try {
                java.util.Date date = dateFormat.parse(line);

                if (validator.validateBirthday(date)) {
                    return date;
                } else {
                    if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка в скрипте: некорректная дата");
                    view.println("Ошибка: Эта дата не подходит по правилам");
                }
            } catch (java.text.ParseException e) {
                if (inputManager.isScriptMode()) throw new RuntimeException("Ошибка в скрипте: неверный формат даты");
                view.println("Ошибка: Неверный формат. Используйте dd.MM.yyyy (например, 20.10.2005)");
            }
        }
    }
}
