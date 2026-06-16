package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentRepository {

    // StudentRepository.java
    List<Student> findByNameContaining(String name);

    Student findById(UUID id);

    Student findByStudentNo(String studentNo);

    List<Student> findAll();

    void save(Student student);

    List<Student> findByStudentNoOrName(String keyword);
}