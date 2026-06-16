package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.dto.IssueRequestDTO;
import com.wutao.dormItem.domain.dto.IssueResponseDTO;
import com.wutao.dormItem.domain.model.IssueRecord;

import java.util.List;
import java.util.UUID;

public interface IssueService {
    IssueResponseDTO issueItem(IssueRequestDTO request);

    List<IssueRecord> findActiveByStudentId(UUID studentId);

}