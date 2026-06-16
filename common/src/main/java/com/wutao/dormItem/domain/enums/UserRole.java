package com.wutao.dormItem.domain.enums;

public enum UserRole {
    VIEWER("Зритель", 0),
    /**
     * 宿管 / Администратор общежития
     */
    USER("Администратор общежития", 1),
    /**
     * 系统管理员 / Системный администратор
     */
    ADMIN("Системный администратор", 2);

    private final String text;

    private final Integer value;

    UserRole(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Получить enum по value
     *
     * @param value значение enum
     * @return enum или null
     */
    public static UserRole getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (UserRole anEnum : UserRole.values()) {
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