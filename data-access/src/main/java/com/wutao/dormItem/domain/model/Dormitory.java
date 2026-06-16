package com.wutao.dormItem.domain.model;

import java.util.UUID;

/**
 * 宿舍楼 / Общежитие
 * 对应数据库表：dormitory
 */
public class Dormitory {
    private UUID id;
    private String name;
    private String address;

    public Dormitory() {
    }

    public Dormitory(UUID id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}