package com.wutao.dormItem.domain.enums;

public enum Major {
    /**
     * 软件工程 / Программная инженерия
     */
    SOFTWARE_ENGINEERING("Программная инженерия", 0),

    /**
     * 计算机科学 / Информатика
     */
    COMPUTER_SCIENCE("Информатика", 1),

    /**
     * 信息系统 / Информационные системы
     */
    INFORMATION_SYSTEMS("Информационные системы", 2),

    /**
     * 应用数学 / Прикладная математика
     */
    APPLIED_MATHEMATICS("Прикладная математика", 3),

    /**
     * 其他 / Другое
     */
    OTHER("Другое", 99);

    private final String text;
    private final Integer value;

    Major(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static Major getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (Major anEnum : Major.values()) {
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