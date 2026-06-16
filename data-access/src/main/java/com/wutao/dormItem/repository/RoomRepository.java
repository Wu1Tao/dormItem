package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.model.Room;

import java.util.List;
import java.util.UUID;

public interface RoomRepository {
    Room findById(UUID id);

    List<Room> findAll();

    List<Room> findByDormitoryId(UUID dormitoryId);

}