package malo.bloc.tree.exports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface Exporter <T>{
    void export(HttpServletResponse response, List<T> beans) throws IOException;
    void export(HttpServletResponse response) throws IOException;
}
