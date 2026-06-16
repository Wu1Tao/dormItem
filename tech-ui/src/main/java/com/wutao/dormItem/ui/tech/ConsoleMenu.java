package com.wutao.dormItem.ui.tech;

import com.wutao.dormItem.domain.dto.IssueRequestDTO;
import com.wutao.dormItem.domain.dto.IssueResponseDTO;
import com.wutao.dormItem.domain.dto.ReturnRequestDTO;
import com.wutao.dormItem.domain.dto.ReturnResponseDTO;
import com.wutao.dormItem.domain.enums.*;
import com.wutao.dormItem.domain.model.*;
import com.wutao.dormItem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Slf4j
public class ConsoleMenu {
    private final StudentService studentService;
    private final ItemService itemService;
    private final IssueService issueService;
    private final ReturnService returnService;
    private final UserService userService;
    private final RoomService roomService;
    private final DormitoryService dormitoryService;

    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ConsoleMenu(StudentService studentService,
                       ItemService itemService,
                       IssueService issueService,
                       ReturnService returnService,
                       RoomService roomService,
                       DormitoryService dormitoryService,
                       UserService userService) {
        this.studentService = studentService;
        this.itemService = itemService;
        this.issueService = issueService;
        this.returnService = returnService;
        this.userService = userService;
        this.roomService = roomService;
        this.dormitoryService = dormitoryService;
    }

