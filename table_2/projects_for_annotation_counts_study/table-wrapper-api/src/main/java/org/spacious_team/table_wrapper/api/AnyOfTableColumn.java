package org.spacious_team.table_wrapper.api;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class AnyOfTableColumn implements TableColumn {
    private final TableColumn[] columns;
    public static TableColumn of(TableColumn... columns) {
        return new AnyOfTableColumn(columns);
    }
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        for (TableColumn c : columns) {
            try {
                return c.getColumnIndex(firstColumnForSearch, headerRows);
            } catch (RuntimeException ignore) {
            }
        }
        throw new RuntimeException("Не обнаружен заголовок таблицы, включающий: " + String.join(", ",
                Arrays.stream(columns)
                        .map(TableColumn::toString)
                        .toArray(String[]::new)));
    }
}
