package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.enums.UserRole;
import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;
import com.wutao.dormItem.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class AdminUserMvcController {

    private final UserService userService;

    public AdminUserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String adminUsersPage(Model model) {
        model.addAttribute("users", userService.listAll());
        return "admin-users";
    }

    @PostMapping("/admin/users/create")
    public String createAdminUser(String account,
                                  String password,
                                  String name,
                                  Integer role,
                                  Model model) {
        try {
            validateCreateUserRequest(account, password, role);

            Users user = new Users();
            user.setUserAccount(account.trim());
            user.setUserPassword(password);
            user.setUserName(name == null || name.trim().isEmpty() ? account.trim() : name.trim());
            user.setRole(UserRole.getEnumByValue(role));
            user.setUserStatus(UserStatus.getEnumByValue(0));
            user.setIsDeleted(false);

            userService.addUser(user);

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("users", userService.listAll());
            model.addAttribute("error", e.getMessage());
            return "admin-users";
        }
    }

    @PostMapping("/admin/users/block")
    public String blockUser(@RequestParam UUID id, HttpSession session, Model model) {
        try {
            UUID currentUserId = (UUID) session.getAttribute("currentUserId");
            if (id.equals(currentUserId)) {
                throw new IllegalArgumentException("Нельзя заблокировать собственную учётную запись");
            }
            userService.changeUserStatus(id, UserStatus.DISABLED); // 注意枚举
            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("users", userService.listAll());
            model.addAttribute("error", e.getMessage());
            return "admin-users";
        }
    }

    @PostMapping("/admin/users/unblock")
    public String unblockUser(UUID id, Model model) {
        try {
            userService.changeUserStatus(id, UserStatus.ACTIVE);
            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("users", userService.listAll());
            model.addAttribute("error", e.getMessage());
            return "admin-users";
        }
    }

    private void validateCreateUserRequest(String account, String password, Integer role) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин обязателен");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль обязателен");
        }

        if (!UserRole.USER.getValue().equals(role) && !UserRole.ADMIN.getValue().equals(role)) {
            throw new IllegalArgumentException("Некорректная роль пользователя");
        }
    }
}