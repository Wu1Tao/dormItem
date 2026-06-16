package com.wutao.dormItem.repository.postgres.sql;

public final class UserSql {
    private UserSql() {}

    public static final String FIND_BY_ID = 
        "SELECT id, user_account, user_password, user_name, role, user_status, is_deleted " +
        "FROM users " +
        "WHERE id = :id AND is_deleted = false";

    public static final String FIND_BY_ACCOUNT = 
        "SELECT id, user_account, user_password, user_name, role, user_status, is_deleted " +
        "FROM users " +
        "WHERE user_account = :account AND is_deleted = false";

    public static final String FIND_ALL = 
        "SELECT id, user_account, user_password, user_name, role, user_status, is_deleted " +
        "FROM users " +
        "WHERE is_deleted = false " +
        "ORDER BY user_name";

    public static final String INSERT = 
        "INSERT INTO users ( " +
        "    id, user_account, user_password, user_name, role, user_status, is_deleted " +
        ") VALUES ( " +
        "    :id, :userAccount, :userPassword, :userName, :role, :userStatus, :isDeleted " +
        ")";

    public static final String UPDATE_STATUS = 
        "UPDATE users " +
        "SET user_status = :userStatus " +
        "WHERE id = :id AND is_deleted = false";
}