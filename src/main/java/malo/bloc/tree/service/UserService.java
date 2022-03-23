package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.NodeLeaf;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(this.handleAssociationForSave(user));
    }

    public User update(User user){
        return userRepository.save(this.handleAssociationForSave(user));
    }

    public Iterable <User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.getById(id);
    }
    
    private User handleAssociationForSave(User user){
        Tree tree = user.getTree();
        NodeLeaf leaf = tree.getNodeLeaf();
        tree.setUser(user);
        leaf.setTree(tree);
        leaf.getLinks().forEach(l -> l.setLeaf(leaf));
        leaf.getMetadata().forEach(m-> m.setLeaf(leaf));
        return user;
    }
}
