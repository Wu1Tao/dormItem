package com.wutao.dormItem.domain.model;

import com.wutao.dormItem.domain.enums.Gender;
import com.wutao.dormItem.domain.enums.Major;
import com.wutao.dormItem.domain.enums.StudentStatus;

import java.util.UUID;

public class Student {
    private UUID id;

    /**
     * 学号 / Номер студенческого билета
     */
    private String studentNo;

    private String name;

    /**
     * 性别 / Пол
     */
    private Gender gender;

    /**
     * 专业 / Специальность
     */
    private Major major;

    private UUID dormitoryId;

    /**
     * 宿舍号 / Номер комнаты в общежитии
     */
    private UUID dormRoom;

    /**
     * 学籍状态 / Статус студента
     */
    private StudentStatus status;

    /**
     * 电话号码 / Номер телефона
     */
    private String phone;

    /**
     * 逻辑删除 / Логическое удаление
     */
    private Boolean isDeleted;

    public Student() {
    }

    public Student(UUID id,
                   String studentNo,
                   String name,
                   Gender gender,
                   Major major,
                   UUID dormRoom,
                   StudentStatus status,
                   String phone,
                   Boolean isDeleted) {
        this.id = id;
        this.studentNo = studentNo;
        this.name = name;
        this.gender = gender;
        this.major = major;
        this.dormRoom = dormRoom;
        this.status = status;
        this.phone = phone;
        this.isDeleted = isDeleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public UUID getDormRoom() {
        return dormRoom;
    }

    public void setDormRoom(UUID dormRoom) {
        this.dormRoom = dormRoom;
    }

    public UUID getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(UUID dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}