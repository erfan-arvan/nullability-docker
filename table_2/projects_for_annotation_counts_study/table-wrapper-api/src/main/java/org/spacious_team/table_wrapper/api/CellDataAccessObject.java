package org.spacious_team.table_wrapper.api;
public interface CellDataAccessObject<C, R extends ReportPageRow> {
    ZoneId defaultZoneId = ZoneId.systemDefault();
    Pattern spacePattern = Pattern.compile("\\s");
    String NO_CELL_VALUE_EXCEPTION_MESSAGE = "Cell doesn't contains value";
    C getCell(R row, Integer cellIndex);
    Object getValue(C cell);
    default int getIntValue(C cell) {
        return (int) getLongValue(cell);
    }
    default long getLongValue(C cell) {
         Object value = getValue(cell);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value != null) {
            return Long.parseLong(spacePattern.matcher((CharSequence) value).replaceAll(""));
        } else {
            throw new NullPointerException(NO_CELL_VALUE_EXCEPTION_MESSAGE);
        }
    }
    default double getDoubleValue(C cell) {
         Object value = getValue(cell);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value != null) {
            String str = spacePattern.matcher((CharSequence) value).replaceAll("");
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                if (str.indexOf(',') != -1) {
                    return Double.parseDouble(str.replace(',', '.'));
                } else if (str.indexOf('.') != -1) {
                    return Double.parseDouble(str.replace('.', ','));
                }
                throw e;
            }
        } else {
            throw new NullPointerException(NO_CELL_VALUE_EXCEPTION_MESSAGE);
        }
    }
    default BigDecimal getBigDecimalValue(C cell) {
        String number = getStringValue(cell);
        number = number.replace(',', '.');
        return (Objects.equals(number, "0") || Objects.equals(number, "0.0")) ?
                BigDecimal.ZERO : new BigDecimal(number);
    }
    default String getStringValue(C cell) {
        Object value = requireNonNull(getValue(cell), "Not a string");
        return value.toString();
    }
    Instant getInstantValue(C cell);
    default LocalDateTime getLocalDateTimeValue(C cell) {
        return getInstantValue(cell)
                .atZone(defaultZoneId)
                .toLocalDateTime();
    }
    default Object getValue(R row, Integer cellIndex) {
         C cell = getCell(row, cellIndex);
        return (cell == null) ? null : getValue(cell);
    }
    default int getIntValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getIntValue(cell);
    }
    default long getLongValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getLongValue(cell);
    }
    default double getDoubleValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getDoubleValue(cell);
    }
    default BigDecimal getBigDecimalValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getBigDecimalValue(cell);
    }
    default String getStringValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getStringValue(cell);
    }
    default Instant getInstantValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getInstantValue(cell);
    }
    default LocalDateTime getLocalDateTimeValue(R row, Integer cellIndex) {
        C cell = requireNonNull(getCell(row, cellIndex), "Cell not found");
        return getLocalDateTimeValue(cell);
    }
}
