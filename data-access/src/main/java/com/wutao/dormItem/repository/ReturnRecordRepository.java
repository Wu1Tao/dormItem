package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.model.ReturnRecord;

import java.util.UUID;

public interface ReturnRecordRepository {
    ReturnRecord findById(UUID id);

    void save(ReturnRecord record);

    int sumReturnedQuantityByIssueId(UUID issueRecordId);
}