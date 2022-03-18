package malo.bloc.tree.controller;

import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TreeController {
    @Autowired
    private TreeService treeService;
    @GetMapping(value= "/trees")
    public Iterable<Tree> trees(){
        return treeService.getAllTree();
    }
    @PostMapping(value= "/tree")
    public Tree save(@RequestBody Tree tree){
        Optional<Tree> optional= java.util.Optional.of(tree);
        return treeService.save(optional);
    }
}
