package org.spacious_team.table_wrapper.api;
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTableFactory<T extends ReportPage> implements TableFactory {
    private final Class<T> reportPageType;
    @Override
    public boolean canHandle(ReportPage reportPage) {
        return reportPageType.isAssignableFrom(reportPage.getClass());
    }
    protected T cast(ReportPage reportPage) {
        return (T) reportPage;
    }
}