package org.spacious_team.table_wrapper.csv;
public class CsvCellDataAccessObject implements CellDataAccessObject<RowAndIndex, CsvTableRow> {
    public static final CsvCellDataAccessObject INSTANCE = new CsvCellDataAccessObject();
    private final  DateTimeFormatter dateTimeFormatter;
    private final  ZoneId defaultZone;
    public CsvCellDataAccessObject() {
        this(null, null);
    }
    public CsvCellDataAccessObject( DateTimeFormatter dateTimeFormatter,
                                    ZoneId defaultZone) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.defaultZone = defaultZone;
    }
    @Override
    public  RowAndIndex getCell(CsvTableRow row, Integer cellIndex) {
         CsvTableCell cell = row.getCell(cellIndex);
        return (cell == null) ? null : cell.getRowAndIndex();
    }
    @Override
    public  String getValue(RowAndIndex cell) {
        return cell.getValue();
    }
    @Override
    public Instant getInstantValue(RowAndIndex cell) {
         String value = getValue(cell);
        Objects.requireNonNull(value, "Not an instant");
        DateTimeFormatter formatter = (dateTimeFormatter != null) ?
                dateTimeFormatter :
                DateTimeFormatParser.getFor(value);
        LocalDateTime dateTime = (value.length() == 10) ?
                LocalDate.parse(value, formatter).atTime(12, 0) : 
                LocalDateTime.parse(value, formatter);
        return dateTime
                .atZone(defaultZone == null ? ZoneId.systemDefault() : defaultZone)
                .toInstant();
    }
}
