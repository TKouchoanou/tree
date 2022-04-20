package malo.bloc.tree.exports.csv;

import malo.bloc.tree.persistence.entity.User;
import org.springframework.stereotype.Component;


import java.util.LinkedHashMap;
@Component
public class CsvUserExporter extends GenericExporter<User> {
    @Override
    protected LinkedHashMap<String, String> beanToMapData(User user) {
        LinkedHashMap<String,String> data =new LinkedHashMap<>();
        data.put("id",user.getId().toString());
        data.put("firstName",user.getFirstName());
        data.put("lastName",user.getLastName());
        data.put("email",user.getEmail());
        if(user.getTree()!=null)
            data.put("treeId",user.getTree().getId().toString());
        else
            data.put("treeId","");

        data.put("Ville", user.getCity());
        return data;
    }
}
