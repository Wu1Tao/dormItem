package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.enums.StudentStatus;
import com.wutao.dormItem.domain.model.Student;
import com.wutao.dormItem.repository.StudentRepository;
import com.wutao.dormItem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    public StudentServiceImpl(StudentRepository studentRepo) {
        this.studentRepository = studentRepo;
    }

    @Override
    public List<Student> findByNameContaining(String name) {
        log.debug("Поиск студентов по имени: {}", name);
        
        try {
            List<Student> students = studentRepository.findByNameContaining(name);
            log.info("Найдено {} студентов с именем, содержащим '{}'", students.size(), name);
            return students;
        } catch (Exception e) {
            log.error("Ошибка при поиске студентов по имени: {}", name, e);
            throw e;
        }
    }

    @Override
    public List<Student> findByStudentNoOrName(String keyword) {
        log.debug("Поиск студентов по ключевому слову: {}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("Попытка поиска с пустым ключевым словом");
            return null;
        }
        
        try {
            List<Student> students = studentRepository.findByStudentNoOrName(keyword.trim());
            log.info("Найдено {} студентов по ключевому слову '{}'", students.size(), keyword);
            return students;
        } catch (Exception e) {
            log.error("Ошибка при поиске студентов по ключевому слову: {}", keyword, e);
            throw e;
        }
    }

    @Override
    public void addStudent(Student student) {
        log.info("Начало добавления студента: номер={}, имя={}", 
                 student.getStudentNo(), student.getName());
        
        Student existing = studentRepository.findByStudentNo(student.getStudentNo());

        if (existing != null) {
            log.error("Студент с номером {} уже существует", student.getStudentNo());
            throw new IllegalArgumentException("Студент с таким номером уже существует");
        }

        if (student.getGender() == null) {
            log.error("Попытка добавить студента без указания пола");
            throw new IllegalArgumentException("Пол студента обязателен");
        }

        if (student.getMajor() == null) {
            log.error("Попытка добавить студента без указания специальности");
            throw new IllegalArgumentException("Специальность студента обязательна");
        }

        if (student.getStatus() == null) {
            log.error("Попытка добавить студента без указания статуса");
            throw new IllegalArgumentException("Статус студента обязателен");
        }

        try {
            studentRepository.save(student);
            log.info("Студент успешно добавлен: ID={}, номер={}, имя={}",
                     student.getId(), student.getStudentNo(), student.getName());
            userLog.info("Добавлен новый студент: ID={}, номер={}, имя={}",
                     student.getId(), student.getStudentNo(), student.getName());
        } catch (Exception e) {
            log.error("Ошибка при добавлении студента: номер={}", student.getStudentNo(), e);
            throw e;
        }
    }

    @Override
    public Student findById(UUID id) {
        log.debug("Поиск студента по ID: {}", id);
        
        Student student = studentRepository.findById(id);

        if (student == null) {
            log.error("Студент не найден: ID={}", id);
            throw new IllegalArgumentException("Студент не найден");
        }

        log.debug("Студент найден: ID={}, имя={}", id, student.getName());
        return student;
    }

    @Override
    public Student findByStudentNo(String no) {
        log.debug("Поиск студента по номеру: {}", no);
        
        Student student = studentRepository.findByStudentNo(no);

        if (student == null) {
            log.error("Студент не найден: номер={}", no);
            throw new IllegalArgumentException("Студент не найден");
        }

        log.debug("Студент найден: номер={}, имя={}", no, student.getName());
        return student;
    }

    @Override
    public List<Student> listAll() {
        log.debug("Получение списка всех студентов");
        
        try {
            List<Student> students = studentRepository.findAll().stream()
                    .filter(student -> student.getDormRoom() != null)
                    .filter(student -> StudentStatus.ACTIVE.equals(student.getStatus()))
                    .collect(Collectors.toList());
            
            log.info("Получено {} студентов, проживающих в общежитии", students.size());
            return students;
        } catch (Exception e) {
            log.error("Ошибка при получении списка студентов", e);
            throw e;
        }
    }
}