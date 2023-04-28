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
    public final Map<String, Preconfiguration> preconfigurations;

    public Report(String name, String tableName, List<Column> columns,
            Map<String, Preconfiguration> preconfigurations) {
        this.name = name;
        this.tableName = tableName;
        this.columns = columns;
        this.preconfigurations = preconfigurations;
    }

    static public Map<String, Report> getAvailableReports() {
        return Map.ofEntries(
                Map.entry("inventory", new Report(
                        "Products",
                        "product",
                        List.of(
                                new Column("id", "Id", Column.Type.INTEGER),
                                new Column("name", "Name", Column.Type.TEXT),
                                new Column("desc", "Description", Column.Type.TEXT),
                                new Column("brand", "Brand", Column.Type.TEXT),
                                new Column("weight", "Weight", Column.Type.INTEGER),
                                new Column("quantity", "Quantity", Column.Type.INTEGER),
                                new Column("price", "Price", Column.Type.INTEGER)),
                        Map.ofEntries(
                                Map.entry("running_out", new Preconfiguration(
                                        "5 products or less",
                                        List.of(
                                                new Generator.Filter<Integer>("quantity",
                                                        Generator.Filter.Operation.LTEQ, 5)),
                                        List.of()))))));
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    static private class Preconfiguration {
        public final String name;
        public final List<Generator.Filter<?>> filters;
        public final List<Generator.Sort> sorts;

        public Preconfiguration(String name, List<Generator.Filter<?>> filters, List<Generator.Sort> sorts) {
            this.name = name;
            this.filters = filters;
            this.sorts = sorts;
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
                if (value instanceof String v) {
                    stmt.setString(index, v);
                } else if (value instanceof Long v) {
                    stmt.setLong(index, v);
                } else if (value instanceof Integer v) {
                    stmt.setLong(index, v);
                } else if (value instanceof Short v) {
                    stmt.setLong(index, v);
                } else if (value instanceof Double v) {
                    stmt.setDouble(index, v);
                } else if (value instanceof Float v) {
                    stmt.setDouble(index, v);
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
