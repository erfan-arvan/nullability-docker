package org.spacious_team.table_wrapper.api;
public interface TableColumn {
    int NOCOLUMN_INDEX = -1;
    TableColumn NOCOLUMN = (i, j) -> NOCOLUMN_INDEX;
    TableColumn LEFTMOST_COLUMN = (firstColumnForSearch, $) -> firstColumnForSearch;
    default int getColumnIndex(ReportPageRow... headerRows) {
        return getColumnIndex(0, headerRows);
    }
    int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows);
}
