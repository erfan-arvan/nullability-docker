package org.spacious_team.table_wrapper.csv;
@ToString(callSuper = true)
public class CsvTable extends AbstractTable<CsvTableRow> {
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private CellDataAccessObject<?, CsvTableRow> cellDataAccessObject = CsvCellDataAccessObject.INSTANCE;
    protected <T extends Enum<T> & TableHeaderColumn>
    CsvTable(AbstractReportPage<CsvTableRow> reportPage,
             String tableName,
             TableCellRange tableRange,
             Class<T> headerDescription,
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
