package malo.bloc.tree.controller;

import lombok.SneakyThrows;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.dtos.error.ErrorDtoInterface;
import malo.bloc.tree.mapper.Dto2EntityMapper;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
    Dto2EntityMapper mapper;

    @Autowired
    ErrorDtoInterface error;

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
        return (UserDto) mapper.toDto(userService.getUserById(id),UserDto.class);
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

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            Optional<User> user = userService.delete(id);
            if (user.isPresent()) {
                UserDto userDto= (UserDto) mapper.toDto(user.get(),UserDto.class);
                return new ResponseEntity<>(userDto,HttpStatus.OK);
            }
            return new ResponseEntity<>(error.setMessage("Failed to delete user with id ="+id),HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().
                    contentType(MediaType.APPLICATION_JSON).
                    body(error.setMessage(e.getMessage()));
        }
    }
}
