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

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface ReportPageRow extends Iterable<TableCell> {

    /**
     * @param i zero-based cell number
     * @return cell ot null if cell does not exist
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    TableCell getCell(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int i);

    /**
     * Zero-based row number
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    int getRowNum();

    /**
     * Zero-based cell number or -1 if row doesn't contain cells
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    int getFirstCellNum();

    /**
     * @return Zero-based cell number or -1 if row doesn't contain cells
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    int getLastCellNum();

    /**
     * @param expected searching value
     * @return true if any cell of this row has exact value, false otherwise
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    boolean rowContains(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object expected);
}
