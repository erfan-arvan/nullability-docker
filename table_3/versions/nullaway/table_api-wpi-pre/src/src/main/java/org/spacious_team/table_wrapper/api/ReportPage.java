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

import javax.annotation.Nullable;
import java.util.function.Predicate;
import static java.util.Objects.requireNonNull;
import static org.spacious_team.table_wrapper.api.ReportPageHelper.getCellStringValueIgnoreCasePrefixPredicate;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface ReportPage {

    /**
     * Finds cell address containing exact value.
     *
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object value) {
        return find(value, 0);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object value,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow) {
        return find(value, startRow, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object value,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow) {
        return find(value, startRow, endRow, 0, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param value       searching value
     * @param startRow    search rows start from this
     * @param endRow      search rows excluding this, can handle values greater than real rows count
     * @param startColumn search columns start from this
     * @param endColumn   search columns excluding this, can handle values greater than real columns count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    TableCellAddress find(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object value,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startColumn,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endColumn);

    /**
     * Finds cell by predicate.
     *
     * @param cellValuePredicate predicate for testing cell value
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> cellValuePredicate) {
        return find(0, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> cellValuePredicate) {
        return find(startRow, Integer.MAX_VALUE, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress find( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> cellValuePredicate) {
        return find(startRow, endRow, 0, Integer.MAX_VALUE, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow           search rows start from this
     * @param endRow             search rows excluding this, can handle values greater than real rows count
     * @param startColumn        search columns start from this
     * @param endColumn          search columns excluding this, can handle values greater than real columns count
     * @param cellValuePredicate predicate for testing cell value
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.Pure
    TableCellAddress find( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startColumn,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endColumn, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> cellValuePredicate);

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress findByPrefix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String prefix) {
        return findByPrefix(prefix, 0);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress findByPrefix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String prefix,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow) {
        return findByPrefix(prefix, startRow, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress findByPrefix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String prefix,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow) {
        return findByPrefix(prefix, startRow, endRow, 0, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow    search rows start from this
     * @param endRow      search rows excluding this, can handle values greater than real rows count
     * @param startColumn search columns start from this
     * @param endColumn   search columns excluding this, can handle values greater than real columns count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress findByPrefix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String prefix,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endRow,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startColumn,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int endColumn) {
        return prefix == null ? TableCellAddress.NOT_FOUND : find(startRow, endRow, startColumn, endColumn, getCellStringValueIgnoreCasePrefixPredicate(prefix));
    }

    /**
     * For vertical table of key-value records (table with two columns), search and return value for requested key.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object getNextColumnValue(String firstColumnValuePrefix) {
        TableCellAddress address = findByPrefix(firstColumnValuePrefix);
        ReportPageRow row = getRow(address.getRow());
        if (row != null) {
            for (TableCell cell : row) {
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

    /**
     * @param i zero-based index
     * @return row object or null is row does not exist
     * @apiNote Method impl should return {@link CellDataAccessObject} aware {@link ReportPageRow} impl
     */
    @org.checkerframework.dataflow.qual.Pure
    ReportPageRow getRow( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int i);

    /**
     * @return last row contained on this page (zero-based) or -1 if no row exists
     */
    @org.checkerframework.dataflow.qual.Pure
    int getLastRowNum();

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable TableCell getCell(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellAddress address) {
        ReportPageRow row = getRow(address.getRow());
        return (row == null) ? null : row.getCell(address.getColumn());
    }

    /**
     * Returns table range. Table's first row starts with 'firstRowPrefix' prefix in one of the cells
     * and table ends with predefined prefix in one of the last row cells.
     */
    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellRange getTableCellRange(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowPrefix,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String lastRowPrefix) {
        if (firstRowPrefix == null || lastRowPrefix == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(getCellStringValueIgnoreCasePrefixPredicate(firstRowPrefix), headersRowCount, getCellStringValueIgnoreCasePrefixPredicate(lastRowPrefix));
    }

    /**
     * Returns table range. First and last row will be found by predicate.
     */
    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellRange getTableCellRange(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> lastRowFinder) {
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
        return new TableCellRange(startAddress.getRow(), endAddress.getRow(), firstRow.getFirstCellNum(), lastRow.getLastCellNum());
    }

    /**
     * Returns table range. First row starts with 'firstRowPrefix' prefix in one of the cells,
     * range ends with empty row or last row of report page.
     */
    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellRange getTableCellRange(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String firstRowPrefix,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
        if (firstRowPrefix == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(getCellStringValueIgnoreCasePrefixPredicate(firstRowPrefix), headersRowCount);
    }

    /**
     * Returns table range. First row will be found by predicate, range ends with empty row or last row of report page.
     */
    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableCellRange getTableCellRange(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Predicate<Object> firstRowFinder,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int headersRowCount) {
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
            // empty row not found
            lastRowNum = getLastRowNum();
        } else if (lastRowNum <= getLastRowNum()) {
            // exclude last row from table
            lastRowNum--;
        }
        if (lastRowNum < startAddress.getRow()) {
            lastRowNum = startAddress.getRow();
        }
        ReportPageRow lastRow = requireNonNull(getRow(lastRowNum), "Row not found");
        return new TableCellRange(startAddress.getRow(), lastRowNum, firstRow.getFirstCellNum(), lastRow.getLastCellNum());
    }

    /**
     * Returns zero-based index of empty row.
     * This implementation generates a huge amount of garbage. May be overridden for improve performance.
     *
     * @param startRow first row for check
     * @return index of first empty row or -1 if not found
     */
    @org.checkerframework.dataflow.qual.Impure
    default  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int findEmptyRow( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int startRow) {
        int lastRowNum = startRow;
        LAST_ROW: for (int n = getLastRowNum(); lastRowNum <= n; lastRowNum++) {
            ReportPageRow row = getRow(lastRowNum);
            if (row == null || row.getLastCellNum() == -1) {
                // all row cells blank
                return lastRowNum;
            }
            for (TableCell cell : row) {
                Object value;
                if (!(cell == null || ((value = cell.getValue()) == null) || (value instanceof String) && (value.toString().isEmpty()))) {
                    // not empty
                    continue LAST_ROW;
                }
            }
            // all row cells blank
            return lastRowNum;
        }
        return -1;
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(String tableName, String tableFooterString, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).create(this, tableName, tableFooterString, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(String tableName, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).create(this, tableName, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(String tableName, String tableFooterString, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).create(this, tableName, tableFooterString, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(String tableName, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).create(this, tableName, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(Predicate<Object> tableNameFinder, Predicate<Object> tableFooterFinder, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).create(this, tableNameFinder, tableFooterFinder, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(Predicate<Object> tableNameFinder, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).create(this, tableNameFinder, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(Predicate<Object> tableNameFinder, Predicate<Object> tableFooterFinder, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).create(this, tableNameFinder, tableFooterFinder, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table create(Predicate<Object> tableNameFinder, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).create(this, tableNameFinder, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String firstLineText, String lastRowString, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).createNameless(this, firstLineText, lastRowString, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String firstLineText, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).createNameless(this, firstLineText, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String providedTableName, String firstLineText, String lastRowString, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).createNameless(this, providedTableName, firstLineText, lastRowString, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String providedTableName, String firstLineText, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).createNameless(this, providedTableName, firstLineText, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(Predicate<Object> firstLineFinder, Predicate<Object> lastRowFinder, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).createNameless(this, firstLineFinder, lastRowFinder, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(Predicate<Object> firstLineFinder, Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this).createNameless(this, firstLineFinder, headerDescription);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String providedTableName, Predicate<Object> firstLineFinder, Predicate<Object> lastRowFinder, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).createNameless(this, providedTableName, firstLineFinder, lastRowFinder, headerDescription, headersRowCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    default @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Table createNameless(String providedTableName, Predicate<Object> firstLineFinder, Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        return TableFactoryRegistry.get(this).createNameless(this, providedTableName, firstLineFinder, headerDescription, headersRowCount);
    }
}
