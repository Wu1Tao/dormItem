package com.wutao.dormItem.domain.model;

import com.wutao.dormItem.domain.enums.UserRole;
import com.wutao.dormItem.domain.enums.UserStatus;

import java.util.UUID;

public class Users {
    private UUID id;
    private String userAccount;
    private String userPassword;
    private String userName;

    /**
     * 用户角色 / Роль
     */
    private UserRole role;

    /**
     * 账号状态 / Статус аккаунта
     */
    private UserStatus userStatus;

    /**
     * 逻辑删除 / Логическое удаление
     */
    private Boolean isDeleted;

    public Users() {
    }

    public Users(UUID id,
                String userAccount,
                String userPassword,
                String userName,
                UserRole role,
                UserStatus userStatus,
                Boolean isDeleted) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.role = role;
        this.userStatus = userStatus;
        this.isDeleted = isDeleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
