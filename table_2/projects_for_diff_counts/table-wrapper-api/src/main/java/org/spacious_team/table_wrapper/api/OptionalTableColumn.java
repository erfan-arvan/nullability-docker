package org.spacious_team.table_wrapper.api;
public class OptionalTableColumn implements TableColumn {
    public static TableColumn of(TableColumn column) {
        return AnyOfTableColumn.of(column, TableColumn.NOCOLUMN);
    }
    private OptionalTableColumn() {
    }
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        return TableColumn.NOCOLUMN_INDEX;
    }
}
