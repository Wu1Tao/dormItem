package com.wutao.dormItem.domain.enums;

public enum UserStatus {
    /**
     * 正常 / Активен
     */
    ACTIVE("Активен", 0),

    /**
     * 禁用 / Отключён
     */
    DISABLED("Отключён", 1);

    private final String text;
    private final Integer value;

    UserStatus(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static UserStatus getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (UserStatus anEnum : UserStatus.values()) {
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
