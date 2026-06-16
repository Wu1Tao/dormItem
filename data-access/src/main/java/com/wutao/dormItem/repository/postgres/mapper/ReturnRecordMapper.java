package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.enums.ItemCondition;
import com.wutao.dormItem.domain.model.ReturnRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Component
public class ReturnRecordMapper implements RowMapper<ReturnRecord> {

    @Override
    public ReturnRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReturnRecord record = new ReturnRecord();
        record.setId(rs.getObject("id", UUID.class));
        record.setIssueRecordId(rs.getObject("issue_record_id", UUID.class));
        record.setUserId(rs.getObject("user_id", UUID.class));
        record.setReturnDate(toDate(rs.getTimestamp("return_date")));
        record.setQuantity(rs.getInt("quantity"));
        record.setCondition(ItemCondition.getEnumByValue(rs.getInt("condition")));
        return record;
    }

    private Date toDate(Timestamp timestamp) {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }
}