package malo.bloc.tree.service;


import malo.bloc.tree.exceptions.exceptions.EmptyIdException;
import malo.bloc.tree.exceptions.exceptions.EntityNotFoundException;
import malo.bloc.tree.exceptions.exceptions.ErrorCode;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;


    public User create(User user){
        String password= user.getPassword();
        user.setPassword(bcryptEncoder.encode(password));
        return userRepository.save(user);
    }

    public User update(User user){
        AssertNotEmptyUserId(user.getId());
        setPassWord(user);
        return userRepository.save(user);
    }


    public Iterable <User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        AssertNotEmptyUserId(id);
        System.out.println(userRepository.getById(id));
        return userRepository.getById(id);
    }

    public   Optional<User>  findById(int id){
        AssertNotEmptyUserId(id);
        return userRepository.findById(id);
    }
    public User getUserByIdOrThrowException(int userId){
       return this.findById(userId).orElseThrow(()->new EntityNotFoundException("user with id = "+userId+" does not found for delete", ErrorCode.ENTITY_USER_NOT_FOUND));
    }

    public Optional<User> delete(int id){
        User user = getUserByIdOrThrowException(id);
        userRepository.deleteById(id);
        userRepository.flush();
        Optional<User> shadowUser= userRepository.findById(id);
        return shadowUser.isPresent()? Optional.empty() :Optional.of(user);

    }
    public void AssertNotEmptyUserId(@Nullable Object id){
        if(id == null || id.equals(0))
            throw new EmptyIdException("can't update get or delete user with empty id ",ErrorCode.EMPTY_USER_ID);
    }

    private void setPassWord(User user){
        String password =userRepository.getById(user.getId()).getPassword();
        user.setPassword(password);
    }

}
