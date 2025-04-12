package org.spacious_team.table_wrapper.api;
@ToString
@EqualsAndHashCode
public class TableColumnImpl implements TableColumn {
    private final String[] words;
    public static TableColumn of(String... words) {
        if (words.length == 0 || (words.length == 1 && (words[0] == null || words[0].isEmpty()))) {
            return LEFTMOST_COLUMN;
        }
        return new TableColumnImpl(words);
    }
    private TableColumnImpl(String... words) {
        this.words = Arrays.stream(words)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        for (ReportPageRow header : headerRows) {
            next_cell:
            for (  TableCell cell : header) {
                 Object value;
                if ((cell != null) && (cell.getColumnIndex() >= firstColumnForSearch) &&
                        (((value = cell.getValue()) != null) && (value instanceof String))) {
                    String colName = value.toString().toLowerCase();
                    for (String word : words) {
                        if (!containsWord(colName, word)) {
                            continue next_cell;
                        }
                    }
                    return cell.getColumnIndex();
                }
            }
        }
        throw new RuntimeException("Не обнаружен заголовок таблицы, включающий слова: " + String.join(", ", words));
    }
    private boolean containsWord(String text,  String word) {
        return word != null && text.matches("(^|(.|\\n)*\\b|(.|\\n)*\\s)" + word + "(\\b(.|\\n)*|\\s(.|\\n)*|$)");
    }
}
