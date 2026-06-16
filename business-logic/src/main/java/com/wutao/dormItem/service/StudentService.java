package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    void addStudent(Student student);

    Student findById(UUID id);

    Student findByStudentNo(String no);

    List<Student> listAll();

    // StudentService.java
    List<Student> findByNameContaining(String name);

    List<Student> findByStudentNoOrName(String keyword);
}