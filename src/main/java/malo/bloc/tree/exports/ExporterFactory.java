package malo.bloc.tree.exports;

public interface ExporterFactory <T>{
    Exporter<T> getExporter(Class<?> type);
    Exporter<T> getExporter(String type);
}
