package org.spacious_team.table_wrapper.api;
@Data
class MutableTableRow<T extends ReportPageRow> implements TableRow {
    private final Table table;
    private final CellDataAccessObject<?, T> dao;
    @Setter(AccessLevel.PACKAGE)
    private volatile T row;
    public TableCell getCell(TableColumnDescription column) {
        return getCell(getCellIndex(column));
    }
    @Override
    public TableCell getCell(int i) {
        return row.getCell(i);
    }
    @Override
    public int getRowNum() {
        return row.getRowNum();
    }
    @Override
    public int getFirstCellNum() {
        return row.getFirstCellNum();
    }
    @Override
    public int getLastCellNum() {
        return row.getLastCellNum();
    }
    @Override
    public boolean rowContains(Object expected) {
        return row.rowContains(expected);
    }
    @Override
    public Iterator<TableCell> iterator() {
        return row.iterator();
    }
    public Object getCellValue(TableColumnDescription column) {
        return dao.getValue(row, getCellIndex(column));
    }
    public int getIntCellValue(TableColumnDescription column) {
        return dao.getIntValue(row, getCellIndex(column));
    }
    public long getLongCellValue(TableColumnDescription column) {
        return dao.getLongValue(row, getCellIndex(column));
    }
    public double getDoubleCellValue(TableColumnDescription column) {
        return dao.getDoubleValue(row, getCellIndex(column));
    }
    public BigDecimal getBigDecimalCellValue(TableColumnDescription column) {
        return dao.getBigDecimalValue(row, getCellIndex(column));
    }
    public String getStringCellValue(TableColumnDescription column) {
        return dao.getStringValue(row, getCellIndex(column));
    }
    public Instant getInstantCellValue(TableColumnDescription column) {
        return dao.getInstantValue(row, getCellIndex(column));
    }
    public LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column) {
        return dao.getLocalDateTimeValue(row, getCellIndex(column));
    }
    private Integer getCellIndex(TableColumnDescription column) {
         Integer cellIndex = table.getHeaderDescription()
                .get(column.getColumn());
        return requireNonNull(cellIndex, "Cell not found");
    }
    public MutableTableRow<T> clone() {
        try {
            return (MutableTableRow<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone " + this.getClass().getName());
        }
    }
}
