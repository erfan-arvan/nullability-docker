package org.spacious_team.table_wrapper.api;
public interface Table extends Iterable<TableRow> {
    ReportPage getReportPage();
    default <T> List<T> getData(Function<TableRow, T> rowExtractor) {
        return getData("unknown", rowExtractor);
    }
    <T> List<T> getData(Object report, Function<TableRow, T> rowExtractor);
    default <T> List<T> getDataCollection(Function<TableRow, Collection<T>> rowExtractor) {
        return getDataCollection("unknown", rowExtractor);
    }
    <T> List<T> getDataCollection(Object report, Function<TableRow, Collection<T>> rowExtractor);
    <T> List<T> getDataCollection(Object report, Function<TableRow, Collection<T>> rowExtractor,
                                  BiPredicate<T, T> equalityChecker,
                                  BiFunction<T, T, Collection<T>> mergeDuplicates);
    boolean isEmpty();
    Stream<TableRow> stream();
    ReportPageRow getRow(int i);
    TableRow findRow(Object value);
    TableRow findRowByPrefix(String prefix);
    Map<TableColumn, Integer> getHeaderDescription();
    default Table excludeTotalRow() {
        return subTable(0, -1);
    }
    Table subTable(int topRows, int bottomRows);
}
