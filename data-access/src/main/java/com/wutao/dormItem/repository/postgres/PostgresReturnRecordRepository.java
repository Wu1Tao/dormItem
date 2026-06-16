package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.model.ReturnRecord;
import com.wutao.dormItem.repository.ReturnRecordRepository;
import com.wutao.dormItem.repository.postgres.mapper.ReturnRecordMapper;
import com.wutao.dormItem.repository.postgres.sql.ReturnRecordSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresReturnRecordRepository implements ReturnRecordRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ReturnRecordMapper returnRecordMapper;

    public PostgresReturnRecordRepository(NamedParameterJdbcTemplate jdbcTemplate, ReturnRecordMapper returnRecordMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.returnRecordMapper = returnRecordMapper;
    }

    @Override
    public ReturnRecord findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(ReturnRecordSql.FIND_BY_ID, params, returnRecordMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(ReturnRecord record) {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", record.getId())
                .addValue("issueRecordId", record.getIssueRecordId())
                .addValue("userId", record.getUserId())
                .addValue("returnDate", toTimestamp(record.getReturnDate()))
                .addValue("quantity", record.getQuantity())
                .addValue("condition", record.getCondition().getValue());
        jdbcTemplate.update(ReturnRecordSql.INSERT, params);
    }

    @Override
    public int sumReturnedQuantityByIssueId(UUID issueRecordId) {
        MapSqlParameterSource params = new MapSqlParameterSource("issueRecordId", issueRecordId);
        Integer result = jdbcTemplate.queryForObject(ReturnRecordSql.SUM_RETURNED_QUANTITY_BY_ISSUE_ID, params, Integer.class);
        return result != null ? result : 0;
    }

    private Timestamp toTimestamp(java.util.Date date) {
        return date != null ? new Timestamp(date.getTime()) : null;
    }
}