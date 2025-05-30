// Generated by delombok at Mon Aug 26 03:21:05 EDT 2024
/*
 * Table Wrapper API
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.spacious_team.table_wrapper.api;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TableColumnImpl implements TableColumn {

    private final String[] words;

    public static TableColumn of(String... words) {
        if (words.length == 0 || (words.length == 1 && (words[0] == null || words[0].isEmpty()))) {
            return LEFTMOST_COLUMN;
        }
        return new TableColumnImpl(words);
    }

    private TableColumnImpl(String... words) {
        this.words = Arrays.stream(words).map(String::toLowerCase).toArray(String[]::new);
    }

    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        for (ReportPageRow header : headerRows) {
            next_cell: for (TableCell cell : header) {
                Object value;
                if (cell != null && cell.getColumnIndex() >= firstColumnForSearch && ((value = cell.getValue()) instanceof String)) {
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

    private boolean containsWord(String text, String word) {
        return text.matches("(^|(.|\\n)*\\b|(.|\\n)*\\s)" + word + "(\\b(.|\\n)*|\\s(.|\\n)*|$)");
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "TableColumnImpl(words=" + java.util.Arrays.deepToString(this.words) + ")";
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TableColumnImpl))
            return false;
        final TableColumnImpl other = (TableColumnImpl) o;
        if (!other.canEqual((java.lang.Object) this))
            return false;
        if (!java.util.Arrays.deepEquals(this.words, other.words))
            return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof TableColumnImpl;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.deepHashCode(this.words);
        return result;
    }
}
