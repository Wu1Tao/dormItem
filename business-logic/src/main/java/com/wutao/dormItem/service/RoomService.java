package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.model.Room;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    Room findById(UUID id);

    List<Room> listAll();

    List<Room> findByDormitoryId(UUID dormitoryId);

}