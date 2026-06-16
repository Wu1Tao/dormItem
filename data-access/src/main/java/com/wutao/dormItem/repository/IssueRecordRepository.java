package com.wutao.dormItem.repository;

import com.wutao.dormItem.domain.enums.IssueStatus;
import com.wutao.dormItem.domain.model.IssueRecord;

import java.util.List;
import java.util.UUID;

public interface IssueRecordRepository {
    IssueRecord findById(UUID id);

    List<IssueRecord> findByStudentId(UUID studentId);

    List<IssueRecord> findByStatusNot(IssueStatus status);

    void save(IssueRecord record);

    void updateStatus(UUID id, IssueStatus status);
}