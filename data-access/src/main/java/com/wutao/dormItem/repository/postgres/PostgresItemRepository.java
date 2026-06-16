package com.wutao.dormItem.repository.postgres;

import com.wutao.dormItem.domain.model.Item;
import com.wutao.dormItem.repository.ItemRepository;
import com.wutao.dormItem.repository.postgres.mapper.ItemMapper;
import com.wutao.dormItem.repository.postgres.sql.ItemSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgresql", matchIfMissing = true)
public class PostgresItemRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ItemMapper itemMapper;

    public PostgresItemRepository(NamedParameterJdbcTemplate jdbcTemplate, ItemMapper itemMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemMapper = itemMapper;
    }

    @Override
    public Item findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return jdbcTemplate.queryForObject(ItemSql.FIND_BY_ID, params, itemMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Item findByCategory(Integer Category) {
        MapSqlParameterSource params = new MapSqlParameterSource("category", Category);
        try {
            return jdbcTemplate.queryForObject(ItemSql.FIND_BY_CATEGORY, params, itemMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Item> findAll() {
        return jdbcTemplate.query(ItemSql.FIND_ALL, itemMapper);
    }

    @Override
    public List<Item> findAvailableItems() {
        return jdbcTemplate.query(ItemSql.FIND_AVAILABLE, itemMapper);
    }

    @Override
    public void save(Item item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", item.getId())
                .addValue("category", item.getCategory().getValue())
                .addValue("totalStock", item.getTotalStock())
                .addValue("availableStock", item.getAvailableStock());
        jdbcTemplate.update(ItemSql.INSERT, params);
    }

    @Override
    public void decreaseStock(UUID itemId, int quantity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantity", quantity)
                .addValue("id", itemId);
        int rows = jdbcTemplate.update(ItemSql.DECREASE_STOCK, params);
        if (rows == 0) {
            throw new IllegalStateException("Недостаточно доступного запаса");
        }
    }

    @Override
    public void increaseStock(UUID itemId, int quantity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantity", quantity)
                .addValue("id", itemId);
        jdbcTemplate.update(ItemSql.INCREASE_STOCK, params);
    }
}