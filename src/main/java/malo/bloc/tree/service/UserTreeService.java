package malo.bloc.tree.service;


import malo.bloc.tree.exceptions.exceptions.ErrorCode;
import malo.bloc.tree.exceptions.exceptions.NotResourceOwnerException;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
@Service
public class UserTreeService {
    @Autowired
    private TreeService treeService;
    @Autowired
    private UserService userService;

    public Tree addChildrenForUserTree(int userId, int treeId, Collection<Tree> children) throws NotResourceOwnerException {
        Tree tree= this.getTreeByUserAndTreeOrThrowException(userId,treeId);
        return  treeService.addChildren(tree,children);
    }

    public Tree addChildForUserTree(int userid, int treeId,Tree child) throws NotResourceOwnerException {
        Tree tree =this.getTreeByUserAndTreeOrThrowException(userid,treeId);
        return  treeService.addChild(tree,child);
    }

    public Tree updateUserTree(int userId,Tree tree) throws NotResourceOwnerException {
        treeService.AssertNotEmptyTreeId(tree.getId());
        User user = userService.getUserByIdOrThrowException(userId);
        AssertIsResourceOwner(user,tree);
        return treeService.update(tree);
    }

    public Optional<Tree> findById(int userid, int treeId) throws NotResourceOwnerException {
        Tree tree= this.getTreeByUserAndTreeOrThrowException(userid,treeId);
        return Optional.ofNullable(tree);
    }

    public User addTreeToUser(int userId,Tree tree){
        User user = userService.getUserByIdOrThrowException(userId);
        user.setTree(tree).getTree().setUser(user);
        return  userService.update(user);
    }

    public Optional<Tree> deleteUserTree(int userId, int id) throws NotResourceOwnerException {
        Tree tree= this.getTreeByUserAndTreeOrThrowException(userId,id);
        return treeService.delete(tree) ;
    }

    public Tree getTreeByUserAndTreeOrThrowException(int userId, int treeId) throws NotResourceOwnerException {
        Tree tree= treeService.getTreeByIdOrThrowException(treeId);
        User user = userService.getUserByIdOrThrowException(userId);
        AssertIsResourceOwner(user,tree);
        return tree;
    }

    public boolean isOwner(User user, Tree tree){
        return Objects.equals(user.getTree().getId(), tree.getId()) || treeService.hasChildWithId(user.getTree(), tree.getId());
    }

    private void AssertIsResourceOwner(User user, Tree tree) throws NotResourceOwnerException {
        if(!isOwner(user,tree))
            throw new NotResourceOwnerException("user with id = "+user.getId()+" is not owner of tree with id = "+tree.getId(), ErrorCode.NOT_TREE_OWNER);

    }


}
