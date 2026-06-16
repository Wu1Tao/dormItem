package com.wutao.dormItem.repository.postgres.sql;

public final class DormitorySql {
    private DormitorySql() {
    }

    public static final String INSERT = ""
            + "INSERT INTO dormitory (id, name, address) "
            + "VALUES (:id, :name, :address)";

    public static final String FIND_BY_ID = ""
            + "SELECT id, name, address "
            + "FROM dormitory "
            + "WHERE id = :id";

    public static final String FIND_ALL = ""
            + "SELECT id, name, address "
            + "FROM dormitory "
            + "ORDER BY name";

}