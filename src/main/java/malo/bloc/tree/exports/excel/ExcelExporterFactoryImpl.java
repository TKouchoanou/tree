package malo.bloc.tree.exports.excel;

import malo.bloc.tree.exports.GenericExporterFactory;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ExcelExporterFactoryImpl<T> extends GenericExporterFactory<ExcelExporter,T> implements ExcelExporterFactory<T> {
    @Autowired
    private ExcelUserExporter excelUserExporter;
    public void initFactoryMap(HashMap<Class<?>, ExcelExporter> factory){
        factory.put(User.class,excelUserExporter);
    }


}
