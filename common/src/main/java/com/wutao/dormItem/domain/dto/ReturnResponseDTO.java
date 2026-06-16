package com.wutao.dormItem.domain.dto;

import java.util.UUID;

public class ReturnResponseDTO {
    private UUID returnRecordId;
    private String status;
    private String message;

    public UUID getReturnRecordId() {
        return returnRecordId;
    }

    public void setReturnRecordId(UUID returnRecordId) {
        this.returnRecordId = returnRecordId;
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

