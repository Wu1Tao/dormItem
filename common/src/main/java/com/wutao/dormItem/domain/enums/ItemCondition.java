package com.wutao.dormItem.domain.enums;

public enum ItemCondition {
    /**
     * 完好 / Хорошее состояние
     */
    GOOD("Хорошее состояние", 0),
    /**
     * 损坏 / Повреждено
     */
    DAMAGED("Повреждено", 1);

    private final String text;

    private final Integer value;

    ItemCondition(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Получить enum по value
     *
     * @param value значение enum
     * @return enum или null
     */
    public static ItemCondition getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (ItemCondition anEnum : ItemCondition.values()) {
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