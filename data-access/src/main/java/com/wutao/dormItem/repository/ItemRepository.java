package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.model.Item;

import java.util.List;
import java.util.UUID;

public interface ItemRepository {

    Item findById(UUID id);

    Item findByCategory(Integer category);

    List<Item> findAll();

    List<Item> findAvailableItems();

    void save(Item item);

    void decreaseStock(UUID itemId, int quantity);

    void increaseStock(UUID itemId, int quantity);
}
