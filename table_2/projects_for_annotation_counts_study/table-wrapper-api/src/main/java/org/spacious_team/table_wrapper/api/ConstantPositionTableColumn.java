package org.spacious_team.table_wrapper.api;
@ToString
@RequiredArgsConstructor(staticName = "of")
public class ConstantPositionTableColumn implements TableColumn {
    private final int columnIndex;
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        return columnIndex;
    }
}
