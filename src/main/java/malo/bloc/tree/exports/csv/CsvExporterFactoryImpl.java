package malo.bloc.tree.exports.csv;

import malo.bloc.tree.exports.GenericExporterFactory;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class CsvExporterFactoryImpl<T> extends GenericExporterFactory<CsvExporter,T> implements CsvExporterFactory<T> {
    @Autowired
    private CsvUserExporter csvUserExporter;
    @Override
    protected void initFactoryMap(HashMap<Class<?>, CsvExporter> factory) {
       factory.put(User.class,csvUserExporter);
    }
}
