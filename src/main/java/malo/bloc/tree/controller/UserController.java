package malo.bloc.tree.controller;

import lombok.SneakyThrows;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.dtos.error.ErrorDto;
import malo.bloc.tree.exceptions.exceptions.ErrorCode;
import malo.bloc.tree.exceptions.exceptions.HomeException;
import malo.bloc.tree.exceptions.exceptions.InvalidEntityException;
import malo.bloc.tree.mapper.user.UserDto2EntityMapper;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserDto2EntityMapper mapper;


    @GetMapping(value = "/users")
    public Iterable<User> users(){
        return  userService.getAllUsers();
    }

    @GetMapping(value = "/users/ids")
    public Iterable<Integer> userIds(){
        return  StreamSupport.stream(userService.getAllUsers().spliterator(),false).map(User::getId).collect(Collectors.toList());
    }

    @GetMapping(value = "/user/{id}")
    public UserDto show(@PathVariable("id") int id){
        return  mapper.toDto(userService.getUserById(id));
    }

    @SneakyThrows
    @PostMapping(value = "/user")
    public UserDto create(@Valid @RequestBody NewUserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidEntityException("Invalid new user ", ErrorCode.INVALID_ENTITY_USER,bindingResult.getFieldErrors());
        }
        User user = mapper.toEntity(userDto);
        return  mapper.toDto(userService.create(user));
    }

    @PutMapping(value = "/user/{id}")
    public UserDto update(@PathVariable("id") int id, @Valid @RequestBody UserDto userDto, BindingResult bindingResult){
        System.out.println(userDto.getTree().getNodeLeaf());
        if(!Objects.equals(id,userDto.getId())){
            throw new HomeException("User IDs in path don't match with user id in entity");
        }
        User user =  mapper.toEntity(userDto);
        return  mapper.toDto(userService.update(user));
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
            Optional<User> user = userService.delete(id);
            if (user.isPresent()) {
                UserDto userDto=  mapper.toDto(user.get());
                return new ResponseEntity<>(userDto,HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body((new ErrorDto()).setMessage("Failed to delete user with id ="+id));
    }
}
