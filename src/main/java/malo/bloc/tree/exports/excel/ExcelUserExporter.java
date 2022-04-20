package malo.bloc.tree.exports.excel;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Component
public class ExcelUserExporter extends GenericExporter<User> {
    @Autowired
    UserService userService;
    private static final int SHEET_SIZE=100;
    private final String sheetName = "Users";


/**
    public List<LinkedHashMap<String, Object>> beanToMap(List<User> beans) {
        return  beans.stream().map(this::beanToMapData).collect(Collectors.toList());
    }**/


    protected LinkedHashMap<String,Object> beanToMapData(User user){
        LinkedHashMap<String,Object> data =new LinkedHashMap<>();
        data.put("id",user.getId());
        data.put("firstName",user.getFirstName());
        data.put("lastName",user.getLastName());
        data.put("email",user.getEmail());
        if(user.getTree()!=null)
            data.put("treeId",user.getTree().getId());
        else
            data.put("treeId","");
        return data;
    }

    @Override
    protected List<GenericExporter<User>.ExcelFileConf> buildConf(List<User> beans) {

        List<GenericExporter<User>.ExcelFileConf> fileConfs = new ArrayList<>();
        for (List<User> batchBeans:Lists.partition(beans, SHEET_SIZE)) {
            //each excel leaf conf
            fileConfs.add(getConf(batchBeans, sheetName));
        }
        return fileConfs;
    }

    @Override
    protected List<GenericExporter<User>.ExcelFileConf> getDefaultConf() {
        List<User> users =
                StreamSupport.stream(userService.getAllUsers().spliterator(),false)
                        .collect(Collectors.toList());
        return buildConf(users);
    }

    private GenericExporter<User>.ExcelFileConf getConf(List<User> beans, String sheetName){
        Pair<Integer,Integer> columnOneWidth= new Pair<>(0,6000);
        Pair<Integer,Integer> columnTwoWidth= new Pair<>(1,4000);
        List<Pair<Integer,Integer>> columnsWidth = new ArrayList<>();
        columnsWidth.add(columnOneWidth);
        columnsWidth.add(columnTwoWidth);

        GenericExporter<User>.ExcelFileConf conf= newConf();
        conf.setBeans(beans);
       return  conf.setColumnsWidth(columnsWidth).setSheetName(sheetName);


    }


}
