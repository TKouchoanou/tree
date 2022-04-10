package malo.bloc.tree.exports;

import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Component
public class UserExport extends GenericExport<User>{
    @Autowired
    UserService userService;

    @Override
    public List<LinkedHashMap<String, Object>> beanToMap(List<User> beans) {
        return  beans.stream().map(this::toData).collect(Collectors.toList());
    }

    @Override
    public void setDefaultDataList() {
        List<User> users =
                StreamSupport.stream(userService.getAllUsers().spliterator(),false)
                        .collect(Collectors.toList());
        super.setDataList(beanToMap(users));
    }

    private LinkedHashMap<String,Object> toData(User user){
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
}
