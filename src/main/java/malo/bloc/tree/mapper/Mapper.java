package malo.bloc.tree.mapper;

import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.mapper.tree.TreeDto2EntityMapper;
import malo.bloc.tree.mapper.user.UserDto2EntityMapper;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class Mapper implements TreeDto2EntityMapper, UserDto2EntityMapper {
    private final ModelMapper modelMapper=new ModelMapper();



    @Override
    public User toEntity(UserDto o) {
        return this.modelMapper.map( o,User.class);
    }

    @Override
    public UserDto toDto(User o) {
        return this.modelMapper.map( o,UserDto.class);
    }

    @Override
    public User toEntity(NewUserDto o) {
        return this.modelMapper.map( o,User.class);
    }


    @Override
    public Tree toEntity(NewTreeDto o) {
        return this.modelMapper.map( o,Tree.class);
    }

    @Override
    public TreeDto toDto(Tree o) {
        return this.modelMapper.map( o,TreeDto.class);
    }


    @Override
    public Tree toEntity(TreeDto o) {
        return this.modelMapper.map( o,Tree.class);
    }
}