    public void start() {
        log.info("Technical UI started");

        while (true) {
            printMenu();

            String command = scanner.nextLine().trim();

            try {
                switch (command) {
                    case "1":
                        addStudent();
                        break;
                    case "2":
                        listStudents();
                        break;
                    case "3":
                        addItem();
                        break;
                    case "4":
                        listItems();
                        break;
                    case "5":
                        listAvailableItems();
                        break;
                    case "6":
                        issueItem();
                        break;
                    case "7":
                        returnItem();
                        break;
                    case "8":
                        findUserByAccount();
                        break;
                    case "0":
                        log.info("Technical UI stopped");
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неизвестная команда");
                        break;
                }
            } catch (Exception e) {
                log.error("Technical UI operation failed", e);
                log.warn("Technical UI operation failed: {}", e.getMessage());
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("1. Добавить студента");
        System.out.println("2. Список студентов");
        System.out.println("3. Добавить предмет");
        System.out.println("4. Список всех предметов");
        System.out.println("5. Список доступных предметов");
        System.out.println("6. Выдать предмет");
        System.out.println("7. Вернуть предмет");
        System.out.println("8. Найти пользователя по аккаунту");
        System.out.println("0. Выход");
        System.out.print("Команда: ");
    }

    private UUID selectRoomForStudent() {
        List<Dormitory> dormitories = dormitoryService.listAll();

        if (dormitories.isEmpty()) {
            System.out.println("Пока нет общага, надо добавить");
            return null;
        }

        System.out.println("Номер общага：");
        for (int i = 0; i < dormitories.size(); i++) {
            Dormitory dormitory = dormitories.get(i);
            System.out.printf(
                    "%d. %s，адрес：%s%n",
                    i + 1,
                    dormitory.getName(),
                    dormitory.getAddress()
            );
        }
        System.out.println("ввести номер общага：");
        int dormitoryIndex = Integer.parseInt(scanner.nextLine().trim());
        Dormitory selectedDormitory = dormitories.get(dormitoryIndex - 1);

        List<Room> rooms = roomService.findByDormitoryId(selectedDormitory.getId());

        if (rooms.isEmpty()) {
            System.out.println("Пока нет комнаты, надо добавить。");
            return null;
        }

        System.out.println("В настоящее время свободные комнаты:");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.printf(
                    "%d. номер комнаты：%s，этаж：%s，容量：%s%n",
                    i + 1,
                    room.getRoomNumber(),
                    room.getFloorNo(),
                    room.getCapacity()
            );
        }
        System.out.println("Номер комнаты：");
        int roomIndex = Integer.parseInt(scanner.nextLine().trim());

        Room selectedRoom = rooms.get(roomIndex - 1);

        return selectedRoom.getId();
    }

    private void addStudent() {
        Student student = new Student();

        System.out.print("Student no / (Номер студенческого билета): ");
        student.setStudentNo(scanner.nextLine().trim());

        System.out.print("Name: ");
        student.setName(scanner.nextLine().trim());

        System.out.print("Gender (Пол: 0-женский, 1-мужской, 2-неизвестно): ");
        student.setGender(Gender.getEnumByValue(Integer.parseInt(scanner.nextLine().trim())));

        System.out.print("Major / (факультет): ");
        student.setMajor(Major.getEnumByValue(Integer.parseInt(scanner.nextLine().trim())));

        System.out.print("Dorm room / (Номер комнаты в общежитии): ");
        UUID roomId = selectRoomForStudent();

        if (roomId == null) {
            System.out.println("未选择房间，取消添加学生。");
            return;
        }

        student.setDormRoom(roomId);

        System.out.print("Status (active/graduated/moved_out): ");
        student.setStatus(StudentStatus.getEnumByValue(Integer.parseInt(scanner.nextLine().trim())));

        System.out.print("Phone: ");
        student.setPhone(scanner.nextLine().trim());

        student.setIsDeleted(false);

        studentService.addStudent(student);

        log.info("Student added: id={}, studentNo={}", student.getId(), student.getStudentNo());
        System.out.println("Студент добавлен. ID = " + student.getId());
    }

    private void listStudents() {
        List<Student> students = studentService.listAll();

        System.out.println("ID | StudentNo | Name | Gender | Major | Room | Status | Phone");
        for (Student s : students) {
            System.out.printf("%s | %s | %s | %s | %s | %s | %s%n",
                    s.getStudentNo(),
                    s.getName(),
                    s.getGender(),
                    s.getMajor(),
                    roomService.findById(s.getDormRoom()).getRoomNumber(),
                    s.getStatus(),
                    s.getPhone());
        }

        log.info("Students listed, count={}", students.size());
    }

    private void addItem() {


        System.out.print("Item / (Предмет) / (0 - 9): ");
        Integer itemId = Integer.parseInt(scanner.nextLine().trim());

        Item item = itemService.findByCategory(itemId);

        System.out.print("Quantity / (количество): ");
        Integer num = Integer.parseInt(scanner.nextLine().trim());


        itemService.updateItem(item, num);

        log.info("Item added: name={}, quantity={}", item.getCategory().getText(), num);
        System.out.println(item.getCategory().getText() + " добавлен " + num);
    }

    private void listItems() {
        List<Item> items = itemService.listAll();
        printItems(items);
        log.info("Items listed, count={}", items.size());
    }

    private void listAvailableItems() {
        List<Item> items = itemService.listAvailable();
        printItems(items);
        log.info("Available items listed, count={}", items.size());
    }

    private void printItems(List<Item> items) {
        System.out.println("Category | Total | Available");
        for (Item i : items) {
            System.out.printf("%s | %d | %d%n",
                    i.getCategory().getText(),
                    i.getTotalStock(),
                    i.getAvailableStock());
        }
    }

    private void issueItem() throws Exception {
        // 1. 通过学生号查询学生
        System.out.print("Student number (студенческий билет): ");
        String studentNo = scanner.nextLine().trim();
        Student student = studentService.findByStudentNo(studentNo);
        if (student == null) {
            System.out.println("Студент не найден");
            return;
        }

        // 2. 通过物品名称查询物品（可能有多个，让用户选择）
        List<Item> items = itemService.listAvailable(); // 或 listAll()
        if (items.isEmpty()) {
            System.out.println("Нет доступных предметов");
            return;
        }
        System.out.println("Доступные предметы:");
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.printf("%d. %s (доступно: %d)%n", i+1, it.getCategory().getText(), it.getAvailableStock());
        }
        System.out.print("Выберите номер предмета: ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (idx < 0 || idx >= items.size()) {
            System.out.println("Неверный выбор");
            return;
        }
        Item selectedItem = items.get(idx);

        Users user = userService.findByAccount("admin");

        // 4. 输入数量和预期归还日期
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Expected return date (yyyy-MM-dd): ");
        Date expectedDate = dateFormat.parse(scanner.nextLine().trim());

        // 5. 构造 DTO 并调用 Service
        IssueRequestDTO request = new IssueRequestDTO();
        request.setStudentId(student.getId());       // UUID
        request.setItemId(selectedItem.getId());    // UUID
        request.setUserId(user.getId());             // UUID
        request.setQuantity(quantity);
        request.setExpectedReturnDate(expectedDate);

        IssueResponseDTO response = issueService.issueItem(request);
        System.out.println(response.getMessage() + ". Issue record ID = " + response.getIssueRecordId());
    }

