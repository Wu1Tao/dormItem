// ReturnMvcController.java

package com.wutao.dormItem.ui.web;

import com.wutao.dormItem.domain.dto.ReturnRequestDTO;
import com.wutao.dormItem.domain.enums.ItemCondition;
import com.wutao.dormItem.domain.model.IssueRecord;
import com.wutao.dormItem.domain.model.Student;
import com.wutao.dormItem.service.IssueService;
import com.wutao.dormItem.service.ItemService;
import com.wutao.dormItem.service.ReturnService;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class ReturnMvcController {

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    private final ReturnService returnService;
    private final StudentService studentService;
    private final IssueService issueService;
    private final ItemService itemService;

    public ReturnMvcController(ReturnService returnService,
                               StudentService studentService,
                               IssueService issueService,
                               ItemService itemService) {
        this.returnService = returnService;
        this.studentService = studentService;
        this.issueService = issueService;
        this.itemService = itemService;
    }

    @GetMapping("/return")
    public String returnPage() {
        return "return";
    }

    // 第一步：根据学生号或姓名查询学生，展示未归还记录列表
    @PostMapping("/return/search")
    public String searchStudent(@RequestParam String searchKeyword,
                                Model model) {
        List<Student> students = studentService.findByStudentNoOrName(searchKeyword);
        if (students.isEmpty()) {
            model.addAttribute("error", "Студент не найден");
            return "return";
        }
        if (students.size() == 1) {
            Student student = students.get(0);
            List<IssueRecord> activeRecords = issueService.findActiveByStudentId(student.getId());
            if (activeRecords.isEmpty()) {
                model.addAttribute("error", "У студента нет активных выдач");
                return "return";
            }
            // 为每条记录补充物品名称
            for (IssueRecord rec : activeRecords) {
                String itemName = itemService.findById(rec.getItemId()).getCategory().getText();
                rec.setItemName(itemName); // 需要在 IssueRecord 中添加临时字段，或者用 DTO
            }
            model.addAttribute("activeRecords", activeRecords);
            model.addAttribute("student", student);
        } else {
            model.addAttribute("students", students);
        }
        return "return";
    }

    // 第二步：选中具体记录后执行归还
    @PostMapping("/return/do")
    public String returnItem(@RequestParam UUID issueRecordId,
                             @RequestParam Integer quantity,
                             @RequestParam Integer condition,
                             HttpSession session,
                             Model model) {
        try {
            UUID userId = (UUID) session.getAttribute("currentUserId");
            ReturnRequestDTO request = new ReturnRequestDTO();
            request.setIssueRecordId(issueRecordId);
            request.setQuantity(quantity);
            request.setCondition(ItemCondition.getEnumByValue(condition));
            request.setUserId(userId);

            returnService.returnItem(request);

            log.info("Возврат оформлен через веб-интерфейс: запись={}", issueRecordId);
            userLog.info("Веб: пользователь {} оформил возврат записи {}, количество={}",
                    userId, issueRecordId, quantity);
            model.addAttribute("success", "Возврат успешно оформлен");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "return";
    }
}