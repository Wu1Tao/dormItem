package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.dto.ReturnRequestDTO;
import com.wutao.dormItem.domain.dto.ReturnResponseDTO;
import com.wutao.dormItem.domain.enums.IssueStatus;
import com.wutao.dormItem.domain.enums.ItemCondition;
import com.wutao.dormItem.domain.model.IssueRecord;
import com.wutao.dormItem.domain.model.ReturnRecord;
import com.wutao.dormItem.repository.IssueRecordRepository;
import com.wutao.dormItem.repository.ItemRepository;
import com.wutao.dormItem.repository.ReturnRecordRepository;
import com.wutao.dormItem.service.ReturnService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class ReturnServiceImpl implements ReturnService {
    
    private final IssueRecordRepository issueRecordRepository;
    private final ReturnRecordRepository returnRecordRepository;
    private final ItemRepository itemRepository;

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    public ReturnServiceImpl(IssueRecordRepository issueRecordRepository,
                             ReturnRecordRepository returnRecordRepository,
                             ItemRepository itemRepository) {
        this.issueRecordRepository = issueRecordRepository;
        this.returnRecordRepository = returnRecordRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ReturnResponseDTO returnItem(ReturnRequestDTO request) {
        log.info("Начало оформления возврата: запись={}, количество={}, состояние={}, пользователь={}",
                request.getIssueRecordId(), request.getQuantity(), request.getCondition(), request.getUserId());
        
        try {
            // Валидация запроса
            validateRequest(request);
            log.debug("Валидация запроса на возврат пройдена");

            // Проверка записи о выдаче
            IssueRecord issueRecord = issueRecordRepository.findById(request.getIssueRecordId());

            if (issueRecord == null) {
                log.error("Запись о выдаче не найдена: ID={}", request.getIssueRecordId());
                throw new IllegalArgumentException("Запись о выдаче не найдена");
            }

            if (issueRecord.getStatus() == IssueStatus.RETURNED) {
                log.warn("Попытка возврата уже возвращённого предмета: запись={}", request.getIssueRecordId());
                throw new IllegalStateException("Предмет уже возвращён полностью");
            }

            // Проверка количества
            int alreadyReturned = returnRecordRepository.sumReturnedQuantityByIssueId(request.getIssueRecordId());
            int remaining = issueRecord.getQuantity() - alreadyReturned;

            log.debug("Проверка количества: выдано={}, уже возвращено={}, осталось={}, запрос={}", 
                    issueRecord.getQuantity(), alreadyReturned, remaining, request.getQuantity());

            if (request.getQuantity() > remaining) {
                log.warn("Количество возврата превышает выданное: осталось={}, запрошено={}", 
                        remaining, request.getQuantity());
                throw new IllegalArgumentException("Количество возврата превышает выданное");
            }

            ItemCondition condition = request.getCondition();

            // Создание записи о возврате
            ReturnRecord returnRecord = new ReturnRecord();
            returnRecord.setIssueRecordId(request.getIssueRecordId());
            returnRecord.setUserId(request.getUserId());
            returnRecord.setReturnDate(new Date());
            returnRecord.setQuantity(request.getQuantity());
            returnRecord.setCondition(condition);

            returnRecordRepository.save(returnRecord);
            log.debug("Запись о возврате создана: ID={}", returnRecord.getId());

            // Обновление статуса выдачи
            int newTotalReturned = alreadyReturned + request.getQuantity();

            if (newTotalReturned >= issueRecord.getQuantity()) {
                issueRecordRepository.updateStatus(issueRecord.getId(), IssueStatus.RETURNED);
                log.info("Статус выдачи обновлён на RETURNED: запись={}", issueRecord.getId());
            } else {
                issueRecordRepository.updateStatus(issueRecord.getId(), IssueStatus.PARTIALLY_RETURNED);
                log.info("Статус выдачи обновлён на PARTIALLY_RETURNED: запись={}, возвращено={}/{}",
                        issueRecord.getId(), newTotalReturned, issueRecord.getQuantity());
            }

            // Возврат предмета на склад (если в хорошем состоянии)
            if (condition == ItemCondition.GOOD) {
                itemRepository.increaseStock(issueRecord.getItemId(), request.getQuantity());
                log.info("Запасы увеличены: предмет={}, количество={}", 
                        issueRecord.getItemId(), request.getQuantity());
            } else {
                log.warn("Предмет возвращён в повреждённом состоянии: предмет={}, количество={}", 
                        issueRecord.getItemId(), request.getQuantity());
            }

            // Формирование ответа
            ReturnResponseDTO response = new ReturnResponseDTO();
            response.setReturnRecordId(returnRecord.getId());
            response.setStatus("success");
            response.setMessage("Возврат оформлен");

            log.info("Возврат успешно оформлен: запись={}, количество={}, состояние={}",
                    returnRecord.getId(), request.getQuantity(), condition);

            userLog.info("Пользователь {} оформил возврат: запись={}, количество={}, состояние={}",
                    request.getUserId(), request.getIssueRecordId(), request.getQuantity(), condition);

            return response;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Ошибка при оформлении возврата: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при оформлении возврата: запись={}", 
                    request.getIssueRecordId(), e);
            throw new RuntimeException("Ошибка при оформлении возврата", e);
        }
    }

    private void validateRequest(ReturnRequestDTO request) {
        if (request == null) {
            log.error("Получен null запрос на возврат");
            throw new IllegalArgumentException("request обязателен");
        }

        if (request.getIssueRecordId() == null) {
            log.error("Отсутствует issueRecordId в запросе на возврат");
            throw new IllegalArgumentException("issueRecordId обязателен");
        }

        if (request.getUserId() == null) {
            log.error("Отсутствует userId в запросе на возврат");
            throw new IllegalArgumentException("userId обязателен");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            log.error("Некорректное количество в запросе на возврат: {}", request.getQuantity());
            throw new IllegalArgumentException("Количество должно быть больше 0");
        }

        if (request.getCondition() == null) {
            log.error("Отсутствует condition в запросе на возврат");
            throw new IllegalArgumentException("condition обязателен");
        }
    }
}