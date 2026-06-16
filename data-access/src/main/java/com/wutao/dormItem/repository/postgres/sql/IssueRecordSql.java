package com.wutao.dormItem.repository.postgres.sql;

public final class IssueRecordSql {
    private IssueRecordSql() {}

    public static final String FIND_BY_ID = 
        "SELECT id, student_id, item_id, user_id, issue_date, " +
        "       quantity, return_date, status " +
        "FROM issue_record " +
        "WHERE id = :id";

    public static final String FIND_BY_STUDENT_ID = 
        "SELECT id, student_id, item_id, user_id, issue_date, " +
        "       quantity, return_date, status " +
        "FROM issue_record " +
        "WHERE student_id = :studentId " +
        "ORDER BY issue_date DESC";

    public static final String FIND_BY_STATUS_NOT = 
        "SELECT id, student_id, item_id, user_id, issue_date, " +
        "       quantity, return_date, status " +
        "FROM issue_record " +
        "WHERE status <> :status " +
        "ORDER BY issue_date ASC";

    public static final String INSERT = 
        "INSERT INTO issue_record ( " +
        "    id, student_id, item_id, user_id, issue_date, " +
        "    quantity, return_date, status " +
        ") VALUES ( " +
        "    :id, :studentId, :itemId, :userId, :issueDate, " +
        "    :quantity, :returnDate, :status " +
        ")";

    public static final String UPDATE_STATUS = 
        "UPDATE issue_record " +
        "SET status = :newStatus, " +
        "    return_date = CASE " +
        "        WHEN :newStatus = 2 THEN CURRENT_TIMESTAMP " +
        "        ELSE return_date " +
        "    END " +
        "WHERE id = :id";
}