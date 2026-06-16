package com.wutao.dormItem.domain.enums;

public enum ItemCategory {
    // 床上用品
    QUILT("Одеяло", 1),
    PILLOW("Подушка", 2),
    BED_SHEET("Простыня", 3),
    // 家具
    DESK("Стол", 4),
    CHAIR("Стул", 5),
    // 电器
    LAMP("Настольная лампа", 6),
    CHARGER("Зарядное устройство", 7),
    // 清洁用品
    BROOM("Метла", 8),
    DUSTPAN("Совок", 9),
    // 其他
    OTHER("Другое", 0);

    private final String text;
    private final Integer value;

    ItemCategory(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static ItemCategory getEnumByValue(Integer value) {
        if (value == null) return null;
        for (ItemCategory c : ItemCategory.values()) {
            if (c.value.equals(value)) return c;
        }
        return null;
    }

    public String getText() { return text; }
    public Integer getValue() { return value; }
}