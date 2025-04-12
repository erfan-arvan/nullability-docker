package org.spacious_team.table_wrapper.csv;
public class CsvTableFactory extends AbstractTableFactory<CsvReportPage> {
    public CsvTableFactory() {
        super(CsvReportPage.class);
    }
    @Override
    public <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 TableCellRange tableRange,
                 Class<T> headerDescription,
                 int headersRowCount) {
        return new CsvTable(
                cast(reportPage),
                tableName,
                tableRange,
                headerDescription,
                headersRowCount);
    }
}
