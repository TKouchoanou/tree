package malo.bloc.tree.exports.csv;

import malo.bloc.tree.exports.GenericExporterFactory;
import malo.bloc.tree.exports.excel.ExcelExporter;
import malo.bloc.tree.exports.excel.ExcelExporterFactory;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class CsvExporterFactoryImpl<T> extends GenericExporterFactory<CsvExporter,T> implements CsvExporterFactory<T> {
    @Override
    protected void initFactoryMap(HashMap<Class<?>, CsvExporter> factory) {
       factory.put(User.class,new UserExporter());
    }
}
