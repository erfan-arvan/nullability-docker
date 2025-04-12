package org.spacious_team.table_wrapper.api;
class ReportPageHelper {
    static Predicate<Object> getCellStringValueIgnoreCasePrefixPredicate(String prefix) {
        String lowercasePrefix = prefix.trim().toLowerCase();
        return (cell) -> (cell instanceof String) &&
                ((String) cell).trim().toLowerCase().startsWith(lowercasePrefix);
    }
}
