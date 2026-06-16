package com.wutao.dormItem.domain.model;

import java.util.UUID;

/**
 * 房间 / Комната
 * 对应数据库表：room
 */
public class Room {
    private UUID id;

    /**
     * 宿舍楼ID / ID общежития
     */
    private UUID dormitoryId;

    /**
     * 房间号 / Номер комнаты
     */
    private String roomNumber;

    /**
     * 楼层号 / Номер этажа
     */
    private Integer floorNo;

    /**
     * 容量 / Вместимость
     */
    private Integer capacity;

    public Room() {
    }

    public Room(UUID id, UUID dormitoryId, String roomNumber, Integer floorNo, Integer capacity) {
        this.id = id;
        this.dormitoryId = dormitoryId;
        this.roomNumber = roomNumber;
        this.floorNo = floorNo;
        this.capacity = capacity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(UUID dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(Integer floorNo) {
        this.floorNo = floorNo;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}