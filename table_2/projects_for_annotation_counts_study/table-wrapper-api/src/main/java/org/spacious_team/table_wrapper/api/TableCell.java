package org.spacious_team.table_wrapper.api;
public interface TableCell {
    int getColumnIndex();
    Object getValue();
    int getIntValue();
    long getLongValue();
    Double getDoubleValue();
    BigDecimal getBigDecimalValue();
    String getStringValue();
    Instant getInstantValue();
    LocalDateTime getLocalDateTimeValue();
    default Object getValueOrDefault( Object defaultValue) {
        try {
            return getValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default int getIntValueOrDefault(int defaultValue) {
        try {
            return getIntValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default long getLongValueOrDefault(long defaultValue) {
        try {
            return getLongValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default double getDoubleValue(double defaultValue) {
        try {
            return getDoubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default BigDecimal getBigDecimalValueOrDefault(BigDecimal defaultValue) {
        try {
            return getBigDecimalValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default String getStringValueOrDefault(String defaultValue) {
        try {
            return getStringValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default Instant getInstantValueOrDefault(Instant defaultValue) {
        try {
            return getInstantValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
    default LocalDateTime getLocalDateTimeValueOrDefault(LocalDateTime defaultValue) {
        try {
            return getLocalDateTimeValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
