package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.model.Room;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class RoomMapper implements RowMapper<Room> {

    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        Room room = new Room();

        room.setId((UUID) rs.getObject("id"));
        room.setDormitoryId((UUID) rs.getObject("dormitory_id"));
        room.setRoomNumber(rs.getString("room_number"));

        Object floorNoObj = rs.getObject("floor_no");
        if (floorNoObj == null) {
            room.setFloorNo(null);
        } else {
            room.setFloorNo(rs.getInt("floor_no"));
        }

        Object capacityObj = rs.getObject("capacity");
        if (capacityObj == null) {
            room.setCapacity(null);
        } else {
            room.setCapacity(rs.getInt("capacity"));
        }

        return room;
    }
}