package org.spacious_team.table_wrapper.api;
@Data
class EmptyTableRow implements TableRow {
    private final Table table;
    private final int rowNum;
    @Override
    public TableCell getCell(TableColumnDescription column) {
        return null;
    }
    @Override
    public TableCell getCell(int i) {
        return null;
    }
    @Override
    public int getFirstCellNum() {
        return -1;
    }
    @Override
    public int getLastCellNum() {
        return -1;
    }
    @Override
    public boolean rowContains(Object expected) {
        return false;
    }
    @Override
    public Iterator<TableCell> iterator() {
        return emptyIterator();
    }
    @Override
    public Object getCellValue(TableColumnDescription column) {
        return null;
    }
    @Override
    public int getIntCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public long getLongCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public double getDoubleCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public BigDecimal getBigDecimalCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public String getStringCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public Instant getInstantCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column) {
        throw new NullPointerException("Cell not found");
    }
    @Override
    public TableRow clone() {
        try {
            return (TableRow) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone " + this.getClass().getName());
        }
    }
}
