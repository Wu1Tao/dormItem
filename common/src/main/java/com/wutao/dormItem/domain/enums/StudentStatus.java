package com.wutao.dormItem.domain.enums;

public enum StudentStatus {
    /**
     * 在住 / Проживает
     */
    ACTIVE("Проживает", 0),
    /**
     * 已毕业 / Выпускник
     */
    GRADUATED("Выпускник", 1),
    /**
     * 已搬出 / Выселен
     */
    MOVED_OUT("Выселен", 2);

    private final String text;

    private final Integer value;

    StudentStatus(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Получить enum по value
     *
     * @param value значение enum
     * @return enum или null
     */
    public static StudentStatus getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (StudentStatus anEnum : StudentStatus.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }

        return null;
    }

    public static boolean isValidValue(Integer value) {
        return getEnumByValue(value) != null;
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }
}