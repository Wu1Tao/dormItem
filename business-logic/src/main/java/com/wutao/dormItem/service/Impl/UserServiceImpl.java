package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;
import com.wutao.dormItem.repository.UserRepository;
import com.wutao.dormItem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(Users user) {
        log.info("Начало добавления пользователя: логин={}, имя={}, роль={}", 
                 user.getUserAccount(), user.getUserName(), user.getRole());
        
        Users existing = userRepository.findByAccount(user.getUserAccount());

        if (existing != null) {
            log.error("Пользователь с логином {} уже существует", user.getUserAccount());
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        if (user.getRole() == null) {
            log.error("Попытка добавить пользователя без указания роли");
            throw new IllegalArgumentException("Роль пользователя обязательна");
        }

        if (user.getUserStatus() == null) {
            log.debug("Статус не указан, установлен по умолчанию: ACTIVE");
            user.setUserStatus(UserStatus.ACTIVE);
        }

        try {
            userRepository.save(user);
            log.info("Пользователь успешно добавлен: ID={}, логин={}, роль={}",
                     user.getId(), user.getUserAccount(), user.getRole());
            userLog.info("Зарегистрирован новый пользователь: ID={}, логин={}, роль={}",
                     user.getId(), user.getUserAccount(), user.getRole());
        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя: логин={}", user.getUserAccount(), e);
            throw e;
        }
    }

    @Override
    public Users findById(UUID id) {
        log.debug("Поиск пользователя по ID: {}", id);
        
        Users user = userRepository.findById(id);
        
        if (user != null) {
            log.debug("Пользователь найден: ID={}, логин={}", id, user.getUserAccount());
        } else {
            log.warn("Пользователь не найден: ID={}", id);
        }
        
        return user;
    }

    @Override
    public Users findByAccount(String account) {
        log.debug("Поиск пользователя по логину: {}", account);
        
        Users user = userRepository.findByAccount(account);
        
        if (user != null) {
            log.debug("Пользователь найден: логин={}, имя={}", account, user.getUserName());
        } else {
            log.debug("Пользователь не найден: логин={}", account);
        }
        
        return user;
    }

    @Override
    public List<Users> listAll() {
        log.debug("Получение списка всех пользователей");
        
        try {
            List<Users> users = userRepository.findAll();
            log.info("Получено {} пользователей", users.size());
            return users;
        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    @Override
    public void changeUserStatus(UUID id, UserStatus status) {
        log.info("Начало изменения статуса пользователя: ID={}, новый статус={}", id, status);
        
        if (id == null) {
            log.error("Попытка изменить статус с null ID");
            throw new IllegalArgumentException("ID пользователя обязателен");
        }

        if (status == null) {
            log.error("Попытка установить null статус для пользователя: ID={}", id);
            throw new IllegalArgumentException("Статус пользователя обязателен");
        }

        Users user = userRepository.findById(id);

        if (user == null) {
            log.error("Пользователь не найден при изменении статуса: ID={}", id);
            throw new IllegalArgumentException("Пользователь не найден");
        }

        try {
            userRepository.updateStatus(id, status);
            log.info("Статус пользователя успешно изменён: ID={}, логин={}, статус={}",
                     id, user.getUserAccount(), status);
            userLog.info("Изменён статус пользователя: ID={}, логин={}, новый статус={}",
                     id, user.getUserAccount(), status);
        } catch (Exception e) {
            log.error("Ошибка при изменении статуса пользователя: ID={}", id, e);
            throw e;
        }
    }
}