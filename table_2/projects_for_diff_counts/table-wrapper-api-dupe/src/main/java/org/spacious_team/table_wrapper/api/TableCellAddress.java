package org.spacious_team.table_wrapper.api;
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class TableCellAddress {
    public static final TableCellAddress NOT_FOUND = new TableCellAddress(-1, -1);
    private final int row;
    private final int column;
}
