package org.spacious_team.table_wrapper.api;
public interface ReportPage {
    default TableCellAddress find(Object value) {
        return find(value, 0);
    }
    default TableCellAddress find(Object value, int startRow) {
        return find(value, startRow, Integer.MAX_VALUE);
    }
    default TableCellAddress find(Object value, int startRow, int endRow) {
        return find(value, startRow, endRow, 0, Integer.MAX_VALUE);
    }
    TableCellAddress find(Object value, int startRow, int endRow, int startColumn, int endColumn);
    default TableCellAddress find(Predicate<Object> cellValuePredicate) {
        return find(0, cellValuePredicate);
    }
    default TableCellAddress find(int startRow, Predicate<Object> cellValuePredicate) {
        return find(startRow, Integer.MAX_VALUE, cellValuePredicate);
    }
    default TableCellAddress find(int startRow, int endRow, Predicate<Object> cellValuePredicate) {
        return find(startRow, endRow, 0, Integer.MAX_VALUE, cellValuePredicate);
    }
    TableCellAddress find(int startRow, int endRow,
                          int startColumn, int endColumn,
                          Predicate<Object> cellValuePredicate);
    default TableCellAddress findByPrefix(String prefix) {
        return findByPrefix(prefix, 0);
    }
    default TableCellAddress findByPrefix(String prefix, int startRow) {
        return findByPrefix(prefix, startRow, Integer.MAX_VALUE);
    }
    default TableCellAddress findByPrefix(String prefix, int startRow, int endRow) {
        return findByPrefix(prefix, startRow, endRow, 0, Integer.MAX_VALUE);
    }
    default TableCellAddress findByPrefix( String prefix, int startRow, int endRow, int startColumn, int endColumn) {
        return prefix == null ?
                TableCellAddress.NOT_FOUND :
                find(startRow, endRow, startColumn, endColumn, getCellStringValueIgnoreCasePrefixPredicate(prefix));
    }
    default Object getNextColumnValue(String firstColumnValuePrefix) {
        TableCellAddress address = findByPrefix(firstColumnValuePrefix);
         ReportPageRow row = getRow(address.getRow());
        if (row != null) {
            for (  TableCell cell : row) {
                if (cell != null && cell.getColumnIndex() > address.getColumn()) {
                     Object value = cell.getValue();
                    if (value != null && (!(value instanceof String) || !((String) value).isBlank())) {
                        return value;
                    }
                }
            }
        }
        return null;
    }
    ReportPageRow getRow(int i);
    int getLastRowNum();
    default TableCell getCell(TableCellAddress address) {
         ReportPageRow row = getRow(address.getRow());
        return (row == null) ? null : row.getCell(address.getColumn());
    }
    default TableCellRange getTableCellRange( String firstRowPrefix,
                                             int headersRowCount,
                                              String lastRowPrefix) {
        if (firstRowPrefix == null || lastRowPrefix == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(
                getCellStringValueIgnoreCasePrefixPredicate(firstRowPrefix),
                headersRowCount,
                getCellStringValueIgnoreCasePrefixPredicate(lastRowPrefix));
    }
    default TableCellRange getTableCellRange( Predicate<Object> firstRowFinder,
                                             int headersRowCount,
                                              Predicate<Object> lastRowFinder) {
        if (firstRowFinder == null || lastRowFinder == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = find(firstRowFinder);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        ReportPageRow firstRow = requireNonNull(getRow(startAddress.getRow()), "Row not found");
        TableCellAddress endAddress = find(startAddress.getRow() + headersRowCount + 1, lastRowFinder);
        if (endAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        ReportPageRow lastRow = requireNonNull(getRow(endAddress.getRow()), "Row not found");
        return new TableCellRange(
                startAddress.getRow(),
                endAddress.getRow(),
                firstRow.getFirstCellNum(),
                lastRow.getLastCellNum());
    }
    default TableCellRange getTableCellRange( String firstRowPrefix, int headersRowCount) {
        if (firstRowPrefix == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(
                getCellStringValueIgnoreCasePrefixPredicate(firstRowPrefix),
                headersRowCount);
    }
    default TableCellRange getTableCellRange( Predicate<Object> firstRowFinder, int headersRowCount) {
        if (firstRowFinder == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = find(firstRowFinder);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        ReportPageRow firstRow = requireNonNull(getRow(startAddress.getRow()), "Row not found");
        int lastRowNum = findEmptyRow(startAddress.getRow() + headersRowCount + 1);
        if (lastRowNum == -1) {
            lastRowNum = getLastRowNum(); 
        } else if (lastRowNum <= getLastRowNum()) {
            lastRowNum--; 
        }
        if (lastRowNum < startAddress.getRow()) {
            lastRowNum = startAddress.getRow();
        }
        ReportPageRow lastRow = requireNonNull(getRow(lastRowNum), "Row not found");
        return new TableCellRange(
                startAddress.getRow(),
                lastRowNum,
                firstRow.getFirstCellNum(),
                lastRow.getLastCellNum());
    }
    default int findEmptyRow(int startRow) {
        int lastRowNum = startRow;
        LAST_ROW:
        for (int n = getLastRowNum(); lastRowNum <= n; lastRowNum++) {
             ReportPageRow row = getRow(lastRowNum);
            if (row == null || row.getLastCellNum() == -1) {
                return lastRowNum; 
            }
            for (  TableCell cell : row) {
                 Object value;
                if (!(cell == null
                        || ((value = cell.getValue()) == null)
                        || (value instanceof String) && (value.toString().isEmpty()))) {
                    continue LAST_ROW;
                }
            }
            return lastRowNum; 
        }
        return -1;
    }
    default Table create(String tableName,
                         String tableFooterString,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription);
    }
    default Table create(String tableName,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription);
    }
    default Table create(String tableName,
                         String tableFooterString,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription, headersRowCount);
    }
    default Table create(String tableName,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription, headersRowCount);
    }
    default Table create(Predicate<Object> tableNameFinder,
                         Predicate<Object> tableFooterFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableFooterFinder, headerDescription);
    }
    default Table create(Predicate<Object> tableNameFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, headerDescription);
    }
    default Table create(Predicate<Object> tableNameFinder,
                         Predicate<Object> tableFooterFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableFooterFinder, headerDescription, headersRowCount);
    }
    default Table create(Predicate<Object> tableNameFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, headerDescription, headersRowCount);
    }
    default Table createNameless(String firstLineText,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, lastRowString, headerDescription);
    }
    default Table createNameless(String firstLineText,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, headerDescription);
    }
    default Table createNameless(String providedTableName,
                                 String firstLineText,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, lastRowString, headerDescription, headersRowCount);
    }
    default Table createNameless(String providedTableName,
                                 String firstLineText,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, headerDescription, headersRowCount);
    }
    default Table createNameless(Predicate<Object> firstLineFinder,
                                 Predicate<Object> lastRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineFinder, lastRowFinder, headerDescription);
    }
    default Table createNameless(Predicate<Object> firstLineFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineFinder, headerDescription);
    }
    default Table createNameless(String providedTableName,
                                 Predicate<Object> firstLineFinder,
                                 Predicate<Object> lastRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineFinder, lastRowFinder, headerDescription, headersRowCount);
    }
    default Table createNameless(String providedTableName,
                                 Predicate<Object> firstLineFinder,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineFinder, headerDescription, headersRowCount);
    }
}
