package org.spacious_team.table_wrapper.api;
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTableCell<T> implements TableCell {
    private final T cell;
    private final CellDataAccessObject<T, ?> dao;
    @Override
    public Object getValue() {
        return dao.getValue(cell);
    }
    @Override
    public int getIntValue() {
        return dao.getIntValue(cell);
    }
    @Override
    public long getLongValue() {
        return dao.getLongValue(cell);
    }
    @Override
    public Double getDoubleValue() {
        return dao.getDoubleValue(cell);
    }
    @Override
    public BigDecimal getBigDecimalValue() {
        return dao.getBigDecimalValue(cell);
    }
    @Override
    public String getStringValue() {
        return dao.getStringValue(cell);
    }
    @Override
    public Instant getInstantValue() {
        return dao.getInstantValue(cell);
    }
    @Override
    public LocalDateTime getLocalDateTimeValue() {
        return dao.getLocalDateTimeValue(cell);
    }
}
