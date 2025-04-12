package org.spacious_team.table_wrapper.csv;
@NoArgsConstructor(access = PRIVATE)
final class DateTimeFormatParser {
    private static final Map<Integer, DateTimeFormatter> dateTimeFormatters = new ConcurrentHashMap<>();
    static DateTimeFormatter getFor(String dateTime) {
        return (dateTime.length() == 10) ?
            getForDate(dateTime) :
            getForDateTime(dateTime);
    }
    static DateTimeFormatter getForDate(String date) {
        boolean isYearAtFirst;
        char dateSplitter;
        char ch = date.charAt(date.length() - 5);
        if (!Character.isDigit(ch)) {
            isYearAtFirst = false;
            dateSplitter = ch;
        } else {
            isYearAtFirst = true;
            dateSplitter = date.charAt(date.length() - 3);
        }
        return getDateFormatter(isYearAtFirst, dateSplitter);
    }
    static DateTimeFormatter getForDateTime(String dateTime) {
        boolean isDateAtFirst;
        boolean isYearAtFirst;
        char dateSplitter;
        if (dateTime.charAt(2) == ':') {
            isDateAtFirst = false;
            char ch = dateTime.charAt(dateTime.length() - 5);
            if (!Character.isDigit(ch)) {
                isYearAtFirst = false;
                dateSplitter = ch;
            } else {
                isYearAtFirst = true;
                dateSplitter = dateTime.charAt(dateTime.length() - 3);
            }
        } else {
            isDateAtFirst = true;
            if (!Character.isDigit(dateTime.charAt(2))) {
                isYearAtFirst = false;
                dateSplitter = dateTime.charAt(2);
            } else {
                isYearAtFirst = true;
                dateSplitter = dateTime.charAt(4);
            }
        }
        return getDateTimeFormatter(isDateAtFirst, isYearAtFirst, dateSplitter);
    }
    private static DateTimeFormatter getDateFormatter(boolean isYearAtFirst, char dateSplitter) {
        Integer key = dateSplitter + 0x40000 + (isYearAtFirst ? 0x20000 : 0);
         DateTimeFormatter result = dateTimeFormatters.get(key);
        if (result == null) {
            StringBuilder format = new StringBuilder();
            appendDate(isYearAtFirst, dateSplitter, format);
            result = DateTimeFormatter.ofPattern(format.toString());
            dateTimeFormatters.putIfAbsent(key, result);
        }
        return result;
    }
    private static DateTimeFormatter getDateTimeFormatter(boolean isDateAtFirst, boolean isYearAtFirst, char dateSplitter) {
        Integer key = dateSplitter + (isDateAtFirst ? 0x10000 : 0) + (isYearAtFirst ? 0x20000 : 0);
         DateTimeFormatter result = dateTimeFormatters.get(key);
        if (result == null) {
            StringBuilder format = new StringBuilder();
            if (isDateAtFirst) {
                appendDate(isYearAtFirst, dateSplitter, format);
                format.append(" ");
                appendTime(format);
            } else {
                appendTime(format);
                format.append(" ");
                appendDate(isYearAtFirst, dateSplitter, format);
            }
            result = DateTimeFormatter.ofPattern(format.toString());
            dateTimeFormatters.putIfAbsent(key, result);
        }
        return result;
    }
    private static void appendDate(boolean isYearAtFirst, char dateSplitter, StringBuilder format) {
        if (isYearAtFirst) {
            format.append("yyyy").append(dateSplitter).append("MM").append(dateSplitter).append("dd");
        } else {
            format.append("dd").append(dateSplitter).append("MM").append(dateSplitter).append("yyyy");
        }
    }
    private static void appendTime(StringBuilder format) {
        format.append("HH:mm:ss");
    }
}
