package org.spacious_team.table_wrapper.csv;
@NoArgsConstructor(access = PRIVATE)
final class CsvTableHelper {
    static TableCellAddress find(String[][] table, Object expected,
                                 int startRow, int endRow,
                                 int startColumn, int endColumn) {
        return find(table, startRow, endRow, startColumn, endColumn, equalsPredicate(expected));
    }
    static TableCellAddress find(String[][] table, int startRow, int endRow, int startColumn, int endColumn,
                                 Predicate<String> predicate) {
        startRow = Math.max(0, startRow);
        endRow = Math.min(endRow, table.length);
        for (int rowNum = startRow; rowNum < endRow; rowNum++) {
            String[] row = table[rowNum];
            TableCellAddress address = find(row, rowNum, startColumn, endColumn, predicate);
            if (address != NOT_FOUND) {
                return address;
            }
        }
        return NOT_FOUND;
    }
    static TableCellAddress find(String[] row, int rowNum, int startColumn, int endColumn, Predicate<String> predicate) {
        startColumn = Math.max(0, startColumn);
        endColumn = Math.min(endColumn, row.length);
        for (int i = startColumn; i < endColumn; i++) {
            String cell = row[i];
            if (predicate.test(cell)) {
                return TableCellAddress.of(rowNum, i);
            }
        }
        return NOT_FOUND;
    }
    static Predicate<String> equalsPredicate(Object expected) {
        if (expected == null) {
            return Objects::isNull;
        }
        String expectedString = expected.toString();
        return expectedString::equals;
    }
}
