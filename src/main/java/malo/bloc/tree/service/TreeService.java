package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.repository.TreeRepository;
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

 public Tree save(Tree tree){
     return treeRepository.save(tree);
 }

  public Optional<Tree> findById(int id){
       return  treeRepository.findById(id);
    }

    public Optional<Tree> delete(Tree tree){
     treeRepository.deleteById(tree.getId());
     treeRepository.flush();
     Optional<Tree> shadowTree= treeRepository.findById(tree.getId());
     return shadowTree.isPresent()? Optional.empty() : Optional.of(tree);
    }

    protected Tree update(Tree tree){
        return treeRepository.save(tree);
    }

    public Tree getTreeByIdOrThrowException(int treeId){
        Optional<Tree> optionalTree = this.findById(treeId);
        if(optionalTree.isPresent()){
            return optionalTree.get();
        }
        throw new EntityNotFoundException("tree with id = "+treeId+" does not found for delete");
    }


    protected Tree addChildren(Tree parent, Collection<Tree> trees){
            parent.getChildren().addAll(trees);
            return this.save(parent);
    }

    protected Tree addChild(Tree parent,Tree child){
            parent.getChildren().add(child);
            return this.save(parent);
    }
    protected boolean hasChildWithId(Tree tree,int treeId){
        if(tree.getChildren().size()>0){
            Stream<Integer> ids= tree.getChildren().stream().map(Tree::getId);
            if(ids.anyMatch(id->treeId==id)){
                return true;
            }
            return tree.getChildren().stream().anyMatch(child->this.hasChildWithId(child,treeId));
        }
        return false;
    }


}
