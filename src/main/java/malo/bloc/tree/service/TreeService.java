package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TreeService {
    @Autowired
   private TreeRepository treeRepository;

 public Tree save(Optional<Tree> tree){
     Tree entity= tree.get();
     return treeRepository.save(entity);
 }

 public Iterable <Tree> getAllTree(){
     return treeRepository.findAll();
 }
}
