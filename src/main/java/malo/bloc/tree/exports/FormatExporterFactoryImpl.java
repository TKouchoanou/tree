package malo.bloc.tree.exports;

import malo.bloc.tree.exceptions.exceptions.UnSupportedFormatException;
import malo.bloc.tree.exports.csv.CsvExporterFactoryImpl;
import malo.bloc.tree.exports.excel.ExcelExporterFactoryImpl;
import malo.bloc.tree.exports.pdf.PdfExporterFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class FormatExporterFactoryImpl<T> implements FormatExporterFactory<T>{
    private final HashMap<String, ExporterFactory<T>> factory = new HashMap<>();
    @Autowired
    private  ExcelExporterFactoryImpl excelExporterFactory;
    @Autowired
    private CsvExporterFactoryImpl csvExporterFactory;
    @Autowired
    private PdfExporterFactoryImpl pdfExporterFactory;
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
                throw new UnSupportedFormatException("Unsupported format "+type);
        }
        return extension;
    }

    @PostConstruct
    private void initFactory(){
        factory.put("excel",excelExporterFactory);
        factory.put("pdf",pdfExporterFactory);
        factory.put("csv",csvExporterFactory);
    }
}
