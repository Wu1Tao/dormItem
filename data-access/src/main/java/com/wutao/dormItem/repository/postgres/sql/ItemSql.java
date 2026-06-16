package com.wutao.dormItem.repository.postgres.sql;

public final class ItemSql {
    private ItemSql() {}

    public static final String FIND_BY_ID =
            "SELECT id, category, total_stock, available_stock " +
                    "FROM item " +
                    "WHERE id = :id";

    public static final String FIND_BY_CATEGORY =
        "SELECT id, category, total_stock, available_stock " +
        "FROM item " +
        "WHERE category = :category";

    public static final String FIND_ALL = 
        "SELECT id, category, total_stock, available_stock " +
        "FROM item " +
        "ORDER BY category";

    public static final String FIND_AVAILABLE = 
        "SELECT id, category, total_stock, available_stock " +
        "FROM item " +
        "WHERE available_stock > 0 " +
        "ORDER BY category";

    public static final String INSERT = 
        "INSERT INTO item ( " +
        "    id, category, total_stock, available_stock " +
        ") VALUES ( " +
        "    :id, :category, :totalStock, :availableStock " +
        ")";

    public static final String DECREASE_STOCK = 
        "UPDATE item " +
        "SET available_stock = available_stock - :quantity ," +
                "total_stock = total_stock - :quantity " +
        "WHERE id = :id AND available_stock >= :quantity AND total_stock >= :quantity";

    public static final String INCREASE_STOCK =
            "UPDATE item " +
                    "SET available_stock = available_stock + :quantity, " +
                    "    total_stock = total_stock + :quantity " +
                    "WHERE id = :id";

}