package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.model.Dormitory;

import java.util.List;
import java.util.UUID;

public interface DormitoryService {

    Dormitory findById(UUID id);

    List<Dormitory> listAll();

}