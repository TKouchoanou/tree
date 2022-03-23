package malo.bloc.tree.controller;

import lombok.SneakyThrows;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.mapper.Dto2EntityMapper;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    Dto2EntityMapper mapper;

    @GetMapping(value = "/users")
    public List<User> users(){
        return (List<User>) userService.getAllUsers();
    }

    @SneakyThrows
    @PostMapping(value = "/user")
    public UserDto save(@Valid @RequestBody NewUserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new Exception(""+bindingResult.getFieldErrors()+" "+bindingResult.getFieldErrors());
        }
        User user =(User) mapper.toEntity(userDto,User.class);
        return  (UserDto) mapper.toDto(userService.save(user),UserDto.class);
    }

    @PutMapping(value = "/user/{id}")
    public UserDto update(@PathVariable("id") int id, @Valid @RequestBody UserDto userDto, BindingResult bindingResult){
        if(!Objects.equals(id,userDto.getId())){
            throw new IllegalArgumentException("User IDs don't match");
        }

        User user = (User) mapper.toEntity(userDto,User.class);
        return (UserDto) mapper.toDto(userService.update(user),UserDto.class);
    }
}
