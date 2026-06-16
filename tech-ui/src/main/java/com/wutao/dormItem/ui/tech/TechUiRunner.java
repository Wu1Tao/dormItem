package com.wutao.dormItem.ui.tech;

import com.wutao.dormItem.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "tech")

public class TechUiRunner implements CommandLineRunner {

    private final String uiMode;
    private final ConsoleMenu consoleMenu;

    public TechUiRunner(@Value("${app.ui.mode:none}") String uiMode,
                        StudentService studentService,
                        ItemService itemService,
                        IssueService issueService,
                        ReturnService returnService,
                        RoomService roomService,
                        DormitoryService dormitoryService,
                        UserService userService
                        ) {
        this.uiMode = uiMode;
        this.consoleMenu = new ConsoleMenu(
                studentService,
                itemService,
                issueService,
                returnService,
                roomService,
                dormitoryService,
                userService

        );
    }

    @Override
    public void run(String... args) {
        // 如果没有关联的控制台（例如 GUI 应用），则跳过命令行菜单
        if (System.console() == null) {
            System.out.println("No console available, skipping console UI.");
            return;
        }
        // 原来的菜单启动代码
        consoleMenu.start();
    }
}