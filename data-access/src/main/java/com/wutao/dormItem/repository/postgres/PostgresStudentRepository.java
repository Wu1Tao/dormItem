package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.model.Student;
import com.wutao.dormItem.repository.StudentRepository;
import com.wutao.dormItem.repository.postgres.mapper.StudentMapper;
import com.wutao.dormItem.repository.postgres.sql.StudentSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresStudentRepository implements StudentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final StudentMapper studentMapper;


    public PostgresStudentRepository(NamedParameterJdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    // PostgresStudentRepository.java
    @Override
    public List<Student> findByNameContaining(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("namePattern", "%" + name + "%");
        return jdbcTemplate.query(StudentSql.FIND_BY_NAME_CONTAINING, params, studentMapper);
    }

    @Override
    public Student findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(StudentSql.FIND_BY_ID, params, studentMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Student findByStudentNo(String studentNo) {
        MapSqlParameterSource params = new MapSqlParameterSource("studentNo", studentNo);
        try {
            return jdbcTemplate.queryForObject(StudentSql.FIND_BY_STUDENT_NO, params, studentMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(StudentSql.FIND_ALL, studentMapper);
    }

    @Override
    public void save(Student student) {
        if (student.getId() == null) {
            student.setId(UUID.randomUUID());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", student.getId())
                .addValue("studentNo", student.getStudentNo())
                .addValue("name", student.getName())
                .addValue("gender", student.getGender().getValue())
                .addValue("major", student.getMajor().getValue())
                .addValue("dormRoom", student.getDormRoom())
                .addValue("status", student.getStatus().getValue())
                .addValue("phone", student.getPhone())
                .addValue("isDeleted", Boolean.TRUE.equals(student.getIsDeleted()));
        jdbcTemplate.update(StudentSql.INSERT, params);
    }

    @Override
    public List<Student> findByStudentNoOrName(String keyword) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("keyword", keyword)
                .addValue("keywordPattern", "%" + keyword + "%");
        return jdbcTemplate.query(StudentSql.FIND_BY_STUDENT_NO_OR_NAME, params, studentMapper);
    }
}