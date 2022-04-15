package malo.bloc.tree.exports.pdf;

import malo.bloc.tree.persistence.entity.User;

import java.util.LinkedHashMap;

public class UserExport extends GenericExporter<User> {
    @Override
    protected LinkedHashMap<String,String> beanToMapData(User user){
        LinkedHashMap<String,String> data =new LinkedHashMap<>();
        data.put("id",user.getId().toString());
        data.put("firstName",user.getFirstName());
        data.put("lastName",user.getLastName());
        data.put("email",user.getEmail());
        if(user.getTree()!=null)
            data.put("treeId",user.getTree().getId().toString());
        else
            data.put("treeId","");
        return data;
    }
}
