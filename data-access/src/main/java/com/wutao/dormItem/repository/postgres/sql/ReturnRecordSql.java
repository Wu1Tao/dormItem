package com.wutao.dormItem.repository.postgres.sql;

public final class ReturnRecordSql {
    private ReturnRecordSql() {}

    public static final String FIND_BY_ID = 
        "SELECT id, issue_record_id, user_id, return_date, quantity, condition " +
        "FROM return_record " +
        "WHERE id = :id";

    public static final String INSERT = 
        "INSERT INTO return_record ( " +
        "    id, issue_record_id, user_id, return_date, quantity, condition " +
        ") VALUES ( " +
        "    :id, :issueRecordId, :userId, :returnDate, :quantity, :condition " +
        ")";

    public static final String SUM_RETURNED_QUANTITY_BY_ISSUE_ID = 
        "SELECT COALESCE(SUM(quantity), 0) " +
        "FROM return_record " +
        "WHERE issue_record_id = :issueRecordId";
}