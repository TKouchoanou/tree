package malo.bloc.tree.exports;

import malo.bloc.tree.exports.csv.CsvExporterFactoryImpl;
import malo.bloc.tree.exports.excel.ExcelExporterFactoryImpl;
import malo.bloc.tree.exports.pdf.PdfExporterFactoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
@Component
public class FormatExporterFactoryImpl<T> implements FormatExporterFactory<T>{
    private final HashMap<String, ExporterFactory<T>> factory = new HashMap<>();
    @Override
    public ExporterFactory <T>getExporterFactory(String type) {
        ExporterFactory exporter= factory.get(type);
        Assert.notNull(exporter,"No exporter find with type "+type);
        return exporter;
    }

    @Override
    public String getExtension(String type) {
        String extension;
        switch(type){

            case "excel":
                extension="xlsx";
                break;

            case "pdf":
                extension="pdf";
                break;
            case "csv":
                extension="csv";
                break;
            default:
                throw new RuntimeException("Unsupported format "+type);
        }
        return extension;
    }

    @PostConstruct
    private void initFactory(){
        factory.put("excel",new ExcelExporterFactoryImpl());
        factory.put("pdf",new PdfExporterFactoryImpl());
        factory.put("csv",new CsvExporterFactoryImpl<>());
    }
}
