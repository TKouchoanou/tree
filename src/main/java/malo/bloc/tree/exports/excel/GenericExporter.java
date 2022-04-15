package malo.bloc.tree.exports.excel;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class GenericExport <T>implements ExcelExporter<T> {
    Workbook workbook ;

    /**
     * LinkedHashMap to preserve the insertion order
     * HashMap :- HashMap never preserves your Insertion Order. It Internally uses a hashing Concept
     * by which it generates a HashCode to the Corresponding key and add it to the HashMap.
     * LinkedHashMap :- LinkedHashMap It preserves your Insertion Order. and keys will be found as same
     * order you Insert into this LinkedHashMap.
     * TreeMap :- The TreeMap class implements the Map interface by using a Tree. A TreeMap provides an efficient
     * means of storing key/value pairs in sorted order, and allows rapid retrieval.
     */
     protected abstract LinkedHashMap<String,Object> beanToMapData(T beans);
     protected abstract List<ExcelFileConf> buildConf(List<T> beans);
     protected abstract List<ExcelFileConf> getDefaultConf();
     private void setNewWorkBook(){
         this.workbook= new XSSFWorkbook();
     }
    private Sheet createNewSheet(@Nullable String name, @Nullable List<Pair<Integer,Integer>> columnsWidth){
        Sheet sheet = name==null || name.isEmpty()? workbook.createSheet() :  workbook.createSheet(name);
        if(columnsWidth==null){//par defaut mais pas obligatoire
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);
        }else {
            columnsWidth.forEach(cw->sheet.setColumnWidth(cw.getKey(),cw.getValue()));
        }

        return sheet;
    }

    private void createHeader(Sheet sheet,List<String> columns){
        Row header = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);
        IntStream.range(0,columns.size()).forEach(index->createCell(sheet,header,headerStyle, columns.get(index), index));
    }

    private void createValues(Sheet sheet,List<LinkedHashMap<String,Object>> dataList){
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        IntStream.range(1,dataList.size()+1).forEach(index->{
            Row row = sheet.createRow(index);
             LinkedHashMap<String,Object> data = dataList.get(index-1);
             int i =0;
            for (Map.Entry each:data.entrySet()) {
                createCell(sheet,row,style,each.getValue(),i++);
            }
        });
    }

    public Cell createCell(Sheet sheet,Row row, CellStyle style, Object value, int column){
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
        setNewWorkBook();
        List<ExcelFileConf> confs = buildConf(beans);
        confs.forEach(this::buildSheet);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
    public void export(HttpServletResponse response) throws IOException {
        setNewWorkBook();
        List<ExcelFileConf> confs = getDefaultConf();
        confs.forEach(this::buildSheet);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }
    private void buildSheet(ExcelFileConf conf){
        conf.setDataLists(conf.beans,this::beanToMapData);
        Assert.noNullElements(conf.dataLists,"dataList can't be null, please set users");
        List<String> columns= conf.dataLists.stream().findFirst().map(stringObjectHashMap -> new ArrayList<>(stringObjectHashMap.keySet())).orElse(new ArrayList<>());
        Sheet sheet = createNewSheet(conf.sheetName,conf.columnsWidth);
        createHeader(sheet,columns);
        createValues(sheet,conf.dataLists);
    }

    public ExcelFileConf newConf(){
        return new ExcelFileConf();
    }

    @Getter
    @Accessors(chain = true)
    protected class ExcelFileConf{
        //nom feuille excel
        @Setter
        public String sheetName;
        //taille par colonne
        @Setter
        public List<Pair<Integer,Integer>> columnsWidth;
        //données champ valeur
        private List<LinkedHashMap<String,Object>> dataLists;
        //beans
        @Setter
        public List<T> beans;

        private void setDataLists(List<T> beans, Function< T,  LinkedHashMap<String,Object>> mapper) {
            this.dataLists = beans.stream().map(mapper).collect(Collectors.toList());
        }
    }

}
