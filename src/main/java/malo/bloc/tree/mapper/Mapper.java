package malo.bloc.tree.mapper;

import malo.bloc.tree.dtos.PartialUserDto;
import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.mapper.tree.TreeDto2EntityMapper;
import malo.bloc.tree.mapper.user.UserDto2EntityMapper;
import malo.bloc.tree.persistence.entity.NodeLeaf;
import malo.bloc.tree.persistence.entity.Role;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class Mapper implements TreeDto2EntityMapper, UserDto2EntityMapper {
    private final ModelMapper modelMapper=new ModelMapper();
    @Autowired
    private RoleService roleService;

    @Override
    public User toEntity(UserDto userDto) {
        User user = this.modelMapper.map( userDto,User.class);
        toEntityHandleRoles(user,userDto);
        return handleUserAssociationForSave(user);
    }

    @Override
    public UserDto toDto(User user) {
        UserDto userDto = this.modelMapper.map( user,UserDto.class);
        toDtoHandleRoles(user,userDto);
        return userDto;
    }

    @Override
    public User toEntity(NewUserDto userDto) {
        User user = this.modelMapper.map(userDto,User.class);
        toEntityHandleRoles(user,userDto);
        return handleUserAssociationForSave(user);
    }


    @Override
    public Tree toEntity(NewTreeDto o) {
        Tree tree = this.modelMapper.map( o,Tree.class);
        return handleTreeAssociationForSave(tree);
    }

    @Override
    public TreeDto toDto(Tree o) {
        return this.modelMapper.map( o,TreeDto.class);
    }


    @Override
    public Tree toEntity(TreeDto o) {
        Tree tree = this.modelMapper.map( o,Tree.class);
        return handleTreeAssociationForSave(tree);
    }

    private void toEntityHandleRoles(User user, PartialUserDto userDto){
        HashMap<String,Role> roles = roleService.getAllRolesMapByName();
        Set<Role> userRoles = userDto.getRoleNames().stream().map(roles::get).collect(Collectors.toSet());
        user.setRoles(userRoles);
    }

    private void toDtoHandleRoles(User user, PartialUserDto userDto){
        Set<String> rolesNames=user.getRoles().stream().map(Role::toString).collect(Collectors.toSet());
        userDto.setRoleNames(rolesNames);
    }

    private User handleUserAssociationForSave(User user){
        Tree tree = user.getTree();
        if(tree!=null)
            handleTreeAssociationForSave(tree.setUser(user));
        return user;
    }

    private Tree handleTreeAssociationForSave(Tree tree){
        NodeLeaf leaf = tree.getNodeLeaf();
        if(leaf!=null)
            handleLeafAssociationForSave(leaf.setTree(tree));
          if(tree.getChildren()!=null)  tree.getChildren().forEach(child -> child.setParent(tree));
        if(tree.getChildren()!=null) tree.getChildren().forEach(this::handleTreeAssociationForSave);
        return tree;
    }

    private NodeLeaf handleLeafAssociationForSave(NodeLeaf leaf){
        System.out.println(leaf);
        if(leaf.getLinks()!=null) leaf.getLinks().forEach(l -> l.setLeaf(leaf));
        if(leaf.getMetadatas()!=null)  leaf.getMetadatas().forEach(m-> m.setLeaf(leaf));
        return leaf;
    }
}
