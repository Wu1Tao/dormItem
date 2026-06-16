package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.enums.IssueStatus;
import com.wutao.dormItem.domain.model.IssueRecord;
import com.wutao.dormItem.repository.IssueRecordRepository;
import com.wutao.dormItem.repository.postgres.mapper.IssueRecordMapper;
import com.wutao.dormItem.repository.postgres.sql.IssueRecordSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresIssueRecordRepository implements IssueRecordRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final IssueRecordMapper issueRecordMapper;

    public PostgresIssueRecordRepository(NamedParameterJdbcTemplate jdbcTemplate, IssueRecordMapper issueRecordMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.issueRecordMapper = issueRecordMapper;
    }

    @Override
    public IssueRecord findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(IssueRecordSql.FIND_BY_ID, params, issueRecordMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<IssueRecord> findByStudentId(UUID studentId) {
        MapSqlParameterSource params = new MapSqlParameterSource("studentId", studentId);
        return jdbcTemplate.query(IssueRecordSql.FIND_BY_STUDENT_ID, params, issueRecordMapper);
    }

    @Override
    public List<IssueRecord> findByStatusNot(IssueStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource("status", status.getValue());
        return jdbcTemplate.query(IssueRecordSql.FIND_BY_STATUS_NOT, params, issueRecordMapper);
    }

    @Override
    public void save(IssueRecord record) {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", record.getId())
                .addValue("studentId", record.getStudentId())
                .addValue("itemId", record.getItemId())
                .addValue("userId", record.getUserId())
                .addValue("issueDate", toTimestamp(record.getIssueDate()))
                .addValue("expectedReturnDate", toTimestamp(record.getExpectedReturnDate()))
                .addValue("quantity", record.getQuantity())
                .addValue("returnDate", toTimestamp(record.getReturnDate()))
                .addValue("status", record.getStatus().getValue());
        jdbcTemplate.update(IssueRecordSql.INSERT, params);
    }

    @Override
    public void updateStatus(UUID id, IssueStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("newStatus", status.getValue())
                .addValue("id", id);
        jdbcTemplate.update(IssueRecordSql.UPDATE_STATUS, params);
    }

    private Timestamp toTimestamp(java.util.Date date) {
        return date != null ? new Timestamp(date.getTime()) : null;
    }
}