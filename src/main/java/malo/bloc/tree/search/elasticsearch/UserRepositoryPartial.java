package malo.bloc.tree.search.elasticsearch;

import malo.bloc.tree.persistence.repository.UserRepository;
import malo.bloc.tree.search.bean.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserRepositoryPartial extends GenericRepository<User,String> {

    @Value("classpath:elasticsearch/user-index.json")
    Resource resourceFile;
    @Autowired
    UserRepository persistenceUserRepository;

    ModelMapper mapper = new ModelMapper();
    //user1648737707553
    String indexName="user1648739182266";

    @Override
    String getBeanId(User bean) {
        return String.valueOf(bean.getId());
    }

    @Override
    protected Class<User> getBeanClass() {
        return User.class;
    }

    @Override
    java.lang.String getIndexName() {
        return indexName;
    }

    @Override
    java.lang.String getAliasName() {
        return null;
    }

    @Override
    java.lang.String getRelativePath() {
        return "elasticsearch/user-index.json";
    }

    @Override
    Iterable <User> getAllBeans() {
        return persistenceUserRepository
                .findAll()
                .stream()
                .map(this::toSearch)
                .collect(Collectors.toList());
    }

    User toSearch(malo.bloc.tree.persistence.entity.User user){
        String treeId= user.getTree()!=null?""+user.getTree().getId():"";
        return mapper.map(user,User.class).setEntityId(""+user.getId()).setTreeId(treeId);
    }

    }
