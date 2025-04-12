package org.spacious_team.table_wrapper.api;
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class TableCellRange {
    public static final TableCellRange EMPTY_RANGE = new EmptyTableCellRange();
    private final int firstRow;
    private final int lastRow;
    private final int firstColumn;
    private final int lastColumn;
    public boolean contains(TableCellAddress address) {
        return containsRow(address.getRow()) && containsColumn(address.getColumn());
    }
    public boolean containsRow(int row) {
        return firstColumn <= row && row <= lastRow;
    }
    public boolean containsColumn(int column) {
        return firstColumn <= column && column <= lastColumn;
    }
    public TableCellRange addRowsToTop(int number) {
        return new TableCellRange(firstRow - number, lastRow, firstColumn, lastColumn);
    }
    public TableCellRange addRowsToBottom(int number) {
        return new TableCellRange(firstRow, lastRow + number, firstColumn, lastColumn);
    }
    public TableCellRange addColumnsToLeft(int number) {
        return new TableCellRange(firstRow, lastRow, firstColumn - number, lastColumn);
    }
    public TableCellRange addColumnsToRight(int number) {
        return new TableCellRange(firstRow, lastRow, firstColumn, lastColumn + number);
    }
    private static class EmptyTableCellRange extends TableCellRange {
        private EmptyTableCellRange() {
            super(0, 0, 0, 0);
        }
        @Override
        public boolean contains(TableCellAddress address) {
            return false;
        }
        @Override
        public boolean containsRow(int row) {
            return false;
        }
        @Override
        public boolean containsColumn(int column) {
            return false;
        }
        @Override
        public TableCellRange addRowsToTop(int number) {
            return this;
        }
        @Override
        public TableCellRange addRowsToBottom(int number) {
            return this;
        }
        @Override
        public TableCellRange addColumnsToLeft(int number) {
            return this;
        }
        @Override
        public TableCellRange addColumnsToRight(int number) {
            return this;
        }
    }
}
