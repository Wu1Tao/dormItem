package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.enums.UserRole;
import com.wutao.dormItem.domain.enums.UserStatus;
import com.wutao.dormItem.domain.model.Users;
import com.wutao.dormItem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class AuthMvcController {

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    private final UserService userService;

    public AuthMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String account,
                        String password,
                        HttpSession session,
                        Model model) {
        try {
            if (account == null || account.trim().isEmpty()) {
                model.addAttribute("error", "Введите логин");
                return "login";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Введите пароль");
                return "login";
            }

            Users user = userService.findByAccount(account.trim());

            if (user == null) {
                model.addAttribute("error", "Пользователь не найден");
                return "login";
            }

            if (user.getIsDeleted() != null && user.getIsDeleted()) {
                model.addAttribute("error", "Пользователь удалён");
                return "login";
            }

            if (user.getUserStatus() != null && user.getUserStatus().equals(UserStatus.DISABLED)) {
                model.addAttribute("error", "Пользователь заблокирован");
                return "login";
            }

            if (!password.equals(user.getUserPassword())) {
                model.addAttribute("error", "Неверный пароль");
                return "login";
            }

            session.setAttribute("currentUser", user);
            session.setAttribute("currentUserId", user.getId());
            session.setAttribute("currentUserName", user.getUserName());
            session.setAttribute("currentUserRole", user.getRole());

            log.info("Пользователь {} вошёл в систему", user.getUserAccount());
            userLog.info("Пользователь {} вошёл в систему", user.getUserAccount());

            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String register(String account,
                           String password,
                           String confirmPassword,
                           String name,
                           Model model) {
        try {
            if (account == null || account.trim().isEmpty()) {
                model.addAttribute("error", "Введите логин");
                return "register";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Введите пароль");
                return "register";
            }

            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Пароли не совпадают");
                return "register";
            }

            Users existing = userService.findByAccount(account.trim());

            if (existing != null) {
                model.addAttribute("error", "Пользователь с таким логином уже существует");
                return "register";
            }

            Users user = new Users();
            user.setUserAccount(account.trim());
            user.setUserPassword(password);
            user.setUserName(name == null || name.trim().isEmpty() ? account.trim() : name.trim());
            user.setRole(UserRole.USER);
            user.setUserStatus(UserStatus.ACTIVE);
            user.setIsDeleted(false);

            userService.addUser(user);

            log.info("Зарегистрирован новый пользователь: логин={}", account);
            userLog.info("Зарегистрирован новый пользователь: логин={}, имя={}", account, name);

            model.addAttribute("success", "Регистрация успешна. Теперь войдите в систему.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String userName = (String) session.getAttribute("currentUserName");
        session.invalidate();
        log.info("Пользователь {} вышел из системы", userName);
        userLog.info("Пользователь {} вышел из системы", userName);
        return "redirect:/login";
    }
}