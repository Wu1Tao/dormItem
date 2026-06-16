package com.wutao.dormItem.repository.postgres.mapper;

import com.wutao.dormItem.domain.enums.ItemCategory;
import com.wutao.dormItem.domain.model.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getObject("id", UUID.class));
        item.setCategory(ItemCategory.getEnumByValue(rs.getInt("category")));
        item.setTotalStock(rs.getInt("total_stock"));
        item.setAvailableStock(rs.getInt("available_stock"));
        return item;
    }
}