package org.spacious_team.table_wrapper.api;
public class TableFactoryRegistry {
    private static final Collection<TableFactory> factories = new HashSet<>();
    public static void add(TableFactory tableFactory) {
        factories.add(tableFactory);
    }
    public static Collection<TableFactory> getAll() {
        return Collections.unmodifiableCollection(factories);
    }
    public static TableFactory get(ReportPage reportPage) {
        for (TableFactory factory : factories) {
            if (factory.canHandle(reportPage)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("Нет парсера для отчета формата " + reportPage.getClass().getSimpleName());
    }
}
