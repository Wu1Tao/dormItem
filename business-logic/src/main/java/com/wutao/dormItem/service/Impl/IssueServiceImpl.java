package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.dto.IssueRequestDTO;
import com.wutao.dormItem.domain.dto.IssueResponseDTO;
import com.wutao.dormItem.domain.enums.IssueStatus;
import com.wutao.dormItem.domain.enums.StudentStatus;
import com.wutao.dormItem.domain.model.IssueRecord;
import com.wutao.dormItem.domain.model.Item;
import com.wutao.dormItem.domain.model.Student;
import com.wutao.dormItem.repository.IssueRecordRepository;
import com.wutao.dormItem.repository.ItemRepository;
import com.wutao.dormItem.repository.StudentRepository;
import com.wutao.dormItem.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssueServiceImpl implements IssueService {

    private final StudentRepository studentRepository;
    private final ItemRepository itemRepository;
    private final IssueRecordRepository issueRecordRepository;

    @Value("${app.business.max-issue-quantity:10}")
    private int maxIssueQuantity;

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    public IssueServiceImpl(StudentRepository studentRepository,
                            ItemRepository itemRepository,
                            IssueRecordRepository issueRecordRepository) {
        this.studentRepository = studentRepository;
        this.itemRepository = itemRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    @Override
    public IssueResponseDTO issueItem(IssueRequestDTO request) {
        log.info("Начало оформления выдачи: студент={}, предмет={}, количество={}, пользователь={}",
                request.getStudentId(), request.getItemId(), request.getQuantity(), request.getUserId());

        try {
            // Валидация запроса
            validateRequest(request);
            log.debug("Валидация запроса пройдена");

            // Проверка студента
            Student student = studentRepository.findById(request.getStudentId());
            if (student == null) {
                log.error("Студент не найден: ID={}", request.getStudentId());
                throw new IllegalArgumentException("Студент не найден");
            }

            if (student.getStatus() != StudentStatus.ACTIVE) {
                log.warn("Попытка выдачи предмета неактивному студенту: ID={}, статус={}",
                        student.getId(), student.getStatus());
                throw new IllegalStateException("Студент не проживает в общежитии");
            }
            log.debug("Студент найден: номер={}, имя={}", student.getStudentNo(), student.getName());

            // Проверка предмета
            Item item = itemRepository.findById(request.getItemId());
            if (item == null) {
                log.error("Предмет не найден: ID={}", request.getItemId());
                throw new IllegalArgumentException("Предмет не найден");
            }

            if (item.getAvailableStock() < request.getQuantity()) {
                log.warn("Недостаточно запасов: предмет={}, доступно={}, запрошено={}",
                        item.getId(), item.getAvailableStock(), request.getQuantity());
                throw new IllegalStateException("Недостаточно доступного запаса");
            }
            log.debug("Предмет найден: категория={}, доступно={}", item.getCategory(), item.getAvailableStock());

            // Уменьшение запасов
            itemRepository.decreaseStock(item.getId(), request.getQuantity());
            log.debug("Запасы уменьшены: предмет={}, количество={}", item.getId(), request.getQuantity());

            // Создание записи о выдаче
            IssueRecord record = new IssueRecord();
            record.setStudentId(student.getId());
            record.setItemId(item.getId());
            record.setUserId(request.getUserId());
            record.setIssueDate(new Date());
            record.setQuantity(request.getQuantity());
            record.setStatus(IssueStatus.ISSUED);

            issueRecordRepository.save(record);
            log.info("Выдача успешно оформлена: запись={}, студент={}, предмет={}, количество={}",
                    record.getId(), student.getStudentNo(), item.getCategory(), request.getQuantity());
            userLog.info("Пользователь {} выдал предмет {} (ID={}) студенту {} в количестве {}",
                    request.getUserId(), item.getCategory(), item.getId(), student.getStudentNo(), request.getQuantity());

            // Формирование ответа
            IssueResponseDTO response = new IssueResponseDTO();
            response.setIssueRecordId(record.getId());
            response.setStatus("success");
            response.setMessage("Выдача оформлена");

            return response;

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Ошибка при оформлении выдачи: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при оформлении выдачи: студент={}, предмет={}",
                    request.getStudentId(), request.getItemId(), e);
            throw new RuntimeException("Ошибка при оформлении выдачи", e);
        }
    }

    @Override
    public List<IssueRecord> findActiveByStudentId(UUID studentId) {
        log.debug("Поиск активных выдач для студента: ID={}", studentId);

        try {
            List<IssueRecord> all = issueRecordRepository.findByStudentId(studentId);
            List<IssueRecord> active = all.stream()
                    .filter(r -> r.getStatus() != IssueStatus.RETURNED)
                    .collect(Collectors.toList());

            log.debug("Найдено {} активных выдач для студента: ID={}", active.size(), studentId);
            return active;

        } catch (Exception e) {
            log.error("Ошибка при поиске активных выдач: студент={}", studentId, e);
            throw e;
        }
    }

    private void validateRequest(IssueRequestDTO request) {
        if (request == null) {
            log.error("Получен null запрос на выдачу");
            throw new IllegalArgumentException("request обязателен");
        }

        if (request.getStudentId() == null) {
            log.error("Отсутствует studentId в запросе на выдачу");
            throw new IllegalArgumentException("studentId обязателен");
        }

        if (request.getItemId() == null) {
            log.error("Отсутствует itemId в запросе на выдачу");
            throw new IllegalArgumentException("itemId обязателен");
        }

        if (request.getUserId() == null) {
            log.error("Отсутствует userId в запросе на выдачу");
            throw new IllegalArgumentException("userId обязателен");
        }

        if (request.getQuantity() <= 0) {
            log.error("Некорректное количество в запросе: {}", request.getQuantity());
            throw new IllegalArgumentException("Количество должно быть больше 0");
        }

        if (request.getQuantity() > maxIssueQuantity) {
            log.warn("Превышен лимит выдачи: запрошено={}, максимум={}",
                    request.getQuantity(), maxIssueQuantity);
            throw new IllegalArgumentException("Количество не может быть больше " + maxIssueQuantity);
        }
    }
}