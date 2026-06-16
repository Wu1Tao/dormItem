package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.model.Dormitory;
import com.wutao.dormItem.repository.DormitoryRepository;
import com.wutao.dormItem.service.DormitoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DormitoryServiceImpl implements DormitoryService {

    private final DormitoryRepository dormitoryRepository;

    public DormitoryServiceImpl(DormitoryRepository dormitoryRepository) {
        this.dormitoryRepository = dormitoryRepository;
    }

    @Override
    public Dormitory findById(UUID id) {
        log.debug("Поиск общежития по ID: {}", id);
        
        if (id == null) {
            log.warn("Попытка поиска общежития с null ID");
            return null;
        }

        Dormitory dormitory = dormitoryRepository.findById(id);
        
        if (dormitory != null) {
            log.debug("Общежитие найдено: ID={}, название={}", id, dormitory.getName());
        } else {
            log.warn("Общежитие не найдено: ID={}", id);
        }
        
        return dormitory;
    }

    @Override
    public List<Dormitory> listAll() {
        log.debug("Получение списка всех общежитий");
        
        try {
            List<Dormitory> dormitories = dormitoryRepository.findAll();
            log.info("Получено {} общежитий", dormitories.size());
            return dormitories;
        } catch (Exception e) {
            log.error("Ошибка при получении списка общежитий", e);
            throw e;
        }
    }
}