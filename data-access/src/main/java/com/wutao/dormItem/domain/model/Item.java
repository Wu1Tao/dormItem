package com.wutao.dormItem.domain.model;

import com.wutao.dormItem.domain.enums.ItemCategory;

import java.util.UUID;

/**
 * 对应数据库中的 item 表
 */
public class Item {
    private UUID id;

    /**
     * 物品名称 / Название предмета
     */
//    private String name;

    /**
     * 类别 / Категория
     */
    private ItemCategory category;

    /**
     * 总库存 / Общий запас
     */
    private Integer totalStock;

    /**
     * 可用库存 / Доступный запас
     */
    private Integer availableStock;

    public Item() {
    }

    public Item(UUID id,
                ItemCategory category,
                Integer totalStock,
                Integer availableStock) {
        this.id = id;
        this.category = category;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }
}