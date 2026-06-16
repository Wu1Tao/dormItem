package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.enums.Gender;
import com.wutao.dormItem.domain.enums.Major;
import com.wutao.dormItem.domain.enums.StudentStatus;
import com.wutao.dormItem.domain.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();

        student.setId((UUID) rs.getObject("id"));
        student.setStudentNo(rs.getString("student_no"));
        student.setName(rs.getString("name"));

        student.setGender(Gender.getEnumByValue(rs.getInt("gender")));
        student.setMajor(Major.getEnumByValue(rs.getInt("major")));

        Object dormRoomObject = rs.getObject("dorm_room");
        if (dormRoomObject == null) {
            student.setDormRoom(null);
        } else {
            student.setDormRoom((UUID) dormRoomObject);
        }

        student.setStatus(StudentStatus.getEnumByValue(rs.getInt("status")));
        student.setPhone(rs.getString("phone"));
        student.setIsDeleted(rs.getBoolean("is_deleted"));

        return student;
    }
}