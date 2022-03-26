package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.NodeLeaf;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.TreeRepository;
import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TreeService {
    @Autowired
   private TreeRepository treeRepository;
    @Autowired
    private UserRepository userRepository;

 public Tree save(Tree tree){
     return treeRepository.save(this.handleAssociationForSave(tree));
 }

  public Optional<Tree> getTreeById(int id){
        return Optional.of(treeRepository.getById(id));
    }

    private Tree handleAssociationForSave(Tree tree){
        NodeLeaf leaf = tree.getNodeLeaf();
        leaf.setTree(tree);
        leaf.getLinks().forEach(l -> l.setLeaf(leaf));
        leaf.getMetadata().forEach(m-> m.setLeaf(leaf));
        this.handleChildren(tree);
        return tree;
    }

    private Optional<Tree> delete(Tree tree){
     treeRepository.deleteById(tree.getId());
     treeRepository.flush();
     Optional<Tree> shadowTree= treeRepository.findById(tree.getId());
     return shadowTree.isPresent()? Optional.empty() : Optional.of(tree);
    }

    public Optional<Tree> delete(int userId,int id){
     Tree tree= this.getTreeByUserIdAndTreeIdOrThrowException(userId,id);
        return delete(tree) ;
    }

    public Tree addChildren(Tree parent, Collection<Tree> trees){
            parent.getChildren().addAll(trees);
            return this.save(parent);
    }

    protected Tree addChild(Tree parent,Tree child){
            parent.getChildren().add(child);
            return this.save(parent);
    }

    public Tree addChildForUserTree(int userid, int treeId,Tree child){
     Tree tree =this.getTreeByUserIdAndTreeIdOrThrowException(userid,treeId);
        return  this.addChild(tree,child);
    }


    public Tree addChildrenForUserTree(int userId, int treeId,Collection<Tree> children){
        Tree tree= this.getTreeByUserIdAndTreeIdOrThrowException(userId,treeId);
        return  this.addChildren(tree,children);
    }

    public Tree updateUserTree(int userId,Tree tree){
     if(tree.getId() == null || tree.getId().equals(0))
         throw new EntityNotFoundException("can't update tree with empty id ");
        User user = this.getUserByIdOrThrowException(userId);
        isOwnerOrThrowException(user,tree);
         return this.save(tree);
    }


    public User addTreeToUser(int userId,Tree tree){
        User user = this.getUserByIdOrThrowException(userId);
         user.setTree(tree).getTree().setUser(user);
         this.handleAssociationForSave(tree);
        return  userRepository.save(user);
    }

    public boolean userHasTree(User user,Tree tree){
       return this.hasChildWithId(user.getTree(),tree.getId());
    }

    private boolean hasChildWithId(Tree tree,int treeId){

        if(tree.getChildren().size()>0){
            Stream<Integer> ids= tree.getChildren().stream().map(Tree::getId);
            if(ids.anyMatch(id->treeId==id)){
                return true;
            }
            return tree.getChildren().stream().anyMatch(child->this.hasChildWithId(child,treeId));
        }
        return false;

    }

    private void handleChildren(Tree tree){
     if(!tree.getChildren().isEmpty()){
         tree.getChildren().forEach(child -> child.setParent(tree));
         tree.getChildren().forEach(child -> handleChildren(tree));
     }
    }


    public User getUserByIdOrThrowException(int userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
           return optionalUser.get();
        }
        throw new EntityNotFoundException("user with id = "+userId+" does not found for delete");
    }

    public Tree getTreeByIdOrThrowException(int treeId){
        Optional<Tree> optionalTree = this.getTreeById(treeId);
        if(optionalTree.isPresent()){
         return optionalTree.get();
        }
        throw new EntityNotFoundException("tree with id = "+treeId+" does not found for delete");
    }

    public Tree getTreeByUserIdAndTreeIdOrThrowException(int userId,int treeId){
        Tree tree= this.getTreeByIdOrThrowException(treeId);
        User user = this.getUserByIdOrThrowException(userId);
        isOwnerOrThrowException(user,tree);
        return tree;
    }

    private boolean isOwner( User user, Tree tree){
     return userHasTree(user,tree);
    }

    private void isOwnerOrThrowException( User user, Tree tree){
        if(!isOwner(user,tree))
            throw new EntityNotFoundException("user with id = "+user.getId()+" is not owner of tree with id = "+tree.getId());

    }

}
