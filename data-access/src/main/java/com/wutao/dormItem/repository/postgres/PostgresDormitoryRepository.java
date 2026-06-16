package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.model.Dormitory;
import com.wutao.dormItem.repository.DormitoryRepository;
import com.wutao.dormItem.repository.postgres.mapper.DormitoryMapper;
import com.wutao.dormItem.repository.postgres.sql.DormitorySql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresDormitoryRepository implements DormitoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DormitoryMapper dormitoryMapper;

    public PostgresDormitoryRepository(NamedParameterJdbcTemplate jdbcTemplate, DormitoryMapper dormitoryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dormitoryMapper = dormitoryMapper;
    }

    @Override
    public List<Dormitory> findAll() {
        return jdbcTemplate.query(DormitorySql.FIND_ALL, dormitoryMapper);
    }

    @Override
    public Dormitory findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        try {
            return jdbcTemplate.queryForObject(DormitorySql.FIND_BY_ID, params, dormitoryMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}