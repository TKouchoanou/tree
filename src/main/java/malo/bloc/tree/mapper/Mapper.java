package malo.bloc.tree.mapper;

import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.dtos.create.NewUserDto;
import malo.bloc.tree.mapper.tree.TreeDto2EntityMapper;
import malo.bloc.tree.mapper.user.UserDto2EntityMapper;
import malo.bloc.tree.persistence.entity.NodeLeaf;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class Mapper implements TreeDto2EntityMapper, UserDto2EntityMapper {
    private final ModelMapper modelMapper=new ModelMapper();



    @Override
    public User toEntity(UserDto o) {
        User user = this.modelMapper.map( o,User.class);
        return handleUserAssociationForSave(user);
    }

    @Override
    public UserDto toDto(User o) {
        return this.modelMapper.map( o,UserDto.class);
    }

    @Override
    public User toEntity(NewUserDto o) {
        User user = this.modelMapper.map( o,User.class);
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
            tree.getChildren().forEach(child -> child.setParent(tree));
            tree.getChildren().forEach(this::handleTreeAssociationForSave);
        return tree;
    }

    private NodeLeaf handleLeafAssociationForSave(NodeLeaf leaf){
        leaf.getLinks().forEach(l -> l.setLeaf(leaf));
        leaf.getMetadata().forEach(m-> m.setLeaf(leaf));
        return leaf;
    }
}
