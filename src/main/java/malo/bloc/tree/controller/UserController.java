package malo.bloc.tree.controller;

import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/users")
    public List<User> users(){
        return (List<User>) userService.getAllUsers();
    }

    @PostMapping(value = "/user")
    public User save(@RequestBody User user){
      return  userService.save(user);
    }
}
