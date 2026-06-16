package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.enums.IssueStatus;
import com.wutao.dormItem.domain.model.IssueRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Component
public class IssueRecordMapper implements RowMapper<IssueRecord> {

    @Override
    public IssueRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        IssueRecord record = new IssueRecord();
        record.setId(rs.getObject("id", UUID.class));
        record.setStudentId(rs.getObject("student_id", UUID.class));
        record.setItemId(rs.getObject("item_id", UUID.class));
        record.setUserId(rs.getObject("user_id", UUID.class));
        record.setIssueDate(toDate(rs.getTimestamp("issue_date")));
//        record.setExpectedReturnDate(toDate(rs.getTimestamp("expected_return_date")));
        record.setQuantity(rs.getInt("quantity"));
        record.setReturnDate(toDate(rs.getTimestamp("return_date")));
        record.setStatus(IssueStatus.getEnumByValue(rs.getInt("status")));
        return record;
    }

    private Date toDate(Timestamp timestamp) {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }
}