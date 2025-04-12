package org.spacious_team.table_wrapper.api;
public abstract class AbstractReportPage<T extends ReportPageRow> implements ReportPage {
    @Override
    public abstract T getRow(int i);
}
