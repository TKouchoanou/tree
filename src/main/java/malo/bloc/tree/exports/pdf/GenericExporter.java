package malo.bloc.tree.exports.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class GenericExporter<T> implements PdfExporter<T> {
    Document document;

   // List<LinkedHashMap<String,Object>> dataLists;
   protected abstract LinkedHashMap<String,String> beanToMapData(T beans);
    private void chunk() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("List of Users \n ", font);
        document.add(chunk);
        document.add(new Chunk("      "));
    }
    private void image() throws DocumentException, IOException, URISyntaxException {
        Path path = Paths.get("src/main/resources/malo_logo.png");
        System.out.println(path.toAbsolutePath().toString());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);
    }

    private void table(List<String> columns,List<LinkedHashMap<String,String>>  dataLists) throws DocumentException, URISyntaxException, IOException {
        PdfPTable table = new PdfPTable(columns.size());
        table.setPaddingTop(5);
        addTableHeader(table,columns);
        addRows(table,dataLists);
        document.add(table);
    }
    private void addTableHeader(PdfPTable table,List<String> columns) {
        columns.forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private void addRows(PdfPTable table, List<LinkedHashMap<String,String>>  dataLists) {
        IntStream.range(1,dataLists.size()+1).forEach(index->{
            LinkedHashMap<String,String> data = dataLists.get(index-1);
            for (Map.Entry each:data.entrySet()) {
                //each row
                table.addCell((String) each.getValue());
            }
        });
    }
    private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    @Override
    public void export(HttpServletResponse response, List<T> beans) throws IOException {
        List<LinkedHashMap<String,String>>  dataLists = beans.stream().map(this::beanToMapData).collect(Collectors.toList());
        List<String> columns= dataLists.stream().findFirst().map(map -> new ArrayList<>(map.keySet())).orElse(new ArrayList<>());
        document = new Document();
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            chunk();
            document.add(new Paragraph("                          ...................................."));
            table(columns,dataLists);
            image();
            document.add(new Paragraph("Je suis Malo et je porte le nom de Saint-malo , Saint-Malo est une ville portuaire de Bretagne, au nord-ouest de la France."));
           // outputStream.close();
        } catch (DocumentException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        document.close();
    }



    @Override
    public void export(HttpServletResponse response) throws IOException {

    }
}
