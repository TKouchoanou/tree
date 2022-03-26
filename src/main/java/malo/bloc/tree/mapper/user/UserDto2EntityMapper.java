package malo.bloc.tree.mapper.user;

import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.persistence.entity.User;


public interface UserDto2EntityMapper{
    User toEntity(NewUserDto o);
    User toEntity(UserDto o);
    UserDto toDto(User o);
}
