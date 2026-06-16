package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.model.Room;
import com.wutao.dormItem.repository.DormitoryRepository;
import com.wutao.dormItem.repository.RoomRepository;
import com.wutao.dormItem.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository,
                           DormitoryRepository dormitoryRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room findById(UUID id) {
        log.debug("Поиск комнаты по ID: {}", id);
        
        if (id == null) {
            log.warn("Попытка поиска комнаты с null ID");
            return null;
        }

        Room room = roomRepository.findById(id);
        
        if (room != null) {
            log.debug("Комната найдена: ID={}, номер={}", id, room.getRoomNumber());
        } else {
            log.warn("Комната не найдена: ID={}", id);
        }
        
        return room;
    }

    @Override
    public List<Room> listAll() {
        log.debug("Получение списка всех комнат");
        
        try {
            List<Room> rooms = roomRepository.findAll();
            log.info("Получено {} комнат", rooms.size());
            return rooms;
        } catch (Exception e) {
            log.error("Ошибка при получении списка комнат", e);
            throw e;
        }
    }

    @Override
    public List<Room> findByDormitoryId(UUID dormitoryId) {
        log.debug("Поиск комнат по ID общежития: {}", dormitoryId);
        
        if (dormitoryId == null) {
            log.error("Попытка поиска комнат с null dormitoryId");
            throw new IllegalArgumentException("ID общежития обязателен");
        }

        try {
            List<Room> rooms = roomRepository.findByDormitoryId(dormitoryId);
            log.info("Найдено {} комнат для общежития: {}", rooms.size(), dormitoryId);
            return rooms;
        } catch (Exception e) {
            log.error("Ошибка при поиске комнат для общежития: {}", dormitoryId, e);
            throw e;
        }
    }
}