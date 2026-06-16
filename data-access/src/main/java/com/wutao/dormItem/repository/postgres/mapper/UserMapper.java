package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.enums.UserRole;
import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class UserMapper implements RowMapper<Users> {

    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        Users user = new Users();
        user.setId(rs.getObject("id", UUID.class));
        user.setUserAccount(rs.getString("user_account"));
        user.setUserPassword(rs.getString("user_password"));
        user.setUserName(rs.getString("user_name"));
        user.setRole(UserRole.getEnumByValue(rs.getInt("role")));
        user.setUserStatus(UserStatus.getEnumByValue(rs.getInt("user_status")));
        user.setIsDeleted(rs.getBoolean("is_deleted"));
        return user;
    }
}