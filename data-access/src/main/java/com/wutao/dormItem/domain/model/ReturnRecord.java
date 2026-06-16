package com.wutao.dormItem.domain.model;

import com.wutao.dormItem.domain.enums.ItemCondition;

import java.util.Date;
import java.util.UUID;

public class ReturnRecord {
    private UUID id;

    /**
     * 对应发放记录的ID / ID записи о выдаче
     */
    private UUID issueRecordId;

    /**
     * 操作用户ID / ID пользователя
     */
    private UUID userId;

    /**
     * 归还日期时间 / Дата и время возврата
     */
    private Date returnDate;

    /**
     * 归还数量 / Количество возвращенного
     */
    private Integer quantity;

    /**
     * 物品归还状况 / Состояние возвращенного предмета
     */
    private ItemCondition condition;

    public ReturnRecord() {
    }

    public ReturnRecord(UUID id,
                        UUID issueRecordId,
                        UUID userId,
                        Date returnDate,
                        Integer quantity,
                        ItemCondition condition) {
        this.id = id;
        this.issueRecordId = issueRecordId;
        this.userId = userId;
        this.returnDate = returnDate;
        this.quantity = quantity;
        this.condition = condition;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getIssueRecordId() {
        return issueRecordId;
    }

    public void setIssueRecordId(UUID issueRecordId) {
        this.issueRecordId = issueRecordId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }
}