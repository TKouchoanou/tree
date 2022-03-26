package malo.bloc.tree.mapper.tree;

import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.persistence.entity.Tree;



public interface TreeDto2EntityMapper{
    Tree  toEntity(NewTreeDto o);
    Tree toEntity(TreeDto o);
    TreeDto toDto(Tree o);
}
