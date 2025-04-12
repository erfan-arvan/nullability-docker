package org.spacious_team.table_wrapper.api;
public interface ReportPageRow extends Iterable<TableCell> {
    TableCell getCell(int i);
    int getRowNum();
    int getFirstCellNum();
    int getLastCellNum();
    boolean rowContains(Object expected);
}