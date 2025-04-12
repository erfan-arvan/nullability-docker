package org.spacious_team.table_wrapper.api;
public interface TableFactory {
    boolean canHandle(ReportPage reportPage);
    default Table create(ReportPage reportPage,
                         String tableName,
                         String lastRowString,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, lastRowString, headerDescription, 1);
    }
    default Table create(ReportPage reportPage,
                         String tableName,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, headerDescription, 1);
    }
    default Table create(ReportPage reportPage,
                         String tableName,
                         String lastRowString,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return create(reportPage,
                tableName,
                reportPage.getTableCellRange(tableName, headersRowCount, lastRowString),
                headerDescription,
                headersRowCount);
    }
    default Table create(ReportPage reportPage,
                         String tableName,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return create(reportPage,
                tableName,
                reportPage.getTableCellRange(tableName, headersRowCount),
                headerDescription,
                headersRowCount);
    }
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Predicate<Object> lastRowFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 1);
    }
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, headerDescription, 1);
    }
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Predicate<Object> lastRowFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        String tableName = "<not found>";
        TableCellRange range = reportPage.getTableCellRange(tableNameFinder, headersRowCount, lastRowFinder);
        if (!range.equals(TableCellRange.EMPTY_RANGE)) {
            TableCellAddress tableNameCell =
                    reportPage.find(range.getFirstRow(), range.getFirstRow() + 1, tableNameFinder);
            if (!tableNameCell.equals(TableCellAddress.NOT_FOUND)) {
                try {
                    TableCell cell = requireNonNull(reportPage.getCell(tableNameCell));
                    tableName = cell.getStringValue();
                } catch (Exception ignore) {
                }
            }
        }
        return create(reportPage, tableName, range, headerDescription, headersRowCount);
    }
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        String tableName = "<not found>";
        TableCellRange range = reportPage.getTableCellRange(tableNameFinder, headersRowCount);
        if (!range.equals(TableCellRange.EMPTY_RANGE)) {
            TableCellAddress tableNameCell =
                    reportPage.find(range.getFirstRow(), range.getFirstRow() + 1, tableNameFinder);
            if (!tableNameCell.equals(TableCellAddress.NOT_FOUND)) {
                try {
                    TableCell cell = requireNonNull(reportPage.getCell(tableNameCell));
                    tableName = cell.getStringValue();
                } catch (Exception ignore) {
                }
            }
        }
        return create(reportPage, tableName, range, headerDescription, headersRowCount);
    }
    default Table createNameless(ReportPage reportPage,
                                 String firstRowString,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, lastRowString, headerDescription, 1);
    }
    default Table createNameless(ReportPage reportPage,
                                 String firstRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, headerDescription, 1);
    }
    default Table createNameless(ReportPage reportPage,
                                 String providedTableName,
                                 String firstRowString,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getTableCellRange(firstRowString, headersRowCount, lastRowString)
                        .addRowsToTop(1), 
                headerDescription,
                headersRowCount);
    }
    default Table createNameless(ReportPage reportPage,
                                 String providedTableName,
                                 String firstRowString,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getTableCellRange(firstRowString, headersRowCount)
                        .addRowsToTop(1), 
                headerDescription,
                headersRowCount);
    }
    default Table createNameless(ReportPage reportPage,
                                 Predicate<Object> firstRowFinder,
                                 Predicate<Object> lastRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, lastRowFinder, headerDescription, 1);
    }
    default Table createNameless(ReportPage reportPage,
                                 Predicate<Object> firstRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, headerDescription, 1);
    }
    default Table createNameless(ReportPage reportPage,
                                 String providedTableName,
                                 Predicate<Object> firstRowFinder,
                                 Predicate<Object> lastRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getTableCellRange(firstRowFinder, headersRowCount, lastRowFinder)
                        .addRowsToTop(1), 
                headerDescription,
                headersRowCount);
    }
    default Table createNameless(ReportPage reportPage,
                                 String providedTableName,
                                 Predicate<Object> firstRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getTableCellRange(firstRowFinder, headersRowCount)
                        .addRowsToTop(1), 
                headerDescription,
                headersRowCount);
    }
    Table create(ReportPage reportPage,
                 String tableName,
                 TableCellRange tableRange,
                 Class<? extends TableColumnDescription> headerDescription,
                 int headersRowCount);
}
