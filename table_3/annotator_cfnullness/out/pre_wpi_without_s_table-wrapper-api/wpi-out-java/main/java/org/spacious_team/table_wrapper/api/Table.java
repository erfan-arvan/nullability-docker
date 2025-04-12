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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface Table extends Iterable<TableRow> {

    /**
     * Report page this table belongs to
     */
    @org.checkerframework.dataflow.qual.Pure
    ReportPage getReportPage();

    /**
     * Extracts exactly one object from excel row
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default <T> List<T> getData(Function<TableRow, T> rowExtractor) {
        return getData("unknown", rowExtractor);
    }

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    <T> List<T> getData(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object report, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Function<TableRow, T> rowExtractor);

    /**
     * Extracts objects from table without duplicate objects handling (duplicated row are both will be returned)
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default <T> List<T> getDataCollection(Function<TableRow, Collection<T>> rowExtractor) {
        return getDataCollection("unknown", rowExtractor);
    }

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    <T> List<T> getDataCollection(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object report, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Function<TableRow, Collection<T>> rowExtractor);

    /**
     * Extracts objects from table with duplicate objects handling logic
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    <T> List<T> getDataCollection(@Nullable() Object report, @Nullable() Function<TableRow, Collection<T>> rowExtractor, @Nullable() BiPredicate<T, T> equalityChecker, @Nullable() BiFunction<T, T, Collection<T>> mergeDuplicates);

    @org.checkerframework.dataflow.qual.Pure
    boolean isEmpty();

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    Stream<TableRow> stream();

    /**
     * @param i zero-based index
     * @return row object or null is row does not exist
     * @apiNote Method impl should return {@link CellDataAccessObject} aware {@link ReportPageRow} impl
     */
    @org.checkerframework.dataflow.qual.Pure
    ReportPageRow getRow(@Nullable() int i);

    /**
     * @return row containing cell with exact value or null if not found
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    TableRow findRow(Object value);

    /**
     * @return row containing cell starting with prefix or null if not found
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    TableRow findRowByPrefix(String prefix);

    @org.checkerframework.dataflow.qual.Pure
    Map<TableColumn, Integer> getHeaderDescription();

    /**
     * By default, table iterates throw all rows, call method if last row is "total" row, and it should be excluded
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Table excludeTotalRow() {
        return subTable(0, -1);
    }

    /**
     * Returns table with same columns but without some top and bottom data rows. For example use
     * <pre>
     *     subTable(0, -1)
     * </pre>
     * for exclude last "Total" row from iterator or stream.
     * @param topRows positive value for inclusion, negative for exclusion
     * @param bottomRows positive value for inclusion, negative for exclusion
     */
    @org.checkerframework.dataflow.qual.Pure
    Table subTable(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int topRows, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int bottomRows);
}
