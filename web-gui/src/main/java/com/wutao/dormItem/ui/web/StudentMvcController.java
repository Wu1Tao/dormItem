package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.enums.Gender;
import com.wutao.dormItem.domain.enums.Major;
import com.wutao.dormItem.domain.enums.StudentStatus;
import com.wutao.dormItem.domain.model.Student;
import com.wutao.dormItem.service.RoomService;
import com.wutao.dormItem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class StudentMvcController {

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    private final StudentService studentService;
    private final RoomService roomService;

    public StudentMvcController(StudentService studentService, RoomService roomService) {
        this.studentService = studentService;
        this.roomService = roomService;
    }

    @GetMapping("/students")
    public String studentsPage(Model model) {
        List<Student> students = studentService.listAll();
        // 防御：若 major 为 null，可设置一个默认枚举（例如 Major.OTHER）
        students.forEach(s -> {
            if (s.getMajor() == null) {
                s.setMajor(Major.OTHER); // 需要确保 Major 枚举中存在 OTHER 项
            }
        });
        model.addAttribute("students", students);
        model.addAttribute("rooms", roomService.listAll());
        return "students";
    }

    @PostMapping("/students")
    public String addStudent(@RequestParam String studentNo,
                             @RequestParam String name,
                             @RequestParam Integer gender,
                             @RequestParam Integer major,
                             @RequestParam String dormRoom,
                             @RequestParam String phone,
                             @RequestParam(defaultValue = "0") Integer status,
                             @RequestParam(defaultValue = "false") Boolean isDeleted,
                             Model model) {
        try {
            // 1. 基础校验
            if (studentNo == null || studentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("Номер студента обязателен");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Имя обязательно");
            }
            if (phone == null || !phone.matches("\\+7[0-9]{10}")) {
                throw new IllegalArgumentException("Неверный формат телефона. Используйте +7XXXXXXXXXX");
            }

            UUID dormId;
            try {
                dormId = UUID.fromString(dormRoom.trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неверный формат идентификатора комнаты");
            }

            // 3. 枚举转换
            Gender genderEnum = Gender.getEnumByValue(gender);
            Major majorEnum = Major.getEnumByValue(major);
            StudentStatus statusEnum = StudentStatus.getEnumByValue(status);
            if (genderEnum == null || majorEnum == null || statusEnum == null) {
                throw new IllegalArgumentException("Некорректные значения пола, специальности или статуса");
            }

            // 4. 创建 Student 对象
            Student student = new Student();
            student.setStudentNo(studentNo.trim());
            student.setName(name.trim());
            student.setGender(genderEnum);
            student.setMajor(majorEnum);
            student.setDormRoom(dormId);
            student.setPhone(phone.trim());
            student.setStatus(statusEnum);
            student.setIsDeleted(isDeleted);

            studentService.addStudent(student);

            log.info("Студент добавлен через веб-интерфейс: номер={}, имя={}", studentNo, name);
            userLog.info("Веб: добавлен студент, номер={}, имя={}", studentNo, name);

            return "redirect:/students";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.listAll());
            model.addAttribute("rooms", roomService.listAll()); // 重新提供房间列表
            return "students";
        }
    }
}