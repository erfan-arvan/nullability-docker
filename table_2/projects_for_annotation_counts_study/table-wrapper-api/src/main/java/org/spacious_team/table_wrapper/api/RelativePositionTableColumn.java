package org.spacious_team.table_wrapper.api;
@ToString
@RequiredArgsConstructor(staticName = "of")
public class RelativePositionTableColumn implements TableColumn {
    private final TableColumn relatedTableColumn;
    private final int relatedOffset;
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        return relatedTableColumn.getColumnIndex(firstColumnForSearch, headerRows) + relatedOffset;
    }
}
