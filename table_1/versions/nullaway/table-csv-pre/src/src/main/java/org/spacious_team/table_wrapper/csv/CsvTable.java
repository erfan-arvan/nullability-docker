/*
 * Table Wrapper CSV Impl
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

package org.spacious_team.table_wrapper.csv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.spacious_team.table_wrapper.api.AbstractReportPage;
import org.spacious_team.table_wrapper.api.AbstractTable;
import org.spacious_team.table_wrapper.api.CellDataAccessObject;
import org.spacious_team.table_wrapper.api.Table;
import org.spacious_team.table_wrapper.api.TableCellRange;
import org.spacious_team.table_wrapper.api.TableColumnDescription;

@ToString(callSuper = true)
public class CsvTable extends AbstractTable<CsvTableRow> {

    @Setter
    @Getter(AccessLevel.PROTECTED)
    private CellDataAccessObject<?, CsvTableRow> cellDataAccessObject = CsvCellDataAccessObject.INSTANCE;

    protected CsvTable(AbstractReportPage<CsvTableRow> reportPage,
                       String tableName,
                       TableCellRange tableRange,
                       Class<? extends TableColumnDescription> headerDescription,
                       int headersRowCount) {
        super(reportPage, tableName, tableRange, headerDescription, headersRowCount);
    }

    public CsvTable(AbstractTable<CsvTableRow> table, int appendDataRowsToTop, int appendDataRowsToBottom) {
        super(table, appendDataRowsToTop, appendDataRowsToBottom);
    }

    @Override
    public Table subTable(int topRows, int bottomRows) {
        return new CsvTable(this, topRows, bottomRows);
    }
}
