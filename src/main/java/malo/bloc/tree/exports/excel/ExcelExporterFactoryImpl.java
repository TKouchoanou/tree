package malo.bloc.tree.exports.excel;

import malo.bloc.tree.exports.GenericExporterFactory;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ExcelExporterFactoryImpl<T> extends GenericExporterFactory<ExcelExporter,T> implements ExcelExporterFactory<T> {
    public void initFactoryMap(HashMap<Class<?>, ExcelExporter> factory){
        factory.put(User.class,new UserExporter());
    }


}
