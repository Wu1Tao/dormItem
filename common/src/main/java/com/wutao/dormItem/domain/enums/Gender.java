package com.wutao.dormItem.domain.enums;

public enum Gender {
    FEMALE("Женский", 0),
    MALE("Мужской", 1),
    UNKNOWN("Неизвестно", 2);

    private final String text;
    private final Integer value;

    Gender(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static Gender getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
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