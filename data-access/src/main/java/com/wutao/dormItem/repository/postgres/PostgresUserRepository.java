package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;
import com.wutao.dormItem.repository.UserRepository;
import com.wutao.dormItem.repository.postgres.mapper.UserMapper;
import com.wutao.dormItem.repository.postgres.sql.UserSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public PostgresUserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public Users findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(UserSql.FIND_BY_ID, params, userMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Users findByAccount(String account) {
        MapSqlParameterSource params = new MapSqlParameterSource("account", account);
        try {
            return jdbcTemplate.queryForObject(UserSql.FIND_BY_ACCOUNT, params, userMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(Users user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("userAccount", user.getUserAccount())
                .addValue("userPassword", user.getUserPassword())
                .addValue("userName", user.getUserName())
                .addValue("role", user.getRole().getValue())
                .addValue("userStatus", user.getUserStatus().getValue())
                .addValue("isDeleted", Boolean.TRUE.equals(user.getIsDeleted()));
        jdbcTemplate.update(UserSql.INSERT, params);
    }

    @Override
    public List<Users> findAll() {
        return jdbcTemplate.query(UserSql.FIND_ALL, userMapper);
    }

    @Override
    public void updateStatus(UUID id, UserStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userStatus", status.getValue())
                .addValue("id", id);
        jdbcTemplate.update(UserSql.UPDATE_STATUS, params);
    }
}