    private void returnItem() {
        // 1. 通过学生学号或姓名查找学生
        System.out.print("Student number or name: ");
        String input = scanner.nextLine().trim();

        Student student = null;
        // 先按学号精确查找
        student = studentService.findByStudentNo(input);
        if (student == null) {
            // 按姓名模糊查找（需在 StudentService 中实现 findByNameContaining）
            List<Student> students = studentService.findByNameContaining(input);
            if (students.isEmpty()) {
                System.out.println("Student not found");
                return;
            } else if (students.size() == 1) {
                student = students.get(0);
            } else {
                System.out.println("Multiple students found:");
                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    System.out.printf("%d. %s (%s)%n", i+1, s.getName(), s.getStudentNo());
                }
                System.out.print("Choose number: ");
                int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (idx < 0 || idx >= students.size()) {
                    System.out.println("Invalid choice");
                    return;
                }
                student = students.get(idx);
            }
        }

        // 2. 获取该学生的未归还借阅记录
        List<IssueRecord> activeRecords = issueService.findActiveByStudentId(student.getId());
        if (activeRecords == null || activeRecords.isEmpty()) {
            System.out.println("No active issues for this student");
            return;
        }

        // 3. 显示记录供选择
        System.out.println("Active issues:");
        for (int i = 0; i < activeRecords.size(); i++) {
            IssueRecord rec = activeRecords.get(i);
            Item item = itemService.findById(rec.getItemId());
            String itemName = (item != null) ? item.getCategory().getText() : rec.getItemId().toString();
            System.out.printf("%d. %s (quantity: %d, expected return: %s)%n",
                    i+1, itemName, rec.getQuantity(), rec.getExpectedReturnDate());
        }
        System.out.print("Choose issue record to return: ");
        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (choice < 0 || choice >= activeRecords.size()) {
            System.out.println("Invalid choice");
            return;
        }
        IssueRecord selected = activeRecords.get(choice);

        // 4. 输入归还数量
        System.out.print("Return quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine().trim());

        // 5. 输入物品状况
        System.out.print("Condition (good/damaged): ");
        String condStr = scanner.nextLine().trim();
        ItemCondition condition;
        if ("good".equalsIgnoreCase(condStr)) {
            condition = ItemCondition.GOOD;
        } else if ("damaged".equalsIgnoreCase(condStr)) {
            condition = ItemCondition.DAMAGED;
        } else {
            System.out.println("Invalid condition, please use 'good' or 'damaged'");
            return;
        }

        // 6. 通过账号查询操作员用户
        System.out.print("Operator account: ");
        String userAccount = scanner.nextLine().trim();
        Users user = userService.findByAccount(userAccount);
        if (user == null) {
            System.out.println("User not found");
            return;
        }

        // 7. 构造 DTO 并调用 Service
        ReturnRequestDTO request = new ReturnRequestDTO();
        request.setIssueRecordId(selected.getId());   // UUID 类型
        request.setQuantity(quantity);
        request.setCondition(condition);
        request.setUserId(user.getId());

        ReturnResponseDTO response = returnService.returnItem(request);
        System.out.println(response.getMessage() + ". Return record ID = " + response.getReturnRecordId());
    }


    private void findUserByAccount() {
        System.out.print("Account: ");
        String account = scanner.nextLine().trim();

        Users u = userService.findByAccount(account);
        if (u == null) {
            System.out.println("Пользователь не найден");
            return;
        }


        System.out.printf("ID=%s, account=%s, name=%s, role=%s, status=%s%n",
                u.getId(),
                u.getUserAccount(),
                u.getUserName(),
                u.getRole(),
                u.getUserStatus());


        log.info("User searched by account={}", account);
    }
}