package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.model.Room;
import com.wutao.dormItem.repository.RoomRepository;
import com.wutao.dormItem.repository.postgres.mapper.RoomMapper;
import com.wutao.dormItem.repository.postgres.sql.RoomSql;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PostgresRoomRepository implements RoomRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoomMapper roomMapper;

    public PostgresRoomRepository(NamedParameterJdbcTemplate jdbcTemplate, RoomMapper roomMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomMapper = roomMapper;
    }

    @Override
    public Room findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        try {
            return jdbcTemplate.queryForObject(RoomSql.FIND_BY_ID, params, roomMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query(
                RoomSql.FIND_ALL,
                roomMapper
        );
    }

    @Override
    public List<Room> findByDormitoryId(UUID dormitoryId) {
        MapSqlParameterSource params = new MapSqlParameterSource("dormitoryId", dormitoryId);
        return jdbcTemplate.query(RoomSql.FIND_BY_DORMITORY_ID, params, roomMapper);
    }

}