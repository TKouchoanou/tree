package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public Iterable <User> getAllUsers(){
        return userRepository.findAll();
    }
}
