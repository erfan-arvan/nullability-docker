package org.spacious_team.table_wrapper.api;
public interface TableRow extends ReportPageRow, Cloneable {
    Table getTable();
    TableCell getCell(TableColumnDescription column);
    Object getCellValue(TableColumnDescription column);
    int getIntCellValue(TableColumnDescription column);
    long getLongCellValue(TableColumnDescription column);
    double getDoubleCellValue(TableColumnDescription column);
    BigDecimal getBigDecimalCellValue(TableColumnDescription column);
    String getStringCellValue(TableColumnDescription column);
    Instant getInstantCellValue(TableColumnDescription column);
    LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column);
    default Object getCellValueOrDefault(TableColumnDescription column,  Object defaultValue) {
        try {
            return getCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default int getIntCellValueOrDefault(TableColumnDescription column, int defaultValue) {
        try {
            return getIntCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default long getLongCellValueOrDefault(TableColumnDescription column, long defaultValue) {
        try {
            return getLongCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default double getDoubleCellValue(TableColumnDescription column, double defaultValue) {
        try {
            return getDoubleCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default BigDecimal getBigDecimalCellValueOrDefault(TableColumnDescription column, BigDecimal defaultValue) {
        try {
            return getBigDecimalCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default String getStringCellValueOrDefault(TableColumnDescription column, String defaultValue) {
        try {
            return getStringCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default Instant getInstantCellValueOrDefault(TableColumnDescription column, Instant defaultValue) {
        try {
            return getInstantCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default LocalDateTime getLocalDateTimeCellValueOrDefault(TableColumnDescription column, LocalDateTime defaultValue) {
        try {
            return getLocalDateTimeCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    TableRow clone() throws CloneNotSupportedException;
}
