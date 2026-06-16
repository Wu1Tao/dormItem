package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.model.Dormitory;

import java.util.List;
import java.util.UUID;

public interface DormitoryRepository {

    Dormitory findById(UUID id);

    List<Dormitory> findAll();

}
