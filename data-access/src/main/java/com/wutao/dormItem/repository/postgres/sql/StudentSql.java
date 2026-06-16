package com.wutao.dormItem.repository.postgres.sql;

public final class StudentSql {
    private StudentSql() {}

    public static final String FIND_BY_ID = 
        "SELECT id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
        "FROM student " +
        "WHERE id = :id AND is_deleted = false";

    public static final String FIND_BY_STUDENT_NO = 
        "SELECT id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
        "FROM student " +
        "WHERE student_no = :studentNo AND is_deleted = false";

    public static final String FIND_ALL = 
        "SELECT id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
        "FROM student " +
        "WHERE is_deleted = false " +
        "ORDER BY student_no";

    public static final String INSERT = 
        "INSERT INTO student ( " +
        "    id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
        ") VALUES ( " +
        "    :id, :studentNo, :name, :gender, :major, :dormRoom, :status, :phone, :isDeleted " +
        ")";

    // StudentSql.java 中添加
    public static final String FIND_BY_NAME_CONTAINING =
            "SELECT id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
            "FROM student " +
            "WHERE name ILIKE :namePattern AND is_deleted = false " +
            "ORDER BY name";

    // StudentSql.java
    public static final String FIND_BY_STUDENT_NO_OR_NAME =
            "SELECT id, student_no, name, gender, major, dorm_room, status, phone, is_deleted " +
            "FROM student " +
            "WHERE (student_no = :keyword OR name ILIKE :keywordPattern) AND is_deleted = false " +
            "ORDER BY name";
}