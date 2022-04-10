package malo.bloc.tree.exports;

import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public abstract class GenericExport <T>{
    Workbook workbook ;
    List<String> columns;
    Sheet sheet;
    @Setter
    List<LinkedHashMap<String,Object>> dataList;

    /**
     * LinkedHashMap to preserve the insertion order
     * HashMap :- HashMap never preserves your Insertion Order. It Internally uses a hashing Concept
     * by which it generates a HashCode to the Corresponding key and add it to the HashMap.
     * LinkedHashMap :- LinkedHashMap It preserves your Insertion Order. and keys will be found as same
     * order you Insert into this LinkedHashMap.
     * TreeMap :- The TreeMap class implements the Map interface by using a Tree. A TreeMap provides an efficient
     * means of storing key/value pairs in sorted order, and allows rapid retrieval.
     */
     public abstract List<LinkedHashMap<String,Object>> beanToMap(List<T> beans);
     public abstract void setDefaultDataList();

    protected void init(){
        workbook= new XSSFWorkbook();
        sheet = workbook.createSheet("Users");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
    }
    private void createHeader(){
        Row header = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);
        IntStream.range(0,columns.size()).forEach(index->createCell(header,headerStyle, columns.get(index), index));
    }

    private void createValues(){
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        IntStream.range(1,dataList.size()+1).forEach(index->{
            Row row = sheet.createRow(index);
             LinkedHashMap<String,Object> data = dataList.get(index-1);
             int i =0;
            for (Map.Entry each:data.entrySet()) {
                createCell(row,style,each.getValue(),i++);
            }
        });
    }

    public Cell createCell(Row row, CellStyle style, Object value, int column){
        sheet.autoSizeColumn(column);
        Cell cell = row.createCell(column);
        cell.setCellStyle(style);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        return cell;
    }

    public void export(HttpServletResponse response, List<T> beans) throws IOException {
        init();
        this.setDataList(beanToMap(beans));
        Optional <LinkedHashMap<String,Object>> data = dataList.stream().findFirst();
        columns= data.map(stringObjectHashMap -> new ArrayList<>(stringObjectHashMap.keySet())).orElse(null);
        System.out.println(columns+" - "+dataList);
        createHeader();
        createValues();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
    public void export(HttpServletResponse response) throws IOException {
        init();
        this.setDefaultDataList();
        Assert.noNullElements(dataList,"dataList can't be null, please set users");
        createHeader();
        createValues();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }
}
