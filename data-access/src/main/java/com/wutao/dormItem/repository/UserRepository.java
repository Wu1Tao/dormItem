package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    Users findById(UUID id);

    Users findByAccount(String account);

    void save(Users user);

    List<Users> findAll();

    void updateStatus(UUID id, UserStatus status);
}