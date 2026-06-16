package com.wutao.dormItem.domain.dto;

import java.util.UUID;

public class IssueResponseDTO {
    private UUID issueRecordId;
    private String status;
    private String message;

    public UUID getIssueRecordId() {
        return issueRecordId;
    }

    public void setIssueRecordId(UUID issueRecordId) {
        this.issueRecordId = issueRecordId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}