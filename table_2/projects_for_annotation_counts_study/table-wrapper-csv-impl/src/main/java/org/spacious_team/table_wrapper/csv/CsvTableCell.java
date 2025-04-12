package org.spacious_team.table_wrapper.csv;
@EqualsAndHashCode(of = {"rowAndIndex"}, callSuper = false)
public class CsvTableCell extends AbstractTableCell<CsvTableCell.RowAndIndex> {
    @Getter(PACKAGE)
    private final RowAndIndex rowAndIndex;
    public static CsvTableCell of(String[] row, int columnIndex) {
        return new CsvTableCell(new RowAndIndex(row, columnIndex));
    }
    public static CsvTableCell of(String[] row, int columnIndex, CsvCellDataAccessObject dao) {
        return new CsvTableCell(new RowAndIndex(row, columnIndex), dao);
    }
    public CsvTableCell(RowAndIndex rowAndIndex) {
        this(rowAndIndex, CsvCellDataAccessObject.INSTANCE);
    }
    public CsvTableCell(RowAndIndex rowAndIndex, CsvCellDataAccessObject dao) {
        super(rowAndIndex, dao);
        this.rowAndIndex = rowAndIndex;
    }
    @Override
    public int getColumnIndex() {
        return rowAndIndex.getColumnIndex();
    }
    @RequiredArgsConstructor
    static final class RowAndIndex {
        private final String[] row;
        @Getter
        private final int columnIndex;
        String getValue() {
            return checkIndex() ? row[columnIndex] : null;
        }
        private boolean checkIndex() {
            return columnIndex >= 0 && columnIndex < row.length;
        }
        @Override
        public boolean equals( Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof RowAndIndex)) {
                return false;
            }
            RowAndIndex other = (RowAndIndex) obj;
            return checkIndex() &&
                    other.checkIndex() &&
                    Objects.equals(getValue(), other.getValue());
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(getValue());
        }
    }
}
