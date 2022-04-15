package malo.bloc.tree.exports.csv;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GenericExporter <T>implements CsvExporter<T> {
    protected abstract LinkedHashMap<String,String> beanToMapData(T beans);
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }


    public void export(HttpServletResponse response, List<T> beans) throws IOException {

        List<LinkedHashMap<String, String>> dataLists = beans.stream().map(this::beanToMapData).collect(Collectors.toList());
        String[] headers= dataLists.stream().findFirst().get().keySet().toArray(new String[]{});

        List<String[]> dataLines = dataLists.stream().map(data -> data.values().toArray(new String[]{})).collect(Collectors.toList());

        ServletOutputStream outputStream = response.getOutputStream();


        outputStream.write(convertToCSV(headers).getBytes());

        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(line-> {
                    try {
                        line="\n"+line;
                        outputStream.write(line.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

    }
    public void export(HttpServletResponse response) throws IOException {
      throw new IOException("This method export is not implemented");
    }

}
