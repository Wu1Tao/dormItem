package com.wutao.dormItem.domain.enums;

public enum IssueStatus {
    /**
     * 已发放 / Выдано
     */
    ISSUED("Выдано", 0),
    /**
     * 部分归还 / Частично возвращено
     */
    PARTIALLY_RETURNED("Частично возвращено", 1),
    /**
     * 已归还 / Возвращено
     */
    RETURNED("Возвращено", 2);

    private final String text;

    private final Integer value;

    IssueStatus(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Получить enum по value
     *
     * @param value значение enum
     * @return enum или null
     */
    public static IssueStatus getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (IssueStatus anEnum : IssueStatus.values()) {
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