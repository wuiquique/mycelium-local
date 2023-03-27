package com.mycelium.local.dynamic.reports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Report {
    public final String name;
    public final String tableName;
    public final List<Column> columns;

    public Report(String name, String tableName) {
        this.name = name;
        this.tableName = tableName;
        this.columns = Lists.newArrayList();
    }

    public boolean hasColumn(String name) {
        for (var col : columns) {
            if (col.name.toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Column.Type getColumnType(String name) {
        for (var col : columns) {
            if (col.name.toLowerCase().equals(name.toLowerCase())) {
                return col.type;
            }
        }
        return null;
    }

    public void addColumn(String name, String displayName, Column.Type type) {
        if (this.hasColumn(name))
            return;
        this.columns.add(new Column(name, displayName, type));
    }

    static public Map<String, Report> getAvailableReports() {
        Map<String, Report> reports = Maps.newHashMap();

        var productReport = new Report("Products", "product");
        productReport.addColumn("id", "Id", Column.Type.INTEGER);
        productReport.addColumn("name", "Name", Column.Type.TEXT);
        productReport.addColumn("desc", "Description", Column.Type.TEXT);
        productReport.addColumn("brand", "Brand", Column.Type.TEXT);
        productReport.addColumn("weight", "Weight", Column.Type.INTEGER);
        productReport.addColumn("quantity", "Quantity", Column.Type.INTEGER);
        productReport.addColumn("price", "Price", Column.Type.INTEGER);
        reports.put(productReport.tableName, productReport);

        return reports;
    }

    static private class Column {
        public final String name;
        public final String displayName;
        public final Type type;

        public Column(String name, String displayName, Type type) {
            this.name = name;
            this.displayName = displayName;
            this.type = type;
        }

        public enum Type {
            TEXT,
            INTEGER,
            FLOATING,
            DATETIME,
        }
    }

    static public class Generator {
        private final Report report;
        private final Set<String> selected;
        private final List<Filter<?>> filters;
        private final List<Sort> sorts;

        public Generator(Report report) {
            this.report = report;
            this.selected = Sets.newHashSet();
            this.filters = Lists.newArrayList();
            this.sorts = Lists.newArrayList();
        }

        public void select(String... columns) {
            for (var col : columns) {
                selected.add(col);
            }
        }

        public <T> void where(String name, Filter.Operation operation, T value) {
            filters.add(new Filter<T>(name, operation, value));
        }

        public void orderBy(String name, Sort.Order order) {
            sorts.add(new Sort(name, order));
        }

        public String generateQuery() {
            var query = "SELECT";

            if (selected.size() == 0) {
                List<String> built = Lists.newArrayList();
                for (var column : report.columns) {
                    built.add("v.\"" + column.name.toUpperCase() + "\"");
                }
                query += " " + String.join(", ", built);
            } else {
                List<String> built = Lists.newArrayList();
                for (var colName : selected) {
                    if (report.hasColumn(colName)) {
                        built.add("v.\"" + colName.toUpperCase() + "\"");
                    }
                }
                query += " " + String.join(", ", built);
            }

            query += " FROM";
            query += " " + report.tableName + " v";

            if (filters.size() > 0) {
                query += " WHERE";
                List<String> built = Lists.newArrayList();
                for (var filter : filters) {
                    if (report.hasColumn(filter.getName())) {
                        built.add(filter.generate());
                    }
                }
                query += " " + String.join(" AND ", built);
            }

            if (sorts.size() > 0) {
                query += " ORDER BY";
                List<String> built = Lists.newArrayList();
                for (var sort : sorts) {
                    if (report.hasColumn(sort.name)) {
                        built.add(sort.generate());
                    }
                }
                query += " " + String.join(", ", built);
            }

            return query;
        }

        public void setParams(PreparedStatement stmt) throws SQLException {
            var currIndex = 0;
            for (var filter : filters) {
                if (report.hasColumn(filter.getName())) {
                    filter.setParams(++currIndex, stmt);
                }
            }
        }

        public Map<String, Object> getRow(ResultSet rs) throws SQLException {
            Map<String, Object> row = Maps.newHashMap();

            if (selected.size() == 0) {
                for (var column : report.columns) {
                    switch (column.type) {
                        case TEXT -> row.put(column.name, rs.getString(column.name));
                        case INTEGER -> row.put(column.name, rs.getLong(column.name));
                        case FLOATING -> row.put(column.name, rs.getDouble(column.name));
                        case DATETIME -> row.put(column.name, rs.getTimestamp(column.name));
                    }
                }
            } else {
                for (var colName : selected) {
                    if (report.hasColumn(colName)) {
                        switch (report.getColumnType(colName)) {
                            case TEXT -> row.put(colName, rs.getString(colName));
                            case INTEGER -> row.put(colName, rs.getLong(colName));
                            case FLOATING -> row.put(colName, rs.getDouble(colName));
                            case DATETIME -> row.put(colName, rs.getTimestamp(colName));
                        }
                    }
                }
            }

            return row;
        }

        static public class Filter<T> {
            public final String name;
            public final Operation operation;
            public final T value;

            public Filter(String name, Operation operation, T value) {
                this.name = name;
                this.operation = operation;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public String generate() {
                if (operation.equals(Operation.LIKE)) {
                    var res = "UPPER(";
                    res += "v.\"" + name.toUpperCase() + "\"";
                    res += ") LIKE CONCAT(CONCAT('%', UPPER(?)), '%')";
                    return res;
                } else {
                    var res = "";
                    res += "v.\"" + name.toUpperCase() + "\"";
                    switch (operation) {
                        case EQ -> res += " = ";
                        case GT -> res += " > ";
                        case LT -> res += " < ";
                        case GTEQ -> res += " >= ";
                        case LTEQ -> res += " <= ";
                        case LIKE -> {
                        }
                    }
                    res += "?";
                    return res;
                }
            }

            public void setParams(int index, PreparedStatement stmt) throws SQLException {
                if (value instanceof String) {
                    stmt.setString(index, (String) value);
                } else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
                    stmt.setLong(index, (long) value);
                } else if (value instanceof Double || value instanceof Float) {
                    stmt.setDouble(index, (double) value);
                } else {
                    throw new SQLException("Invalid variable type");
                }
            }

            static public enum Operation {
                EQ,
                GT,
                LT,
                GTEQ,
                LTEQ,
                LIKE
            }
        }

        static public class Sort {
            public final String name;
            public final Order order;

            public Sort(String name, Order order) {
                this.name = name;
                this.order = order;
            }

            public String generate() {
                var res = "";
                res += "v.\"" + name.toUpperCase() + "\"";
                switch (order) {
                    case ASC -> res += " ASC";
                    case DESC -> res += " DESC";
                }
                return res;
            }

            static public enum Order {
                ASC,
                DESC
            }
        }
    }
}
