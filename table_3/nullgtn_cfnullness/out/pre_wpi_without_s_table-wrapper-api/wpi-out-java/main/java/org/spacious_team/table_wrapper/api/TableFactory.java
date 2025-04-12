/*
 * Table Wrapper API
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.spacious_team.table_wrapper.api;

import java.util.function.Predicate;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface TableFactory {

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    boolean canHandle(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage);

    /**
     * Creates table which starts with name followed by header and ends with row containing cell with text starting with
     * given string.
     *
     * @param tableName     table name's row should contain cell which starts with given text
     * @param lastRowString table's last row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String lastRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, lastRowString, headerDescription, 1);
    }

    /**
     * Creates table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name's row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, headerDescription, 1);
    }

    /**
     * Creates table which starts with name followed by header and ends with row containing cell with text starting with
     * given string.
     *
     * @param tableName     table name's row should contain cell which starts with given text
     * @param lastRowString table's last row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String lastRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return create(reportPage, tableName, reportPage.getTableCellRange(tableName, headersRowCount, lastRowString), headerDescription, headersRowCount);
    }

    /**
     * Creates table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name's row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return create(reportPage, tableName, reportPage.getTableCellRange(tableName, headersRowCount), headerDescription, headersRowCount);
    }

    /**
     * Creates table. Table name containing row and last row will be found by predicate.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     * @param lastRowFinder   table's last row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> tableNameFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> lastRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates table. Table name containing row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> tableNameFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, headerDescription, 1);
    }

    /**
     * Creates table. Table name containing row and last row will be found by predicate.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     * @param lastRowFinder   table's last row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> tableNameFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> lastRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        String tableName = "<not found>";
        TableCellRange range = reportPage.getTableCellRange(tableNameFinder, headersRowCount, lastRowFinder);
        //noinspection DuplicatedCode
        if (!range.equals(TableCellRange.EMPTY_RANGE)) {
            TableCellAddress tableNameCell = reportPage.find(range.getFirstRow(), range.getFirstRow() + 1, tableNameFinder);
            if (!tableNameCell.equals(TableCellAddress.NOT_FOUND)) {
                try {
                    //noinspection ConstantConditions
                    tableName = reportPage.getCell(tableNameCell).getStringValue();
                } catch (Exception ignore) {
                }
            }
        }
        return create(reportPage, tableName, range, headerDescription, headersRowCount);
    }

    /**
     * Creates table. Table name containing row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> tableNameFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        String tableName = "<not found>";
        TableCellRange range = reportPage.getTableCellRange(tableNameFinder, headersRowCount);
        //noinspection DuplicatedCode
        if (!range.equals(TableCellRange.EMPTY_RANGE)) {
            TableCellAddress tableNameCell = reportPage.find(range.getFirstRow(), range.getFirstRow() + 1, tableNameFinder);
            if (!tableNameCell.equals(TableCellAddress.NOT_FOUND)) {
                try {
                    //noinspection ConstantConditions
                    tableName = reportPage.getCell(tableNameCell).getStringValue();
                } catch (Exception ignore) {
                }
            }
        }
        return create(reportPage, tableName, range, headerDescription, headersRowCount);
    }

    /**
     * Creates table without name which starts with header and ends with row containing cell with text starting with
     * given string.
     *
     * @param firstRowString table first row should contain cell which starts with given text
     * @param lastRowString  table's last row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String lastRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, lastRowString, headerDescription, 1);
    }

    /**
     * Creates table without name which starts with header and ends with empty row or last row of report page.
     *
     * @param firstRowString table first row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, headerDescription, 1);
    }

    /**
     * Creates table with predefined name which starts with header and ends with row containing cell with text starting
     * with given string.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowString    table first row should contain cell which starts with given text
     * @param lastRowString     table's last row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String providedTableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String lastRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return // add fantom first line for provided table name
        create(// add fantom first line for provided table name
        reportPage, // add fantom first line for provided table name
        providedTableName, // add fantom first line for provided table name
        reportPage.getTableCellRange(firstRowString, headersRowCount, lastRowString).addRowsToTop(1), headerDescription, headersRowCount);
    }

    /**
     * Creates table with predefined name which starts with header and ends with empty row or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowString    table first row should contain cell which starts with given text
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String providedTableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return // add fantom first line for provided table name
        create(// add fantom first line for provided table name
        reportPage, // add fantom first line for provided table name
        providedTableName, // add fantom first line for provided table name
        reportPage.getTableCellRange(firstRowString, headersRowCount).addRowsToTop(1), headerDescription, headersRowCount);
    }

    /**
     * Creates table without name. Table first and last row will be found by predicate.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     * @param lastRowFinder  table's last row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> lastRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates table without name. Table first row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, headerDescription, 1);
    }

    /**
     * Creates table with predefined name. Table first and last row will be found by predicate.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     * @param lastRowFinder     table's last row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String providedTableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> lastRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return // add fantom first line for provided table name
        create(// add fantom first line for provided table name
        reportPage, // add fantom first line for provided table name
        providedTableName, // add fantom first line for provided table name
        reportPage.getTableCellRange(firstRowFinder, headersRowCount, lastRowFinder).addRowsToTop(1), headerDescription, headersRowCount);
    }

    /**
     * Creates table with predefined name. Table first row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table createNameless(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String providedTableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        return // add fantom first line for provided table name
        create(// add fantom first line for provided table name
        reportPage, // add fantom first line for provided table name
        providedTableName, // add fantom first line for provided table name
        reportPage.getTableCellRange(firstRowFinder, headersRowCount).addRowsToTop(1), headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Pure
    Table create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReportPage reportPage, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tableName, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellRange tableRange, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends TableColumnDescription> headerDescription, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount);
}
