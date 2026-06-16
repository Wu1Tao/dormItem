package com.wutao.dormItem.domain.model;

import com.wutao.dormItem.domain.enums.IssueStatus;

import java.util.Date;
import java.util.UUID;

/**
 *  issue_record 表
 */
public class IssueRecord {
    private UUID id;
    private UUID studentId;
    private UUID itemId;

    /**
     * 操作管理员ID / ID пользователя
     */
    private UUID userId;

    private String itemName; // getter/setter

    /**
     * 发放日期时间 / Дата и время выдачи
     */
    private Date issueDate;

    /**
     * 预期归还日期 / Ожидаемая дата возврата
     */
    private Date expectedReturnDate;

    /**
     * 发放数量 / Количество выданного
     */
    private Integer quantity;

    /**
     * 归还日期 / Фактическая дата возврата
     */
    private Date returnDate;

    /**
     * 发放状态 / Статус выдачи
     */
    private IssueStatus status;

    public IssueRecord() {
    }

    public IssueRecord(UUID studentId,
                       UUID itemId,
                       UUID userId,
                       Integer quantity,
                       Date expectedReturnDate) {
        this.studentId = studentId;
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
        this.expectedReturnDate = expectedReturnDate;
        this.issueDate = new Date();
        this.status = IssueStatus.ISSUED;
    }

    public IssueRecord(UUID id,
                       UUID studentId,
                       UUID itemId,
                       UUID userId,
                       Date issueDate,
                       String itemName,
                       Date expectedReturnDate,
                       Integer quantity,
                       Date returnDate,
                       IssueStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.itemId = itemId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.itemName = itemName;
        this.expectedReturnDate = expectedReturnDate;
        this.quantity = quantity;
        this.returnDate = returnDate;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
