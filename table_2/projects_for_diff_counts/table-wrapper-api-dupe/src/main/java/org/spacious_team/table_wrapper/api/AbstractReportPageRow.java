package org.spacious_team.table_wrapper.api;
public abstract class AbstractReportPageRow implements ReportPageRow {
    @RequiredArgsConstructor
    protected static class ReportPageRowIterator<T> implements Iterator<TableCell> {
        private final Iterator<T> innerIterator;
        private final Function<T, TableCell> converter;
        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }
        @Override
        public TableCell next() {
            return converter.apply(innerIterator.next());
        }
    }
}