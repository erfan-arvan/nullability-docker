package org.spacious_team.table_wrapper.api;
@Slf4j
@ToString(of = {"tableName"})
public abstract class AbstractTable<R extends ReportPageRow> implements Table {
    @Getter
    protected final AbstractReportPage<R> reportPage;
    protected final String tableName;
    @Getter
    private final TableCellRange tableRange;
    @Getter
    private final Map<TableColumn, Integer> headerDescription;
    @Getter
    private final boolean empty;
    private final int dataRowOffset;
    protected AbstractTable(AbstractReportPage<R> reportPage,
                            String tableName,
                            TableCellRange tableRange,
                            Class<? extends TableColumnDescription> headerDescription,
                            int headersRowCount) {
        this.reportPage = reportPage;
        this.tableName = tableName;
        this.dataRowOffset = 1 + headersRowCount; 
        this.empty = isEmpty(tableRange, dataRowOffset);
        this.headerDescription = this.empty ?
                Collections.emptyMap() :
                getHeaderDescription(reportPage, tableRange, headerDescription, headersRowCount);
        this.tableRange = empty ?
                tableRange :
                new TableCellRange(
                        tableRange.getFirstRow(),
                        tableRange.getLastRow(),
                        getColumnIndices(this.headerDescription).min().orElse(tableRange.getFirstColumn()),
                        getColumnIndices(this.headerDescription).max().orElse(tableRange.getLastColumn()));
    }
    protected AbstractTable(AbstractTable<R> table, int appendDataRowsToTop, int appendDataRowsToBottom) {
        this.reportPage = table.reportPage;
        this.tableName = table.tableName;
        this.tableRange = table.tableRange.addRowsToTop(appendDataRowsToTop).addRowsToBottom(appendDataRowsToBottom);
        this.dataRowOffset = table.dataRowOffset;
        this.empty = isEmpty(tableRange, dataRowOffset);
        this.headerDescription = table.headerDescription;
    }
    private static boolean isEmpty(TableCellRange tableRange, int dataRowOffset) {
        return tableRange.equals(TableCellRange.EMPTY_RANGE) ||
                (getNumberOfTableRows(tableRange) - dataRowOffset) <= 0;
    }
    private static int getNumberOfTableRows(TableCellRange tableRange) {
        return tableRange.getLastRow() - tableRange.getFirstRow() + 1;
    }
    private static Map<TableColumn, Integer> getHeaderDescription(AbstractReportPage<?> reportPage, TableCellRange tableRange,
                                                                  Class<? extends TableColumnDescription> headerDescription,
                                                                  int headersRowCount) {
        Map<TableColumn, Integer> columnIndices = new HashMap<>();
        ReportPageRow[] headerRows = new ReportPageRow[headersRowCount];
        for (int i = 0; i < headersRowCount; i++) {
             ReportPageRow row = reportPage.getRow(tableRange.getFirstRow() + 1 + i);
            ReportPageRow notNullRow = requireNonNull(row, "Header row is absent");
            headerRows[i] = notNullRow;
        }
        TableColumn[] columns = Arrays.stream(headerDescription.getEnumConstants())
                .map(TableColumnDescription::getColumn)
                .toArray(TableColumn[]::new);
        for (TableColumn column : columns) {
            columnIndices.put(column, column.getColumnIndex(headerRows));
        }
        return Collections.unmodifiableMap(columnIndices);
    }
    private static IntStream getColumnIndices(Map<TableColumn, Integer> headerDescription) {
        return headerDescription.values()
                .stream()
                .mapToInt(i -> i)
                .filter(i -> i != TableColumn.NOCOLUMN_INDEX);
    }
    public <T> List<T> getData(Object report, Function<TableRow,  T> rowExtractor) {
        return getDataCollection(report, (row, data) -> {
             T result = rowExtractor.apply(row);
            if (result != null) {
                data.add(result);
            }
        });
    }
    public <T> List<T> getDataCollection(Object report, Function<TableRow,  Collection<T>> rowExtractor) {
        return getDataCollection(report, (row, data) -> {
             Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                data.addAll(result);
            }
        });
    }
    public <T> List<T> getDataCollection(Object report, Function<TableRow,  Collection<T>> rowExtractor,
                                         BiPredicate<T, T> equalityChecker,
                                         BiFunction<T, T,  Collection<T>> mergeDuplicates) {
        return getDataCollection(report, (row, data) -> {
             Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                for (T r : result) {
                    addWithEqualityChecker(r, data, equalityChecker, mergeDuplicates);
                }
            }
        });
    }
    private <T> List<T> getDataCollection(Object report, BiConsumer<TableRow, Collection<T>> rowHandler) {
        List<T> data = new ArrayList<>();
        for (  TableRow row : this) {
            if (row != null) {
                try {
                    rowHandler.accept(row, data);
                } catch (Exception e) {
                    log.warn("Не могу распарсить таблицу '{}' в {}, строка {}",
                            tableName, report, row.getRowNum() + 1, e);
                }
            }
        }
        return data;
    }
    public static <T> void addWithEqualityChecker(T element,
                                                  Collection<T> collection,
                                                  BiPredicate<T, T> equalityChecker,
                                                  BiFunction<T, T,  Collection<T>> duplicatesMerger) {
         T equalsObject = null;
        for (T e : collection) {
            if (equalityChecker.test(e, element)) {
                equalsObject = e;
                break;
            }
        }
        if (equalsObject != null) {
            collection.remove(equalsObject);
             Collection<T> mergedCollection = duplicatesMerger.apply(equalsObject, element);
            if (mergedCollection != null) {
                collection.addAll(mergedCollection);
            }
        } else {
            collection.add(element);
        }
    }
    @Override
    public Stream<TableRow> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    @Override
    public Iterator<TableRow> iterator() {
        return new TableIterator();
    }
    protected class TableIterator implements Iterator<TableRow> {
        private final MutableTableRow<R> tableRow =
                new MutableTableRow<>(AbstractTable.this, getCellDataAccessObject());
        private final int numberOfRows = getNumberOfTableRows(tableRange);
        private int i = dataRowOffset;
        @Override
        public boolean hasNext() {
            return i < numberOfRows;
        }
        @Override
        public TableRow next() {
            int rowNum;
             R row;
            do {
                rowNum = tableRange.getFirstRow() + (i++);
                row = getRow(rowNum);
            } while (row == null && hasNext());
            if (row == null) { 
                return new EmptyTableRow(AbstractTable.this, rowNum);
            }
            tableRow.setRow(row);
            return tableRow;
        }
    }
    @Override
    public R getRow(int i) {
        return reportPage.getRow(i);
    }
    @Override
    public TableRow findRow(Object value) {
        TableCellAddress address = reportPage.find(value);
        return getMutableTableRow(address);
    }
    @Override
    public TableRow findRowByPrefix(String prefix) {
        TableCellAddress address = reportPage.findByPrefix(prefix);
        return getMutableTableRow(address);
    }
    private MutableTableRow<R> getMutableTableRow(TableCellAddress address) {
        if (tableRange.contains(address)) {
            MutableTableRow<R> tableRow = new MutableTableRow<>(this, getCellDataAccessObject());
            R row = requireNonNull(getRow(address.getRow()), "Row is empty");
            tableRow.setRow(row);
            return tableRow;
        }
        return null;
    }
    protected abstract CellDataAccessObject<?, R> getCellDataAccessObject();
}