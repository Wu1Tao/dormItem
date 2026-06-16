package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.model.Item;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    void addItem(Item item);

    Item findById(UUID id);

    Item findByCategory(Integer category);

    List<Item> listAll();

    List<Item> listAvailable();

    void updateItem(Item item, Integer num);
}