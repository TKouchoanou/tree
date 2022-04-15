package malo.bloc.tree.exports;

public interface FormatExporterFactory<T> {
  ExporterFactory<T> getExporterFactory(String type) ;
  String getExtension(String type);
}
