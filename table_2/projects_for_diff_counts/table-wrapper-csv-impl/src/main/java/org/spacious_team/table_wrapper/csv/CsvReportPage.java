package org.spacious_team.table_wrapper.csv;
public class CsvReportPage extends AbstractReportPage<CsvTableRow> {
    private final String[][] rows;
    public CsvReportPage(Path path) throws IOException {
        this(Files.newInputStream(path, StandardOpenOption.READ), UTF_8, getDefaultCsvParserSettings());
    }
    public CsvReportPage(InputStream inputStream, Charset charset, CsvParserSettings csvParserSettings) throws IOException {
        try (Reader inputReader = new InputStreamReader(inputStream, charset)) {
            CsvParser parser = new CsvParser(csvParserSettings);
            rows = parser.parseAll(inputReader).toArray(new String[0][]);
        }
    }
    public CsvReportPage(String[][] cells) {
        this.rows = cells;
    }
    public static CsvParserSettings getDefaultCsvParserSettings() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();
        return settings;
    }
    @Override
    public TableCellAddress find(Object value, int startRow, int endRow, int startColumn, int endColumn) {
        return CsvTableHelper.find(rows, value, startRow, endRow, startColumn, endColumn);
    }
    @Override
    public TableCellAddress find(int startRow, int endRow, int startColumn, int endColumn, Predicate<Object> cellValuePredicate) {
        return CsvTableHelper.find(rows, startRow, endRow, startColumn, endColumn, cellValuePredicate::test);
    }
    @Override
    public  CsvTableRow getRow(int i) {
        return (i >= rows.length) ? null : new CsvTableRow(rows[i], i);
    }
    @Override
    public int getLastRowNum() {
        return rows.length - 1;
    }
}
