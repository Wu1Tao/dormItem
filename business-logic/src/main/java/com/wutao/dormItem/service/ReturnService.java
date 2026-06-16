// ReturnServiceImpl.java
package com.wutao.dormItem.service;

import com.wutao.dormItem.domain.dto.ReturnRequestDTO;
import com.wutao.dormItem.domain.dto.ReturnResponseDTO;

public interface ReturnService {
    ReturnResponseDTO returnItem(ReturnRequestDTO request);
}