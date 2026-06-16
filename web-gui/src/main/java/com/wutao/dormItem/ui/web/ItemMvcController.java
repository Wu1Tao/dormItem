package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.enums.ItemCategory;
import com.wutao.dormItem.domain.enums.UserRole;
import com.wutao.dormItem.domain.model.Item;
import com.wutao.dormItem.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class ItemMvcController {

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    private final ItemService itemService;

    public ItemMvcController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String itemsPage(Model model, HttpSession session) {
        boolean admin = isSuperAdmin(session);

        model.addAttribute("items", itemService.listAll());
        model.addAttribute("categories", ItemCategory.values());
        model.addAttribute("isAdmin", admin);
        return "items";
    }

    @PostMapping("/items")
    public String addItem(@RequestParam Integer category,
                          @RequestParam Integer num,
                          HttpSession session,
                          Model model) {
        try {
            if (!isSuperAdmin(session)) {
                throw new SecurityException("Только системный администратор может добавлять предметы");
            }

            ItemCategory categoryEnum = ItemCategory.getEnumByValue(category);
            if (categoryEnum == null) {
                throw new IllegalArgumentException("Некорректная категория");
            }

            if (num == null || num < 0) {
                throw new IllegalArgumentException("должен быть больше 0");
            }

            Item item = itemService.findByCategory(category);
            itemService.updateItem(item, num);

            log.info("Предметы обновлены через веб-интерфейс: категория={}, количество={}", category, num);
            userLog.info("Веб: обновлены запасы, категория={}, количество={}, пользователь={}",
                    category, num, session.getAttribute("currentUserId"));

            return "redirect:/items";
        } catch (SecurityException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("items", itemService.listAll());
            model.addAttribute("categories", ItemCategory.values());
            model.addAttribute("isAdmin", isSuperAdmin(session));
            return "items";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Ошибка: " + e.getMessage());
            model.addAttribute("items", itemService.listAll());
            model.addAttribute("categories", ItemCategory.values());
            model.addAttribute("isAdmin", isSuperAdmin(session));
            return "items";
        }
    }

    private boolean isSuperAdmin(HttpSession session) {
        Object role = session.getAttribute("currentUserRole");
        return UserRole.ADMIN.equals(role);
    }
}