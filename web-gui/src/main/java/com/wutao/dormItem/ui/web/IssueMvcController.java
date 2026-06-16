package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.dto.IssueRequestDTO;
import com.wutao.dormItem.service.IssueService;
import com.wutao.dormItem.service.ItemService;
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
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class IssueMvcController {

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    private final IssueService issueService;
    private final StudentService studentService;
    private final ItemService itemService;

    public IssueMvcController(IssueService issueService,
                              StudentService studentService,
                              ItemService itemService) {
        this.issueService = issueService;
        this.studentService = studentService;
        this.itemService = itemService;
    }

    @GetMapping("/issue")
    public String issuePage(Model model) {
        fillIssuePageModel(model);
        return "issue";
    }

    // IssueMvcController.java 部分修改

    @PostMapping("/issue")
    public String issueItem(@RequestParam String studentId,
                            @RequestParam String itemId,
                            @RequestParam Integer quantity,
//                            @RequestParam String expectedReturnDate,
                            HttpSession session,
                            Model model) {
        try {
            UUID studentUuid = UUID.fromString(studentId);
            UUID itemUuid = UUID.fromString(itemId);
            UUID userId = (UUID) session.getAttribute("currentUserId");

            IssueRequestDTO request = new IssueRequestDTO();
            request.setStudentId(studentUuid);
            request.setItemId(itemUuid);
            request.setQuantity(quantity);
            request.setUserId(userId);
//            request.setExpectedReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse(expectedReturnDate));

            issueService.issueItem(request);
            log.info("Выдача оформлена через веб-интерфейс: студент={}, предмет={}", studentId, itemId);
            userLog.info("Веб: пользователь {} выдал предмет {} студенту {}", userId, itemId, studentId);
            model.addAttribute("success", "Предмет успешно выдан");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        fillIssuePageModel(model);
        return "issue";
    }

    private void fillIssuePageModel(Model model) {
        model.addAttribute("students", studentService.listAll());
        model.addAttribute("items", itemService.listAvailable());
    }
}