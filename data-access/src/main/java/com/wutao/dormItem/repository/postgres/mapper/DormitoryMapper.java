package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.model.Dormitory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class DormitoryMapper implements RowMapper<Dormitory> {

    @Override
    public Dormitory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dormitory dormitory = new Dormitory();
        dormitory.setId((UUID) rs.getObject("id"));
        dormitory.setName(rs.getString("name"));
        dormitory.setAddress(rs.getString("address"));
        return dormitory;
    }
}