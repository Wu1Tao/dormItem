package com.wutao.dormItem.domain.dto;

import com.wutao.dormItem.domain.enums.ItemCondition;

import java.util.UUID;

public class ReturnRequestDTO {
    private UUID issueRecordId;
    private Integer quantity;
    private ItemCondition condition;
    private UUID userId;

    public UUID getIssueRecordId() {
        return issueRecordId;
    }

    public void setIssueRecordId(UUID issueRecordId) {
        this.issueRecordId = issueRecordId;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}