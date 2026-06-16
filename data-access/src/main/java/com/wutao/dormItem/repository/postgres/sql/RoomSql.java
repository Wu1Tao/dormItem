package com.wutao.dormItem.repository.postgres.sql;

public final class RoomSql {
    private RoomSql() {
    }

    public static final String INSERT = ""
            + "INSERT INTO room (id, dormitory_id, room_number, floor_no, capacity) "
            + "VALUES (:id, :dormitoryId, :roomNumber, :floorNo, :capacity)";

    public static final String FIND_BY_ID = ""
            + "SELECT id, dormitory_id, room_number, floor_no, capacity "
            + "FROM room "
            + "WHERE id = :id";

    public static final String FIND_ALL = ""
            + "SELECT id, dormitory_id, room_number, floor_no, capacity "
            + "FROM room "
            + "ORDER BY room_number";

    public static final String FIND_BY_DORMITORY_ID =
            "SELECT id, dormitory_id, room_number, floor_no, capacity " +
                    "FROM room " +
                    "WHERE dormitory_id = :dormitoryId " +
                    "ORDER BY floor_no, room_number";

}
