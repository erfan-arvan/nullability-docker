package org.spacious_team.table_wrapper.csv;
@EqualsAndHashCode(of ={"row", "rowNum"}, callSuper = false)
public class CsvTableRow extends AbstractReportPageRow {
    private final String[] row;
    @Getter
    private final int rowNum;
    private final CsvTableCell[] cellsCache;
    public CsvTableRow(String[] row, int rowNum) {
        this.row = row;
        this.rowNum = rowNum;
        this.cellsCache = new CsvTableCell[row.length];
    }
    @Override
    public  CsvTableCell getCell(int i) {
        if (i >= row.length) {
            return null;
        }
        CsvTableCell cell = cellsCache[i];
        if (cell == null) {
            cell = CsvTableCell.of(row, i);
            cellsCache[i] = cell;
        }
        return cell;
    }
    @Override
    public int getFirstCellNum() {
        return (row.length > 0) ? 0 : -1;
    }
    @Override
    public int getLastCellNum() {
        return row.length - 1;
    }
    @Override
    public boolean rowContains(Object value) {
        return CsvTableHelper.find(row, rowNum, 0, row.length, equalsPredicate(value)) != NOT_FOUND;
    }
    @Override
    public Iterator< TableCell> iterator() {
        return new Iterator< TableCell>() {
            private int cellIndex = 0;
            @Override
            public boolean hasNext() {
                return cellIndex < row.length;
            }
            @Override
            public  TableCell next() {
                if (hasNext()) {
                    return getCell(cellIndex++);
                }
                throw new NoSuchElementException();
            }
        };
    }
}
