package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(Users user);

    Users findById(UUID id);

    Users findByAccount(String account);

    List<Users> listAll();

    void changeUserStatus(UUID id, UserStatus status);
}