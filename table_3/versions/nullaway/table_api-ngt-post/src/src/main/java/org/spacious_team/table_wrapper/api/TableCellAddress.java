// Generated by delombok at Tue Aug 20 15:02:38 EDT 2024
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

public class TableCellAddress {

    public static final TableCellAddress NOT_FOUND = new TableCellAddress(-1, -1);

    private final int row;

    private final int column;

    @java.lang.SuppressWarnings("all")
    public int getRow() {
        return this.row;
    }

    @java.lang.SuppressWarnings("all")
    public int getColumn() {
        return this.column;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TableCellAddress))
            return false;
        final TableCellAddress other = (TableCellAddress) o;
        if (!other.canEqual((java.lang.Object) this))
            return false;
        if (this.getRow() != other.getRow())
            return false;
        if (this.getColumn() != other.getColumn())
            return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof TableCellAddress;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRow();
        result = result * PRIME + this.getColumn();
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "TableCellAddress(row=" + this.getRow() + ", column=" + this.getColumn() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public TableCellAddress(final int row, final int column) {
        this.row = row;
        this.column = column;
    }
}
