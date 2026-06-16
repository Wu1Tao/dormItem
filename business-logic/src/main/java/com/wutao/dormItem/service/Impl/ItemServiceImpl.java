package com.wutao.dormItem.service.Impl;

import com.wutao.dormItem.domain.model.Item;
import com.wutao.dormItem.repository.ItemRepository;
import com.wutao.dormItem.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private static final Logger userLog = LoggerFactory.getLogger("USER_ACTIONS");

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public void addItem(Item item) {
        if (item.getCategory() == null) {
            log.error("Попытка добавить предмет без категории");
            throw new IllegalArgumentException("Предмет не существует");
        }

        try {
            itemRepository.save(item);
            log.info("Предмет успешно добавлен: ID={}, название={}", item.getId(), item.getCategory().getText());
            userLog.info("Добавлен новый предмет: ID={}, название={}", item.getId(), item.getCategory().getText());

        } catch (Exception e) {
            log.error("Ошибка при добавлении предмета: название={}", item.getCategory().getText(), e);
            throw e;
        }
    }

    @Override
    public Item findById(UUID id) {
        log.debug("Поиск предмета по ID: {}", id);

        Item item = itemRepository.findById(id);

        if (item != null) {
            log.debug("Предмет найден: ID={}, название={}", id, item.getCategory().getText());
        } else {
            log.warn("Предмет не найден: ID={}", id);
        }

        return item;
    }

    @Override
    public Item findByCategory(Integer category) {
        log.debug("Поиск предмета по категории: {}", category);

        Item item = itemRepository.findByCategory(category);

        if (item != null) {
            log.debug("Предмет найден: категория={}, название={}", category, item.getCategory().getText());
        } else {
            log.debug("Предмет не найден для категории: {}", category);
        }

        return item;
    }

    @Override
    public List<Item> listAll() {
        log.debug("Получение списка всех предметов");

        try {
            List<Item> items = itemRepository.findAll();
            log.info("Получено {} предметов", items.size());
            return items;
        } catch (Exception e) {
            log.error("Ошибка при получении списка предметов", e);
            throw e;
        }
    }

    @Override
    public List<Item> listAvailable() {
        log.debug("Получение списка доступных предметов");

        try {
            List<Item> items = itemRepository.findAvailableItems();
            log.info("Получено {} доступных предметов", items.size());
            return items;
        } catch (Exception e) {
            log.error("Ошибка при получении списка доступных предметов", e);
            throw e;
        }
    }

    @Override
    public void updateItem(Item item, Integer num) {
        log.info("Начало обновления запасов предмета: ID={}, изменение={}", item.getId(), num);

        try {
            itemRepository.increaseStock(item.getId(), num);
            log.info("Запасы предмета успешно обновлены: ID={}, изменение={}", item.getId(), num);
            userLog.info("Обновлены запасы предмета: ID={}, изменение={}", item.getId(), num);
        } catch (Exception e) {
            log.error("Ошибка при обновлении запасов предмета: ID={}", item.getId(), e);
            throw e;
        }
    }
}