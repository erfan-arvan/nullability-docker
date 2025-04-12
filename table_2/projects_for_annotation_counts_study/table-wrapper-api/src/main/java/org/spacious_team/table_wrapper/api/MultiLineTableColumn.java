package org.spacious_team.table_wrapper.api;
@ToString
@RequiredArgsConstructor
public class MultiLineTableColumn implements TableColumn {
    private final TableColumn[] rowDescriptors;
    public static MultiLineTableColumn of(TableColumn... rowDescriptors) {
        return new MultiLineTableColumn(rowDescriptors);
    }
    public static MultiLineTableColumn of(String... rowDescriptors) {
        return new MultiLineTableColumn(Arrays.stream(rowDescriptors)
                .map(TableColumnImpl::of)
                .toArray(TableColumn[]::new));
    }
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        if (headerRows.length != rowDescriptors.length) {
            throw new RuntimeException("Внутренняя ошибка, в таблице ожидается " + rowDescriptors.length +
                    " строк в заголовке");
        }
        int columnIndex = firstColumnForSearch;
        int i = 0;
        for (ReportPageRow row : headerRows) {
            TableColumn rowDescriptor = rowDescriptors[i++];
            columnIndex = rowDescriptor.getColumnIndex(columnIndex, row);
        }
        return columnIndex;
    }
}
