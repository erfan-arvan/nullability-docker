/*
 * Table Wrapper API
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
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

import lombok.Data;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;
import static java.util.Collections.emptyIterator;

@Data
class EmptyTableRow implements TableRow {

    private final Table table;

    private final int rowNum;

    @Override
    public TableCell getCell(@Nullable() TableColumnDescription column) {
        return null;
    }

    @Override
    public TableCell getCell(@Nullable() int i) {
        return null;
    }

    @Override
    public int getFirstCellNum() {
        return -1;
    }

    @Override
    public int getLastCellNum() {
        return -1;
    }

    @Override
    public boolean rowContains(@Nullable() Object expected) {
        return false;
    }

    @Override
    public Iterator<TableCell> iterator() {
        return emptyIterator();
    }

    @Override
    public Object getCellValue(@Nullable() TableColumnDescription column) {
        return null;
    }

    @Override
    public int getIntCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public long getLongCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public double getDoubleCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public BigDecimal getBigDecimalCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public String getStringCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public Instant getInstantCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public LocalDateTime getLocalDateTimeCellValue(@Nullable() TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }

    @Override
    public TableRow clone() {
        try {
            return (TableRow) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone " + this.getClass().getName());
        }
    }
}
