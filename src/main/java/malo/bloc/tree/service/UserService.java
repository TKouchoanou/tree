package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.NodeLeaf;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public User update(User user){
        return userRepository.save(user);
    }

    public Iterable <User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.getById(id);
    }

    public   Optional<User>  findById(int id){
        return userRepository.findById(id);
    }
    public User getUserByIdOrThrowException(int userId){
        Optional<User> optionalUser = this.findById(userId);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new EntityNotFoundException("user with id = "+userId+" does not found for delete");
    }

    public Optional<User> delete(int id){
        Optional<User> user = userRepository.findById(id);
       if(user.isPresent()) {
           userRepository.deleteById(id);
           userRepository.flush();
           Optional<User> shadowUser= userRepository.findById(id);
           return shadowUser.isPresent()? Optional.of(null) : user;
       }
       throw new EntityNotFoundException("user with id = "+id+" does not found for delete");
    }

}
