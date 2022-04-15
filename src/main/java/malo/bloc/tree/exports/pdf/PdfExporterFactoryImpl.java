package malo.bloc.tree.exports.pdf;

import malo.bloc.tree.exports.GenericExporterFactory;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.stereotype.Component;
import java.util.HashMap;
@Component
public class PdfExporterFactoryImpl<T>  extends GenericExporterFactory<PdfExporter,T> implements PdfExporterFactory<T> {
    @Override
    protected void initFactoryMap(HashMap<Class<?>, PdfExporter> factory) {
        factory.put(User.class,new UserExport());
    }

}
