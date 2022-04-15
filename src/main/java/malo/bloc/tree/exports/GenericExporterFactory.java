package malo.bloc.tree.exports;

import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericExporterFactory<R extends Exporter,T> implements ExporterFactory<T>{

    HashMap<Class<?>, R> factory = new HashMap<>();
    HashMap<String,R> altFactory=new HashMap<>();
    @PostConstruct
    private void InitExporter(){
       initFactoryMap(factory);
        altFactory();
    }
    

    private void altFactory(){
        for (Map.Entry<Class<?>,R> exportEntry: factory.entrySet()) {
            altFactory.put(exportEntry.getKey().getSimpleName(),exportEntry.getValue());
        }
    }

    /**
     * method to configure factory
     * in mapping a type with ExcelExport class
     */
     protected abstract void initFactoryMap(HashMap<Class<?>, R> factory);

    @Override
    public Exporter <T> getExporter(Class<?> type) {
        InitExporter();
        Exporter exporter= factory.get(type);
        Assert.notNull(exporter,"No exporter find with type "+type);
        return exporter;
    }

    @Override
    public  Exporter<T> getExporter(String type) {
        InitExporter();
        Exporter exporter= altFactory.get(type);
        Assert.notNull(exporter,"No exporter find with type "+type);
        return exporter;
    }
}
