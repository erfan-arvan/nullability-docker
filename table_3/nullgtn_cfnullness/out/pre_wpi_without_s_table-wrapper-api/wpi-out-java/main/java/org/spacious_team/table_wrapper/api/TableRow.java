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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * {@link TableRow} subclass can be mutable. Use {@link #clone()} to make copy.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface TableRow extends ReportPageRow, Cloneable {

    @org.checkerframework.dataflow.qual.Pure
    Table getTable();

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    TableCell getCell(@Nullable() TableColumnDescription column);

    /**
     * Returns cell's native value
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    Object getCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract int value
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    int getIntCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract long value
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    long getLongCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract Double value
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    double getDoubleCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract BigDecimal value
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    BigDecimal getBigDecimalCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract string value
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    String getStringCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract instant value
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    Instant getInstantCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract local date time value
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    LocalDateTime getLocalDateTimeCellValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TableColumnDescription column);

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    default Object getCellValueOrDefault(TableColumnDescription column, Object defaultValue) {
        try {
            return getCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default int getIntCellValueOrDefault(TableColumnDescription column, int defaultValue) {
        try {
            return getIntCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default long getLongCellValueOrDefault(TableColumnDescription column, long defaultValue) {
        try {
            return getLongCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default double getDoubleCellValue(TableColumnDescription column, double defaultValue) {
        try {
            return getDoubleCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default BigDecimal getBigDecimalCellValueOrDefault(TableColumnDescription column, BigDecimal defaultValue) {
        try {
            return getBigDecimalCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default String getStringCellValueOrDefault(TableColumnDescription column, String defaultValue) {
        try {
            return getStringCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default Instant getInstantCellValueOrDefault(TableColumnDescription column, Instant defaultValue) {
        try {
            return getInstantCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default LocalDateTime getLocalDateTimeCellValueOrDefault(TableColumnDescription column, LocalDateTime defaultValue) {
        try {
            return getLocalDateTimeCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    TableRow clone() throws CloneNotSupportedException;
}
